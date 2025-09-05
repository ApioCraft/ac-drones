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

import dan200.computercraft.shared.platform.ContainerTransfer;
import dan200.computercraft.shared.platform.PlatformHelper;
import dan200.computercraft.shared.util.InventoryUtil;
import net.apiocraft.acdrones.DroneCommand;
import net.apiocraft.acdrones.DroneCommandResult;
import net.apiocraft.acdrones.core.IDroneAccess;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldEvents;

import java.util.concurrent.CompletableFuture;

public class DroneSuckCommand implements DroneCommand {
    private final int quantity;

    public DroneSuckCommand(int quantity) {
        this.quantity = quantity;
    }

    private static ContainerTransfer getOffsetInventory(IDroneAccess drone) {
        return PlatformHelper.get().wrapContainer(drone.getInventory()).rotate(drone.getSelectedSlot());
    }

    @Override
    public CompletableFuture<DroneCommandResult> execute(IDroneAccess drone) {
        // Sucking nothing is easy
        if (quantity == 0) {
            return CompletableFuture.completedFuture(DroneCommandResult.success());
        }

        var direction = Direction.DOWN;

        // Get inventory for thing in front
        var world = drone.getLevel();
        var dronePosition = drone.getPosition();
        var blockPosition = drone.getEntity().getBlockPos().offset(direction);
        var side = direction.getOpposite();

        var inventory = PlatformHelper.get().getContainer(world, blockPosition, side);

        if (inventory != null) {
            // Take from inventory of thing in front
            var transferred = inventory.moveTo(getOffsetInventory(drone), quantity);
            switch (transferred) {
                case ContainerTransfer.NO_SPACE:
                    return CompletableFuture.completedFuture(DroneCommandResult.failure("No space for items"));
                case ContainerTransfer.NO_ITEMS:
                    return CompletableFuture.completedFuture(DroneCommandResult.failure("No items to take"));
                default:
                    return CompletableFuture.completedFuture(DroneCommandResult.success());
            }
        } else {
            // Suck up loose items off the ground
            var aabb = new Box(
                    blockPosition.getX(), blockPosition.getY(), blockPosition.getZ(),
                    blockPosition.getX() + 1.0, blockPosition.getY() + 1.0, blockPosition.getZ() + 1.0
            );
            var list = world.getEntitiesByClass(ItemEntity.class, aabb, EntityPredicates.VALID_ENTITY);
            if (list.isEmpty()) return CompletableFuture.completedFuture(DroneCommandResult.failure("No items to take"));

            for (var entity : list) {
                // Suck up the item
                var stack = entity.getStack().copy();

                ItemStack storeStack;
                ItemStack leaveStack;
                if (stack.getCount() > quantity) {
                    storeStack = stack.split(quantity);
                    leaveStack = stack;
                } else {
                    storeStack = stack;
                    leaveStack = ItemStack.EMPTY;
                }

                var oldCount = storeStack.getCount();
                var remainder = InventoryUtil.storeItemsFromOffset(drone.getInventory(), storeStack, drone.getSelectedSlot());

                if (remainder.getCount() != oldCount) {
                    if (remainder.isEmpty() && leaveStack.isEmpty()) {
                        entity.discard();
                    } else if (remainder.isEmpty()) {
                        entity.setStack(leaveStack);
                    } else if (leaveStack.isEmpty()) {
                        entity.setStack(remainder);
                    } else {
                        leaveStack.increment(remainder.getCount());
                        entity.setStack(leaveStack);
                    }

                    // Play fx
                    world.syncGlobalEvent(WorldEvents.DISPENSER_DISPENSES, drone.getEntity().getBlockPos(), 0);
                    return CompletableFuture.completedFuture(DroneCommandResult.success());
                }
            }


            return CompletableFuture.completedFuture(DroneCommandResult.failure("No space for items"));
        }
    }
}
