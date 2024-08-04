package net.apiocraft.acdrones.apis;

import dan200.computercraft.api.lua.ILuaAPI;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import net.apiocraft.acdrones.DroneCommandResult;
import net.apiocraft.acdrones.IDroneAccess;
import net.apiocraft.acdrones.commands.DroneLidarCommand;
import net.apiocraft.acdrones.commands.DroneMoveCommand;

import java.util.concurrent.CompletableFuture;

public class DroneAPI implements ILuaAPI {
    private final IDroneAccess drone;

    public DroneAPI(IDroneAccess drone) {
        this.drone = drone;
    }

    @Override
    public String[] getNames() {
        return new String[] { "drone" };
    }

    @LuaFunction(mainThread = true)
    public final Object[] getPosition() {
        return new Object[] { drone.getPosition().x, drone.getPosition().y, drone.getPosition().z };
    }

    @LuaFunction()
    public final MethodResult move(double x, double y, double z) {
        return drone.executeCommand(new DroneMoveCommand(x, y, z));
    }

    @LuaFunction()
    public final MethodResult select(int slot) {
        return drone.executeCommand((drone) -> {
            if(slot < 1 || slot > drone.getInventory().size()) {
                return CompletableFuture.completedFuture(DroneCommandResult.failure("Slot out of range"));
            }
            drone.setSelectedSlot(slot-1);
            return CompletableFuture.completedFuture(DroneCommandResult.success());
        });
    }

    @LuaFunction()
    public final MethodResult getSelectedSlot() {
        return MethodResult.of(drone.getSelectedSlot() + 1);
    }

    @LuaFunction()
    public final MethodResult lidar(double x, double y) {
        return drone.executeCommand(new DroneLidarCommand(x, y));
    }


}
