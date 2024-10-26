package net.apiocraft.acdrones.accessories.defaultAccessories.modem;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.peripheral.modem.ModemState;
import dan200.computercraft.shared.peripheral.modem.wireless.WirelessModemPeripheral;
import net.apiocraft.acdrones.core.DroneAccessoryAttachment;
import net.apiocraft.acdrones.core.DroneAccessoryType;
import net.apiocraft.acdrones.core.IDroneAccess;
import net.apiocraft.acdrones.core.IDroneAccessory;
import net.apiocraft.acdrones.registries.DroneAccessoryTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DroneModemAccessory extends WirelessModemPeripheral implements IDroneAccessory {

    public static PacketCodec<RegistryByteBuf, DroneModemAccessory> CODEC = PacketCodec.of(
            ((value, buf) -> {
                buf.writeNbt(value.toNbt());
            }),
            (buf -> {
                DroneModemAccessory accessory = new DroneModemAccessory();
                accessory.fromNbt(buf.readNbt());
                return accessory;
            })
    );

    private IDroneAccess drone;

    boolean isAdvanced = false;

    public DroneModemAccessory() {
        super(new ModemState(), false);
    }

    public DroneModemAccessory(boolean isAdvanced) {
        super(new ModemState(), false);
        this.isAdvanced = isAdvanced;
    }

    @Override
    public void update() {

    }

    @Override
    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putBoolean("isAdvanced", isAdvanced);
        return nbt;
    }

    @Override
    public void fromNbt(NbtCompound nbt) {
        isAdvanced = nbt.getBoolean("isAdvanced");
    }

    @Override
    public DroneAccessoryType<?> getAccessoryType() {
        return DroneAccessoryTypes.DRONE_MODEM;
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
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof DroneModemAccessory;
    }

    @Override
    public World getLevel() {
        return drone.getLevel();
    }

    @Override
    public Vec3d getPosition() {
        return drone.getPosition();
    }
}
