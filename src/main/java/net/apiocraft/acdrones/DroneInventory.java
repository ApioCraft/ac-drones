package net.apiocraft.acdrones;

import dan200.computercraft.shared.container.BasicContainer;
import net.apiocraft.acdrones.entities.ComputerDroneEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;

public class DroneInventory implements BasicContainer {

    private final ComputerDroneEntity drone;

    public DroneInventory(ComputerDroneEntity drone) {
        this.drone = drone;
    }

    DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    public NbtList writeNbt(NbtList nbtList) {
        int i;
        NbtCompound nbtCompound;
        for(i = 0; i < this.inventory.size(); ++i) {
            if (!(this.inventory.get(i)).isEmpty()) {
                nbtCompound = new NbtCompound();
                nbtCompound.putByte("Slot", (byte)i);
                nbtList.add((this.inventory.get(i)).encode(this.drone.getRegistryManager(), nbtCompound));
            }
        }

        return nbtList;
    }

    public void readNbt(NbtList nbtList) {
        this.clear();

        for(int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            int j = nbtCompound.getByte("Slot") & 255;
            ItemStack itemStack = (ItemStack)ItemStack.fromNbt(this.drone.getRegistryManager(), nbtCompound).orElse(ItemStack.EMPTY);
            this.inventory.set(j, itemStack);
        }

    }

}
