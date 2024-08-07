package net.apiocraft.acdrones.accessories.base;

import net.apiocraft.acdrones.items.AccessoryItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class DroneAccessorySlot extends Slot {
    public DroneAccessorySlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem() instanceof AccessoryItem &&
                inventory.getStack(0).isEmpty();
    }

    @Override
    public int getMaxItemCount() {
        return 1;
    }
}
