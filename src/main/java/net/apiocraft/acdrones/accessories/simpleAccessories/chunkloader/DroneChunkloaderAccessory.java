package net.apiocraft.acdrones.accessories.simpleAccessories.chunkloader;

import dan200.computercraft.api.peripheral.IPeripheral;
import net.apiocraft.acdrones.accessories.droneClaw.DroneClawAccessory;
import net.apiocraft.acdrones.core.DroneAccessoryAttachment;
import net.apiocraft.acdrones.core.DroneAccessoryType;
import net.apiocraft.acdrones.core.IDroneAccess;
import net.apiocraft.acdrones.core.IDroneAccessory;
import net.apiocraft.acdrones.registries.DroneAccessoryTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import org.jetbrains.annotations.Nullable;

public class DroneChunkloaderAccessory implements IDroneAccessory {

    public static final PacketCodec<RegistryByteBuf, DroneChunkloaderAccessory> CODEC = PacketCodec.of(
            ((value, buf) -> {
                //buf.writeNbt(value.toNbt());
            }),
            (buf -> {
                return new DroneChunkloaderAccessory();
            })
    );

    private IDroneAccess drone;

    @Override
    public void update() {

    }

    @Override
    public NbtCompound toNbt() {
        return new NbtCompound();
    }

    @Override
    public void fromNbt(NbtCompound nbt) {


    }

    @Override
    public DroneAccessoryType<?> getAccessoryType() {
        return DroneAccessoryTypes.DRONE_CHUNKLOADER;
    }

    @Override
    public DroneAccessoryAttachment getAttachmentType() {
        return DroneAccessoryAttachment.INTERNAL;
    }

    @Override
    public void setDrone(IDroneAccess drone) {
        this.drone = drone;
    }

    @Override
    public String getType() {
        return "chunkloader";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof DroneChunkloaderAccessory;
    }
}
