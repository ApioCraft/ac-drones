package net.apiocraft.acdrones.registries;

import net.apiocraft.acdrones.Acdrones;
import net.apiocraft.acdrones.core.IDroneAccessory;
import net.apiocraft.acdrones.accessories.droneClaw.DroneClawAccessory;
import net.apiocraft.acdrones.accessories.factory.DroneAccessoryFactory;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.function.Supplier;

public class DroneAccessoryRegistry {
    public static final Registry<DroneAccessoryFactory> DRONE_ACCESSORIES = FabricRegistryBuilder
            .createSimple(DroneAccessoryFactory.class, Identifier.of(Acdrones.MOD_ID, "accessories"))
            .buildAndRegister();

    public static final HashMap<DroneAccessoryFactory, Item> ACCESSORY_ITEM_LOOKUP = new HashMap<>();

    private static final HashMap<Class<? extends IDroneAccessory>, DroneAccessoryFactory> ACCESSORY_TYPE_TO_FACTORY = new HashMap<>();


    public static DroneAccessoryFactory registerAccessory(String name, Supplier<IDroneAccessory> supplier, Item item) {
        DroneAccessoryFactory factory = new DroneAccessoryFactory(supplier);
        Registry.register(DRONE_ACCESSORIES, Identifier.of(Acdrones.MOD_ID, name), factory);
        ACCESSORY_ITEM_LOOKUP.put(factory, item);
        ACCESSORY_TYPE_TO_FACTORY.put(factory.create().getClass(), factory);
        return factory;
    }

    public static IDroneAccessory createAccessory(Identifier id) {
        DroneAccessoryFactory factory = DRONE_ACCESSORIES.get(id);
        if (factory != null) {
            return factory.create();
        }
        return null;
    }

    public static IDroneAccessory createFromNbt(NbtCompound nbt) {
        Identifier id = Identifier.tryParse(nbt.getString("id"));
        IDroneAccessory accessory = createAccessory(id);
        if(accessory != null) {
            accessory.fromNbt(nbt);
        }
        return accessory;
    }

    public static IDroneAccessory createAccessoryFromNbt(NbtCompound nbt) {
        Identifier id = Identifier.tryParse(nbt.getString("id"));
        IDroneAccessory accessory = createAccessory(id);
        if (accessory != null) {
            accessory.fromNbt(nbt);
        }
        return accessory;
    }

    public static ItemStack createItemStack(IDroneAccessory accessory) {
        Item item = ACCESSORY_ITEM_LOOKUP.get(ACCESSORY_TYPE_TO_FACTORY.get(accessory.getClass()));
        if (item != null) {
            return new ItemStack(item);
        }
        return ItemStack.EMPTY;
    }

    public static IDroneAccessory createAccessory(ItemStack stack) {
        Item item = stack.getItem();
        for (DroneAccessoryFactory accessory : ACCESSORY_ITEM_LOOKUP.keySet()) {
            if (ACCESSORY_ITEM_LOOKUP.get(accessory) == item) {
                return accessory.create();
            }
        }
        return null;
    }


    public static Identifier getId(IDroneAccessory accessory) {
        DroneAccessoryFactory factory = ACCESSORY_TYPE_TO_FACTORY.get(accessory.getClass());
        if (factory != null) {
            return DRONE_ACCESSORIES.getId(factory);
        }
        return null;
    }

    public static void initialize() {
        // just here so the class is loaded
    }

    public static final DroneAccessoryFactory DRONE_ACCESSORY_CLAW = registerAccessory("claw", DroneClawAccessory::new, Acdrones.DRONE_ACCESSORY_CLAW_ITEM);
}