package net.apiocraft.acdrones;


import net.apiocraft.acdrones.core.IDroneAccessory;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class DroneNetworkingConstants {
    public static final TrackedDataHandler<Optional<IDroneAccessory>> DRONE_ACCESSORY_HANDLER = new TrackedDataHandler<Optional<IDroneAccessory>>() {
        @Override
        public void write(PacketByteBuf buf, Optional<IDroneAccessory> value) {
            buf.writeOptional(value, AccessoryIO.WRITER);   // uses PacketWriter<T>
        }

        @Override
        public Optional<IDroneAccessory> read(PacketByteBuf buf) {
            return buf.readOptional(AccessoryIO.READER);     // uses PacketReader<T>
        }

        @Override
        public Optional<IDroneAccessory> copy(Optional<IDroneAccessory> value) {
            // If your accessories are immutable, returning the same Optional is fine.
            // If they carry mutable state, deep-copy here.
            return value;
        }
    };
}
