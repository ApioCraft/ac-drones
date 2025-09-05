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

package net.apiocraft.acdrones.inventory;

import net.apiocraft.acdrones.core.IDroneAccess;
import net.apiocraft.acdrones.registries.DroneAccessoryRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Objects;

public class AttachmentInventory implements Inventory {

    private final IDroneAccess drone;

    public AttachmentInventory(IDroneAccess drone) {
        this.drone = drone;
    }

    @Override
    public int size() {
        return drone.getAccessoryAttachments().length;
    }

    @Override
    public boolean isEmpty() {
        return Arrays.stream(drone.getAccessoryAttachments()).allMatch(Objects::isNull);
    }

    @Override
    public ItemStack getStack(int slot) {
        //System.out.println(drone.getAccessory());
        return drone.getAccessoryAttachments()[slot] == null ? ItemStack.EMPTY : DroneAccessoryRegistry.createItemStack(drone.getAccessoryAttachment(slot));
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        if(slot < 0 || slot >= drone.getAccessoryAttachments().length) {
            return ItemStack.EMPTY;
        }
        if (drone.getAccessoryAttachment(slot) == null) {
            return ItemStack.EMPTY;
        }
        var stack = getStack(slot);
        drone.setAccessoryAttachment(slot, null);
        return stack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        if(slot < 0 || slot >= drone.getAccessoryAttachments().length) {
            return ItemStack.EMPTY;
        }
        var stack = getStack(slot);
        drone.setAccessoryAttachment(slot, null);
        return stack;

    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if(slot < 0 || slot >= drone.getAccessoryAttachments().length) {
            return;
        }
        if(DroneAccessoryRegistry.isAccessory(stack)) {
            drone.setAccessoryAttachment(slot, DroneAccessoryRegistry.createAccessory(stack));
        }
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        drone.getEntity().setAccessory(null);
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return slot == 0 && DroneAccessoryRegistry.isAccessory(stack);
    }



}
