package net.apiocraft.acdrones.core;

import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.nbt.NbtCompound;

public interface IDroneAccessory extends IPeripheral {
    void update();

    NbtCompound toNbt();
    void fromNbt(NbtCompound nbt);

    DroneAccessoryType<?> getAccessoryType();

    void setDrone(IDroneAccess drone);


}
