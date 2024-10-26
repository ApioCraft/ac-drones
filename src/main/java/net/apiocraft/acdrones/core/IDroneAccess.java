package net.apiocraft.acdrones.core;

import dan200.computercraft.api.lua.MethodResult;
import net.apiocraft.acdrones.inventory.AccessoryInventory;
import net.apiocraft.acdrones.DroneCommand;
import net.apiocraft.acdrones.entities.ComputerDroneEntity;
import net.apiocraft.acdrones.inventory.AttachmentInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public interface IDroneAccess {
    Vec3d getPosition();
    ServerWorld getLevel();

    MethodResult executeCommand(DroneCommand command);

    void setBusy(boolean busy);

    boolean isBusy();

    void move(Vec3d targetMovement);

    void setVelocity(Vec3d velocity);

    Vec3d getVelocity();

    int getSelectedSlot();

    Inventory getInventory();

    void setSelectedSlot(int slot);

    ComputerDroneEntity getEntity();

    IDroneAccessory getAccessory();

    AccessoryInventory getAccessoryInventory();

    IDroneAccessory[] getAccessoryAttachments();

    IDroneAccessory getAccessoryAttachment(int index);

    void setAccessoryAttachment(int index, IDroneAccessory accessory);

    AttachmentInventory getAttachmentInventory();
}
