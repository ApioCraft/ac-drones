package net.apiocraft.acdrones;

import dan200.computercraft.api.lua.ILuaCallback;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.shared.computer.core.ServerComputer;
import net.apiocraft.acdrones.entities.ComputerDroneEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.MovementType;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class DroneBrain implements IDroneAccess {
    private ComputerDroneEntity drone;

    private record QueuedDroneCommand(int id, DroneCommand command) {

    }

    boolean isCommandBusy = false;

    private CompletableFuture<DroneCommandResult> currentCommandResultPromise;
    private QueuedDroneCommand currentCommand;

    int didCommands;

    private final Queue<QueuedDroneCommand> commandQueue = new java.util.LinkedList<>();

    public DroneBrain(ComputerDroneEntity drone) {
        this.drone = drone;
    }

    @Override
    public Vec3d getPosition() {
        return drone.getPos();
    }

    @Override
    public ServerWorld getLevel() {
        return (ServerWorld) drone.getWorld();
    }

    public ComputerDroneEntity getOwner() {
        return drone;
    }

    private static final class DroneCommandCallback implements ILuaCallback {
        final MethodResult eventPull = MethodResult.pullEvent("drone_command_result", this);
        private final int id;

        DroneCommandCallback(int id) {
            this.id = id;
        }

        @Override
        public MethodResult resume(Object[] response) throws LuaException {
            System.out.println("Response: " + Arrays.toString(response));
            System.out.println("ID: " + id);
            System.out.println("Response length: " + response.length);
            System.out.println("Response[1] instanceof Number: " + (response[1] instanceof Number));
            System.out.println("Response[2] instanceof Boolean: " + (response[2] instanceof Boolean));
            if (response.length < 3 || !(response[1] instanceof Number id) || !(response[2] instanceof Boolean)) {
                return eventPull;
            }

            if (id.intValue() != this.id) return eventPull;

            System.out.println("ID matches");

            return MethodResult.of(Arrays.copyOfRange(response, 2, response.length));
        }
    }

    @Override
    public MethodResult executeCommand(DroneCommand command) {
        if(getLevel().isClient()) {
            throw new IllegalStateException("Cannot execute commands on the client side");
        }
        if(commandQueue.size() > 16) {
            return MethodResult.of(false, "The command queue is too fat");
        }
        commandQueue.offer(new QueuedDroneCommand(++didCommands, command));
        var commandId = didCommands;
        return new DroneCommandCallback(commandId).eventPull;

    }

    @Override
    public void setBusy(boolean busy) {
        isCommandBusy = busy;
    }

    @Override
    public boolean isBusy() {
        return isCommandBusy;
    }

    @Override
    public void move(Vec3d targetMovement) {
        this.drone.move(MovementType.SELF, targetMovement);
    }

    @Override
    public void setVelocity(Vec3d velocity) {
        this.drone.setVelocity(velocity);
    }

    @Override
    public Vec3d getVelocity() {
        return this.drone.getVelocity();
    }

    @Override
    public int getSelectedSlot() {
        return drone.getSelectedSlot();
    }

    @Override
    public Inventory getInventory() {
        return drone.getInventory();
    }

    @Override
    public void setSelectedSlot(int slot) {
        drone.setSelectedSlot(slot);
    }

    @Override
    public ComputerDroneEntity getEntity() {
        return drone;
    }

    public void update() {
        if(!getLevel().isClient()) {

            var computer = drone.getServerComputer();
            if(computer != null && !computer.getMainThreadMonitor().canWork()) {
                System.out.println("Cannot work");
                return;
            }

            //System.out.println(currentCommandResultPromise);

            if (checkWorkCommand(computer)) return; // if its still in progress, we don't wanna run more

            var command = commandQueue.poll();
            if(command == null) return;

            var start = System.nanoTime();
            var resultPromise = command.command.execute(this);
            this.currentCommandResultPromise = resultPromise;
            currentCommand = command;
            var end = System.nanoTime();

            if(computer == null) return;
            computer.getMainThreadMonitor().trackWork(end - start, TimeUnit.NANOSECONDS);
            checkWorkCommand(computer);
            computer.setPosition(getLevel(), drone.getBlockPos());
        }
    }

    private boolean checkWorkCommand(ServerComputer computer) {
        if(currentCommandResultPromise != null && currentCommandResultPromise.isDone()) {
            var result = currentCommandResultPromise.getNow(null);
            var callback_id = currentCommand.id;
            if(callback_id < 0) return true;

            if(result != null && result.isSuccess()) {
                var results = result.getResults();
                if(results != null) {
                    var args = new Object[results.length + 2];
                    args[0] = callback_id;
                    args[1] = true;
                    System.arraycopy(results, 0, args, 2, results.length);
                    computer.queueEvent("drone_command_result", args);
                } else {
                    computer.queueEvent("drone_command_result", new Object[]{
                            callback_id,
                            true
                    });
                }
                currentCommandResultPromise = null;
                currentCommand = null;
            } else {
                var message = result != null ? result.getErrorMessage() : null;
                computer.queueEvent("drone_command_result", new Object[]{
                        callback_id,
                        false,
                        message
                });
            }

            currentCommandResultPromise = null;
            currentCommand = null;
            //return true;
        } else {
            if(currentCommandResultPromise != null && currentCommandResultPromise.isCancelled()) {
                var callback_id = currentCommand.id;
                this.currentCommandResultPromise = null;
                this.currentCommand = null;
                if(callback_id < 0) return true;
                computer.queueEvent("drone_command_result", new Object[]{
                        callback_id,
                        false
                });
            } else {
                if(currentCommandResultPromise != null && !currentCommandResultPromise.isDone()) {
                    if(computer == null) return true;

                    var start = System.nanoTime();
                    currentCommand.command().update(this);
                    var end = System.nanoTime();
                    computer.getMainThreadMonitor().trackWork(end - start, TimeUnit.NANOSECONDS);
                    return true;
                }
            }
        }
        return false;
    }
}
