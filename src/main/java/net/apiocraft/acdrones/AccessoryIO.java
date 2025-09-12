package net.apiocraft.acdrones;

import net.apiocraft.acdrones.core.DroneAccessoryType;
import net.apiocraft.acdrones.core.IDroneAccessory;
import net.minecraft.network.PacketByteBuf;

import java.util.Optional;

public final class AccessoryIO {
    private AccessoryIO() {}

    public static final PacketByteBuf.PacketWriter<IDroneAccessory> WRITER = (buf, value) -> {
        @SuppressWarnings("unchecked")
        DroneAccessoryType<IDroneAccessory> type = (DroneAccessoryType<IDroneAccessory>) value.getAccessoryType();
        buf.writeRegistryValue(DroneAccessoryType.REGISTRY, type); // <— helper exists in 1.20.1
        type.write(buf, value);
    };

    public static final PacketByteBuf.PacketReader<IDroneAccessory> READER = buf -> {
        DroneAccessoryType<?> rawType = buf.readRegistryValue(DroneAccessoryType.REGISTRY); // <—
        return readDynamic(rawType, buf);
    };

    @SuppressWarnings("unchecked")
    private static <T extends IDroneAccessory> T readDynamic(DroneAccessoryType<?> raw, PacketByteBuf buf) {
        return ((DroneAccessoryType<T>) raw).read(buf);
    };

    public static void writeOptional(PacketByteBuf buf, Optional<IDroneAccessory> opt) {
        buf.writeOptional(opt, WRITER);
    }
    public static Optional<IDroneAccessory> readOptional(PacketByteBuf buf) {
        return buf.readOptional(READER);
    }
}
