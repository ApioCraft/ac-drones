package net.apiocraft.acdrones;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.component.ComputerComponent;
import dan200.computercraft.shared.network.container.ComputerContainerData;
import dan200.computercraft.shared.network.container.ContainerData;
import net.apiocraft.acdrones.apis.DroneAPI;
import net.apiocraft.acdrones.core.IDroneAccess;
import net.apiocraft.acdrones.core.IDroneAccessory;
import net.apiocraft.acdrones.entities.ComputerDroneEntity;
import net.apiocraft.acdrones.items.DroneItem;
import net.apiocraft.acdrones.menu.DroneMenu;
import net.apiocraft.acdrones.registries.DroneAccessoryRegistry;
import net.apiocraft.acdrones.registries.DroneAccessoryTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import java.util.Optional;

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

    public static final Item DRONE_ACCESSORY_CLAW_ITEM = registerItem(new Item(new Item.Settings()), "drone_claw");

    public static final Item COMPUTER_DRONE_ITEM = registerItem(new DroneItem(new Item.Settings().maxCount(1)), "computer_drone");

    public static final ComputerComponent<IDroneAccess> DRONE = ComputerComponent.create(MOD_ID, "drone");

    public static final TrackedDataHandler<Optional<IDroneAccessory>> DRONE_ACCESSORY_HANDLER = TrackedDataHandler.create(DroneAccessoryTypes.OPTIONAL_CODEC);

    @Override
    public void onInitialize() {
        DroneAccessoryRegistry.initialize();
        ComputerCraftAPI.registerAPIFactory((computer) -> {
            var drone = computer.getComponent(DRONE);
            return drone != null ? new DroneAPI(drone) : null;
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
                .register((itemGroup) -> itemGroup.add(DRONE_ACCESSORY_CLAW_ITEM));

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL)
                .register((itemGroup) -> itemGroup.add(COMPUTER_DRONE_ITEM));



        TrackedDataHandlerRegistry.register(DRONE_ACCESSORY_HANDLER);

        System.out.println(DroneAccessoryRegistry.DRONE_ACCESSORIES.getId(DroneAccessoryRegistry.DRONE_ACCESSORY_CLAW));


    }

    public static Item registerItem(Item item, String id) {
        // Create the identifier for the item.
        Identifier itemID = Identifier.of(MOD_ID, id);

        // Register the item.
        Item registeredItem = Registry.register(Registries.ITEM, itemID, item);

        // Return the registered item!
        return registeredItem;
    }
}
