package net.apiocraft.acdrones.commands;

import net.apiocraft.acdrones.DroneCommand;
import net.apiocraft.acdrones.DroneCommandResult;
import net.apiocraft.acdrones.core.IDroneAccess;
import net.minecraft.item.ItemStack;

import java.util.concurrent.CompletableFuture;

public class DroneCompareCommand implements DroneCommand {
    private int slot;

    public DroneCompareCommand(int slot) {
        this.slot = slot;
    }
    @Override
    public CompletableFuture<DroneCommandResult> execute(IDroneAccess drone) {
        var selectedStack = drone.getInventory().getStack(drone.getSelectedSlot());
        var comparingStack = drone.getInventory().getStack(slot-1);
        return ItemStack.areItemsAndComponentsEqual(selectedStack, comparingStack) ? CompletableFuture.completedFuture(DroneCommandResult.success()) : CompletableFuture.completedFuture(DroneCommandResult.failure());
    }
}
