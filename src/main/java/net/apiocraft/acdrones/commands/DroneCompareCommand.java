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

package net.apiocraft.acdrones.commands;

import net.apiocraft.acdrones.DroneCommand;
import net.apiocraft.acdrones.DroneCommandResult;
import net.apiocraft.acdrones.core.IDroneAccess;
import net.minecraft.item.ItemStack;

import java.util.concurrent.CompletableFuture;

public class DroneCompareCommand implements DroneCommand {
    private final int slot;

    public DroneCompareCommand(int slot) {
        this.slot = slot;
    }
    @Override
    public CompletableFuture<DroneCommandResult> execute(IDroneAccess drone) {
        var selectedStack = drone.getInventory().getStack(drone.getSelectedSlot());
        var comparingStack = drone.getInventory().getStack(slot-1);
        return ItemStack.areItemsAndComponentsEqual(selectedStack, comparingStack) ? CompletableFuture.completedFuture(DroneCommandResult.success()) : CompletableFuture.completedFuture(DroneCommandResult.failure());
    }
}
