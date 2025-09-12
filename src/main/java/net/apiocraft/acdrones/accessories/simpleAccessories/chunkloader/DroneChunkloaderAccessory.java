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

package net.apiocraft.acdrones.accessories.simpleAccessories.chunkloader;

import dan200.computercraft.api.peripheral.IPeripheral;
import net.apiocraft.acdrones.accessories.droneClaw.DroneClawAccessory;
import net.apiocraft.acdrones.core.DroneAccessoryAttachment;
import net.apiocraft.acdrones.core.DroneAccessoryType;
import net.apiocraft.acdrones.core.IDroneAccess;
import net.apiocraft.acdrones.core.IDroneAccessory;
import net.apiocraft.acdrones.registries.DroneAccessoryTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.Nullable;

public class DroneChunkloaderAccessory implements IDroneAccessory {

    public static DroneChunkloaderAccessory read(PacketByteBuf buf) {
        DroneChunkloaderAccessory accessory = new DroneChunkloaderAccessory();
        accessory.fromNbt(buf.readNbt());
        return accessory;
    }

    public static void write(PacketByteBuf buf, DroneChunkloaderAccessory accessory) {
        buf.writeNbt(accessory.toNbt());
    }

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
