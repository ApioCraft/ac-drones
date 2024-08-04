package net.apiocraft.acdrones;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.component.ComputerComponent;
import dan200.computercraft.shared.network.container.ComputerContainerData;
import dan200.computercraft.shared.network.container.ContainerData;
import net.apiocraft.acdrones.apis.DroneAPI;
import net.apiocraft.acdrones.entities.ComputerDroneEntity;
import net.apiocraft.acdrones.menu.DroneMenu;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class Acdrones implements ModInitializer {

    public static final String MOD_ID = "acdrones";

    public static final EntityType<ComputerDroneEntity> COMPUTER_DRONE_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(MOD_ID, "computer_drone"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ComputerDroneEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.25f)).build()
    );

    public static final ScreenHandlerType<DroneMenu> DRONE_MENU = Registry.register(
            Registries.SCREEN_HANDLER,
            Identifier.of(MOD_ID, "drone_menu"),
            ContainerData.toType(ComputerContainerData.STREAM_CODEC, DroneMenu::ofMenuData)
    );

    public static final ComputerComponent<IDroneAccess> DRONE = ComputerComponent.create(MOD_ID, "drone");

    @Override
    public void onInitialize() {
        ComputerCraftAPI.registerAPIFactory((computer) -> {
            var drone = computer.getComponent(DRONE);
            return drone != null ? new DroneAPI(drone) : null;
        });
    }
}
