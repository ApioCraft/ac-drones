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

package net.apiocraft.acdrones.client;

import net.apiocraft.acdrones.Acdrones;
import net.apiocraft.acdrones.core.IDroneAccessory;
import net.apiocraft.acdrones.accessories.droneClaw.DroneClawAccessory;
import net.apiocraft.acdrones.client.renderer.ComputerDroneEntityRenderer;
import net.apiocraft.acdrones.client.renderer.DroneClawAccessoryRenderer;
import net.apiocraft.acdrones.client.screen.DroneScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

import java.util.HashMap;

public class AcdronesClient implements ClientModInitializer {

    public static HashMap<Class<? extends IDroneAccessory>, IAccessoryRenderer> accessoryRenderers = new HashMap<>();
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Acdrones.COMPUTER_DRONE_ENTITY, ComputerDroneEntityRenderer::new);
        HandledScreens.register(Acdrones.DRONE_MENU, DroneScreen::new);
        accessoryRenderers.put(DroneClawAccessory.class, new DroneClawAccessoryRenderer());
    }
}
