package net.apiocraft.acdrones.core;

import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.nbt.NbtCompound;

public interface IDroneAccessory extends IPeripheral {
    void update();

    NbtCompound toNbt();
    void fromNbt(NbtCompound nbt);

    DroneAccessoryType<?> getAccessoryType();

    // TODO: better name because this is retarded
    DroneAccessoryAttachment getAttachmentType();

    void setDrone(IDroneAccess drone);


}
