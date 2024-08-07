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
