package net.apiocraft.acdrones.inventory;

import net.apiocraft.acdrones.core.IDroneAccess;
import net.apiocraft.acdrones.items.AccessoryItem;
import net.apiocraft.acdrones.registries.DroneAccessoryRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class AccessoryInventory implements Inventory {

    private final IDroneAccess drone;

    public AccessoryInventory(IDroneAccess drone) {
        this.drone = drone;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return drone.getAccessory() == null;
    }

    @Override
    public ItemStack getStack(int slot) {
        //System.out.println(drone.getAccessory());
        return drone.getAccessory() == null ? ItemStack.EMPTY : DroneAccessoryRegistry.createItemStack(drone.getAccessory());
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        if(slot != 0) {
            return ItemStack.EMPTY;
        }
        if (drone.getAccessory() == null) {
            return ItemStack.EMPTY;
        }
        var stack = getStack(slot);
        drone.getEntity().setAccessory(null);
        return stack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        if(slot != 0) {
            return ItemStack.EMPTY;
        }
        var stack = getStack(slot);
        drone.getEntity().setAccessory(null);
        return stack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if(slot == 0) {
            System.out.println(stack.getItem().getClass());
            if(stack.getItem() instanceof AccessoryItem item) {
                System.out.println("i'll set the accessory (" + item.createAccessory() + ")");
                drone.getEntity().setAccessory(item.createAccessory());
            } else {
                drone.getEntity().setAccessory(null);
            }
        }
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        drone.getEntity().setAccessory(null);
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return slot == 0 && stack.getItem() instanceof AccessoryItem;
    }



}
