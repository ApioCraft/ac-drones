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

package net.apiocraft.acdrones;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.component.ComputerComponent;
import dan200.computercraft.shared.ModRegistry;
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
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class Acdrones implements ModInitializer {

    public static final String MOD_ID = "acdrones";

    public static final EntityType<ComputerDroneEntity> COMPUTER_DRONE_ENTITY = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "computer_drone"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ComputerDroneEntity::new)
                    .dimensions(EntityDimensions.fixed(0.75f, 0.25f)).build());

    public static final ScreenHandlerType<DroneMenu> DRONE_MENU = Registry.register(Registries.SCREEN_HANDLER,
            Identifier.of(MOD_ID, "drone_menu"),
            ContainerData.toType(ComputerContainerData::new, DroneMenu::ofMenuData));

    public static final Item DRONE_ACCESSORY_CLAW_ITEM = registerItem(new Item(new Item.Settings()), "drone_claw");

    public static final Item DRONE_ACCESSORY_CHUNKLOADER_ITEM = registerItem(new Item(new Item.Settings()),
            "drone_chunkloader");

    public static final Item COMPUTER_DRONE_ITEM = registerItem(new DroneItem(new Item.Settings().maxCount(1)),
            "computer_drone");

    public static final Item DRONE_BODY_ITEM = registerItem(new Item(new Item.Settings()), "drone_body");
    public static final Item DRONE_PROPELLER_ITEM = registerItem(new Item(new Item.Settings()), "drone_propeller");

    public static final ComputerComponent<IDroneAccess> DRONE = ComputerComponent.create(MOD_ID, "drone");

    private static MinecraftServer server;

    public static void addToCCTab(Item item) {
        // Scan all item-group ids and pick the ones owned by CC:T ("computercraft")
        for (Identifier id : Registries.ITEM_GROUP.getIds()) {
            if ("computercraft".equals(id.getNamespace())) {
                RegistryKey<ItemGroup> key =
                        RegistryKey.of(RegistryKeys.ITEM_GROUP, id);

                ItemGroupEvents.modifyEntriesEvent(key).register(entries -> {
                    // You can also do entries.addAfter(...), addBefore(...), etc.
                    entries.add(item);
                });
            }
        }
    }

    @Override
    public void onInitialize() {
        // DroneAccessoryRegistry.initialize();

        /*ItemGroupEvents.modifyEntriesEvent(ModRegistry.Items)
                .register((itemGroup) -> {
                    itemGroup.add(DRONE_ACCESSORY_CLAW_ITEM);
                    itemGroup.add(DRONE_ACCESSORY_CHUNKLOADER_ITEM);
                });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL)
                .register((itemGroup) -> itemGroup.add(COMPUTER_DRONE_ITEM));*/

        RegistryKey<ItemGroup> ccTab = RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of("computercraft", "tab"));
        ItemGroupEvents.modifyEntriesEvent(ccTab).register(entries -> {
            entries.add(DRONE_ACCESSORY_CLAW_ITEM);
            // TODO: texture
            entries.add(DRONE_ACCESSORY_CHUNKLOADER_ITEM);
            entries.add(COMPUTER_DRONE_ITEM);
            // TODO: properly give a use to these and make them properly craftable
            //entries.add(DRONE_BODY_ITEM);
            //entries.add(DRONE_PROPELLER_ITEM);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(DRONE_BODY_ITEM);
            entries.add(DRONE_PROPELLER_ITEM);
        });

        TrackedDataHandlerRegistry.register(DroneNetworkingConstants.DRONE_ACCESSORY_HANDLER);

        // not the good even for it but if it works it works:tm:
        DroneAccessoryRegistry.initialize();
        ComputerCraftAPI.registerAPIFactory((computer) -> {
            var drone = computer.getComponent(DRONE);
            System.out.println("Do api drone?");
            System.out.println(DRONE);
            return drone != null ? new DroneAPI(drone) : null;
        });

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            Acdrones.server = server;
        });


//        System.out.println(DroneAccessoryRegistry.DRONE_ACCESSORIES.getId(DroneAccessoryRegistry.DRONE_ACCESSORY_CLAW));
    }

    public static MinecraftServer getServer() {
        return server;
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
