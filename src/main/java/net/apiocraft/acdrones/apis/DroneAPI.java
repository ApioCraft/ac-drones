package net.apiocraft.acdrones.apis;

import dan200.computercraft.api.detail.VanillaDetailRegistries;
import dan200.computercraft.api.lua.ILuaAPI;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import net.apiocraft.acdrones.DroneCommandResult;
import net.apiocraft.acdrones.core.IDroneAccess;
import net.apiocraft.acdrones.commands.*;
import net.minecraft.item.ItemStack;

import java.util.Optional;
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

    @LuaFunction()
    public final MethodResult suck(int quantity) {
        return drone.executeCommand(new DroneSuckCommand(quantity));
    }

    @LuaFunction()
    public final MethodResult suck() {
        return drone.executeCommand(new DroneSuckCommand(1));
    }

    @LuaFunction()
    public final MethodResult place() {
        return drone.executeCommand(new DroneUseCommand(false));
    }

    @LuaFunction()
    public final MethodResult compare(int slot) {
        return drone.executeCommand(new DroneCompareCommand(slot));
    }

    @LuaFunction(mainThread = true)
    public final MethodResult getRotation() {
        return MethodResult.of(new Object[] { drone.getEntity().getYaw(), drone.getEntity().getPitch() });
    }

    @LuaFunction(mainThread = true)
    public final MethodResult getItemDetail(Optional<Integer> slot) {
        int _slot = slot.orElse(drone.getSelectedSlot()+1)-1;
        ItemStack stack = drone.getInventory().getStack(_slot);
        return MethodResult.of(new Object[] { stack.isEmpty() ? null : VanillaDetailRegistries.ITEM_STACK.getDetails(stack) });
    }

}
