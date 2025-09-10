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

import com.mojang.authlib.GameProfile;
import eu.pb4.common.protection.api.CommonProtection;
import net.apiocraft.acdrones.Acdrones;
import net.apiocraft.acdrones.DroneCommand;
import net.apiocraft.acdrones.DroneCommandResult;
import net.apiocraft.acdrones.core.IDroneAccess;
import net.minecraft.block.BlockState;

import java.util.concurrent.CompletableFuture;

public class DroneMineCommand implements DroneCommand {
    @Override
    public CompletableFuture<DroneCommandResult> execute(IDroneAccess drone) {
        if(!drone.getLevel().isAir(drone.getEntity().getBlockPos())) {
            BlockState block = drone.getLevel().getBlockState(drone.getEntity().getBlockPos());
            if(block.getHardness(drone.getLevel(), drone.getEntity().getBlockPos()) >= 0 && block.getHardness(drone.getLevel(), drone.getEntity().getBlockPos()) <= 1) {
                if(CommonProtection.canBreakBlock(drone.getLevel(), drone.getEntity().getBlockPos(), Acdrones.getServer().getUserCache().getByUuid(drone.getEntity().getOwner()).orElse(null), null)) {
                    drone.getLevel().breakBlock(drone.getEntity().getBlockPos(), true, drone.getEntity());
                    return CompletableFuture.completedFuture(DroneCommandResult.success());
                } else {
                    return CompletableFuture.completedFuture(DroneCommandResult.failure("block is unbreakable"));
                }
            } else {
                return CompletableFuture.completedFuture(DroneCommandResult.failure("block is unbreakable"));
            }
        } else {
            return CompletableFuture.completedFuture(DroneCommandResult.failure("no block to mine"));
        }
    }
}
