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

import dan200.computercraft.api.detail.BlockReference;
import dan200.computercraft.api.detail.VanillaDetailRegistries;
import net.apiocraft.acdrones.DroneCommand;
import net.apiocraft.acdrones.DroneCommandResult;
import net.apiocraft.acdrones.core.IDroneAccess;

import java.util.concurrent.CompletableFuture;

public class DroneInspectBlockCommand implements DroneCommand {

    @Override
    public CompletableFuture<DroneCommandResult> execute(IDroneAccess drone) {
        if(drone.getLevel().getBlockState(drone.getEntity().getBlockPos().down()).isAir()) {
            return CompletableFuture.completedFuture(DroneCommandResult.failure("No block to look at"));
        }
        // get block below drone
        var table = VanillaDetailRegistries.BLOCK_IN_WORLD.getDetails(new BlockReference(drone.getLevel(), drone.getEntity().getBlockPos().down()));
        return CompletableFuture.completedFuture(DroneCommandResult.success(new Object[] { table }));
    }
}
