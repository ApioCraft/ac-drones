package net.apiocraft.acdrones.core;

import net.apiocraft.acdrones.Acdrones;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;


public record DroneAccessoryType<T extends IDroneAccessory>(PacketCodec<RegistryByteBuf, T> codec) {
    public static final SimpleRegistry<DroneAccessoryType<?>> REGISTRY = (SimpleRegistry<DroneAccessoryType<?>>)(SimpleRegistry) FabricRegistryBuilder.createSimple(
            RegistryKey.ofRegistry(Identifier.of(Acdrones.MOD_ID, "accessory_types"))
    ).buildAndRegister();
//            ,
//            Lifecycle.stable()

}
