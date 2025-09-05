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

public class AccessoryInventory implements Inventory {

    private final IDroneAccess drone;

    public AccessoryInventory(IDroneAccess drone) {
        this.drone = drone;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return drone.getAccessory() == null;
    }

    @Override
    public ItemStack getStack(int slot) {
        //System.out.println(drone.getAccessory());
        return drone.getAccessory() == null ? ItemStack.EMPTY : DroneAccessoryRegistry.createItemStack(drone.getAccessory());
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        if(slot != 0) {
            return ItemStack.EMPTY;
        }
        if (drone.getAccessory() == null) {
            return ItemStack.EMPTY;
        }
        var stack = getStack(slot);
        drone.getEntity().setAccessory(null);
        return stack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        if(slot != 0) {
            return ItemStack.EMPTY;
        }
        var stack = getStack(slot);
        drone.getEntity().setAccessory(null);
        return stack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if(slot == 0) {
            System.out.println(stack.getItem().getClass());
            if(DroneAccessoryRegistry.isAccessory(stack)) {
                drone.getEntity().setAccessory(DroneAccessoryRegistry.createAccessory(stack));
            } else {
                drone.getEntity().setAccessory(null);
            }
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
