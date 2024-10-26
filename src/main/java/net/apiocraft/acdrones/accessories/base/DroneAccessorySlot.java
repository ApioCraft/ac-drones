package net.apiocraft.acdrones.accessories.base;

import net.apiocraft.acdrones.core.DroneAccessoryAttachment;
import net.apiocraft.acdrones.registries.DroneAccessoryRegistry;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class DroneAccessorySlot extends Slot {
    DroneAccessoryAttachment attachmentType;
    public DroneAccessorySlot(Inventory inventory, int index, int x, int y, DroneAccessoryAttachment attachmentType) {
        super(inventory, index, x, y);
        this.attachmentType = attachmentType;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return DroneAccessoryRegistry.isAccessory(stack) &&
                inventory.getStack(0).isEmpty() &&
                DroneAccessoryRegistry.createAccessory(stack).getAttachmentType() == attachmentType;
    }

    @Override
    public int getMaxItemCount() {
        return 1;
    }
}
