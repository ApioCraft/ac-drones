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

package net.apiocraft.acdrones.accessories.droneClaw.commands;

import net.apiocraft.acdrones.DroneCommand;
import net.apiocraft.acdrones.DroneCommandResult;
import net.apiocraft.acdrones.core.IDroneAccess;
import net.apiocraft.acdrones.accessories.droneClaw.DroneClawAccessory;

import java.util.concurrent.CompletableFuture;

public class ClawGrabCommand implements DroneCommand {
    IDroneAccess drone;
    DroneClawAccessory accessory;
    public ClawGrabCommand(IDroneAccess drone, DroneClawAccessory accessory) {
        this.drone = drone;
        this.accessory = accessory;
    }
    @Override
    public CompletableFuture<DroneCommandResult> execute(IDroneAccess drone) {
        return null;
    }
}
