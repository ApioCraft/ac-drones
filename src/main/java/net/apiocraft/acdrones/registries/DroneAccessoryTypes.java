/*
 * Copyright (c) 2025 qrmcat
 *
 * This file is part of ac-drones.
 *
 * ac-drones is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * ac-drones is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ac-drones; if not, see <https://www.gnu.org/licenses/>.
 *
 */

package net.apiocraft.acdrones.registries;

import net.apiocraft.acdrones.Acdrones;
import net.apiocraft.acdrones.accessories.defaultAccessories.modem.DroneModemAccessory;
import net.apiocraft.acdrones.accessories.simpleAccessories.chunkloader.DroneChunkloaderAccessory;
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
    public static final DroneAccessoryType<DroneChunkloaderAccessory> DRONE_CHUNKLOADER = register("drone_chunkloader", new DroneAccessoryType<>(DroneChunkloaderAccessory.CODEC));

    public static <T extends IDroneAccessory> DroneAccessoryType<T> register(String id, DroneAccessoryType<T> accessoryType) {
        return Registry.register(DroneAccessoryType.REGISTRY, Identifier.of(Acdrones.MOD_ID, id), accessoryType);
    }

    public static final PacketCodec<RegistryByteBuf, IDroneAccessory> CODEC = PacketCodecs.registryValue(DroneAccessoryType.REGISTRY.getKey())
            .dispatch(IDroneAccessory::getAccessoryType, DroneAccessoryType::codec);

    public static final PacketCodec<RegistryByteBuf, Optional<IDroneAccessory>> OPTIONAL_CODEC = PacketCodecs.optional(CODEC);

    public static void initialize() {

    }

}
