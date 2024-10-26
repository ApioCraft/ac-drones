package net.apiocraft.acdrones.registries;

import net.apiocraft.acdrones.Acdrones;
import net.apiocraft.acdrones.accessories.defaultAccessories.modem.DroneModemAccessory;
import net.apiocraft.acdrones.core.DroneAccessoryType;
import net.apiocraft.acdrones.core.IDroneAccessory;
import net.apiocraft.acdrones.accessories.droneClaw.DroneClawAccessory;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class DroneAccessoryTypes {
    public static final DroneAccessoryType<DroneClawAccessory> DRONE_CLAW = register("drone_claw", new DroneAccessoryType<>(DroneClawAccessory.CODEC));
    public static final DroneAccessoryType<DroneModemAccessory> DRONE_MODEM = register("drone_modem", new DroneAccessoryType<>(DroneModemAccessory.CODEC));

    public static <T extends IDroneAccessory> DroneAccessoryType<T> register(String id, DroneAccessoryType<T> accessoryType) {
        return Registry.register(DroneAccessoryType.REGISTRY, Identifier.of(Acdrones.MOD_ID, id), accessoryType);
    }

    public static final PacketCodec<RegistryByteBuf, IDroneAccessory> CODEC = PacketCodecs.registryValue(DroneAccessoryType.REGISTRY.getKey())
            .dispatch(IDroneAccessory::getAccessoryType, DroneAccessoryType::codec);

    public static final PacketCodec<RegistryByteBuf, Optional<IDroneAccessory>> OPTIONAL_CODEC = PacketCodecs.optional(CODEC);

}
