package net.apiocraft.acdrones.items;

import net.apiocraft.acdrones.core.IDroneAccessory;
import net.apiocraft.acdrones.registries.DroneAccessoryRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class AccessoryItem extends Item {
    private final Identifier accessoryId;
    public AccessoryItem(Identifier accessoryId, Settings settings) {
        super(settings);
        this.accessoryId = accessoryId;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    public static AccessoryItem create(Identifier accessoryId) {
        return new AccessoryItem(accessoryId, new Item.Settings());
    }

    public IDroneAccessory createAccessory() {
        System.out.println(accessoryId);
        return DroneAccessoryRegistry.createAccessory(accessoryId);
    }

    public Identifier getAccessoryId() {
        return accessoryId;
    }
}
