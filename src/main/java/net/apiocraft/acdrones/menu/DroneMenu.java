package net.apiocraft.acdrones.menu;


import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.computer.inventory.AbstractComputerMenu;
import dan200.computercraft.shared.container.SingleContainerData;
import dan200.computercraft.shared.network.container.ComputerContainerData;
import net.apiocraft.acdrones.Acdrones;
import net.apiocraft.acdrones.accessories.base.DroneAccessorySlot;
import net.apiocraft.acdrones.core.DroneBrain;
import net.apiocraft.acdrones.entities.ComputerDroneEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.slot.Slot;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class DroneMenu extends AbstractComputerMenu {
    public static final int BORDER = 8;
    public static final int PLAYER_START_Y = 134;
    public static final int DRONE_START_X = SIDEBAR_WIDTH + 175;
    public static final int PLAYER_START_X = SIDEBAR_WIDTH + BORDER;

    private final PropertyDelegate data;

    private DroneMenu(
            int id, Predicate<PlayerEntity> canUse, ComputerFamily family, @Nullable ServerComputer computer, @Nullable ComputerContainerData menuData,
            PlayerInventory playerInventory, Inventory inventory, Inventory droneAccessories, PropertyDelegate data
    ) {
        super(Acdrones.DRONE_MENU, id, canUse, family, computer, menuData);
        this.data = data;
        addProperties(data);

        // Drone inventory
        for (var x = 0; x < 4; x++) {
            addSlot(new Slot(inventory, x, DRONE_START_X + 1 + x * 18, PLAYER_START_Y + 1));
        }

        // Player inventory
        for (var y = 0; y < 3; y++) {
            for (var x = 0; x < 9; x++) {
                addSlot(new Slot(playerInventory, x + y * 9 + 9, PLAYER_START_X + x * 18, PLAYER_START_Y + 1 + y * 18));
            }
        }

        // Player hotbar
        for (var x = 0; x < 9; x++) {
            addSlot(new Slot(playerInventory, x, PLAYER_START_X + x * 18, PLAYER_START_Y + 3 * 18 + 5));
        }

        // Drone accessories
        addSlot(new DroneAccessorySlot(droneAccessories, 0, DRONE_START_X + 1 + (3*18), PLAYER_START_Y + 19 + 18));

    }

    public static DroneMenu ofBrain(int id, PlayerInventory player, DroneBrain drone) {
        return new DroneMenu(
                // Laziness in turtle.getOwner() is important here!
                id, p -> drone.getOwner().canPlayerUse(p), ComputerFamily.ADVANCED, drone.getOwner().createServerComputer(), null,
                player, drone.getInventory(), drone.getAccessoryInventory(), (SingleContainerData) drone::getSelectedSlot
        );
    }

    public static DroneMenu ofMenuData(int id, PlayerInventory player, ComputerContainerData data) {
        return new DroneMenu(
                id, x -> true, data.family(), null, data,
                player, new SimpleInventory(ComputerDroneEntity.INVENTORY_SIZE), new SimpleInventory(1), new ArrayPropertyDelegate(1)
        );
    }

    public int getSelectedSlot() {
        return data.get(0);
    }

    private ItemStack tryItemMerge(PlayerEntity player, int slotNum, int firstSlot, int lastSlot, boolean reverse) {
        var slot = slots.get(slotNum);
        var originalStack = ItemStack.EMPTY;
        if (slot != null && slot.hasStack()) {
            var clickedStack = slot.getStack();
            originalStack = clickedStack.copy();
            if (!insertItem(clickedStack, firstSlot, lastSlot, reverse)) {
                return ItemStack.EMPTY;
            }

            if (clickedStack.isEmpty()) {
                slot.setStackNoCallbacks(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (clickedStack.getCount() != originalStack.getCount()) {
                slot.onTakeItem(player, clickedStack);
            } else {
                return ItemStack.EMPTY;
            }
        }
        return originalStack;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotNum) {
        if (slotNum >= 0 && slotNum < 4) {
            return tryItemMerge(player, slotNum, 4, 40, true);
        } else if (slotNum >= 16) {
            return tryItemMerge(player, slotNum, 0, 4, false);
        }
        return ItemStack.EMPTY;
    }
}
