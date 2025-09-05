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

package net.apiocraft.acdrones.core;

import dan200.computercraft.api.lua.MethodResult;
import net.apiocraft.acdrones.inventory.AccessoryInventory;
import net.apiocraft.acdrones.DroneCommand;
import net.apiocraft.acdrones.entities.ComputerDroneEntity;
import net.apiocraft.acdrones.inventory.AttachmentInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public interface IDroneAccess {
    Vec3d getPosition();
    ServerWorld getLevel();

    MethodResult executeCommand(DroneCommand command);

    void setBusy(boolean busy);

    boolean isBusy();

    void move(Vec3d targetMovement);

    void setVelocity(Vec3d velocity);

    Vec3d getVelocity();

    int getSelectedSlot();

    Inventory getInventory();

    void setSelectedSlot(int slot);

    ComputerDroneEntity getEntity();

    IDroneAccessory getAccessory();

    AccessoryInventory getAccessoryInventory();

    IDroneAccessory[] getAccessoryAttachments();

    IDroneAccessory getAccessoryAttachment(int index);

    void setAccessoryAttachment(int index, IDroneAccessory accessory);

    AttachmentInventory getAttachmentInventory();
}
