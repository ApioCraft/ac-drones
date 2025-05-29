package net.apiocraft.acdrones.items;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.filesystem.Mount;
import dan200.computercraft.api.media.IMedia;
import dan200.computercraft.shared.ModRegistry;
import dan200.computercraft.shared.config.Config;
import dan200.computercraft.shared.util.DataComponentUtil;
import net.apiocraft.acdrones.Acdrones;
import net.apiocraft.acdrones.entities.ComputerDroneEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DroneItem extends Item implements IMedia {
    public DroneItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();

        // System.out.println("DroneItem.useOnBlock");

        if (!world.isClient) {
            System.out.println("make an drone");
            ServerWorld serverWorld = (ServerWorld) world;
            ComputerDroneEntity drone = Acdrones.COMPUTER_DRONE_ENTITY.create(serverWorld);
            if (context.getStack().get(ModRegistry.DataComponents.COMPUTER_ID.get()) != null) {
                drone.setComputerId(context.getStack().get(ModRegistry.DataComponents.COMPUTER_ID.get()).id());
            }
            System.out.println("drone has id too");
            drone.setLabel(DataComponentUtil.getCustomName(context.getStack()));
            drone.refreshPositionAndAngles(context.getHitPos().x, context.getHitPos().y, context.getHitPos().z,
                    drone.getYaw(), drone.getPitch());
            drone.setVelocity(0, 0, 0);
            if (serverWorld.spawnEntity(drone)) {

                context.getStack().decrement(1);
                return ActionResult.SUCCESS;
            } else {
                return ActionResult.FAIL;
            }
        }
        return ActionResult.SUCCESS;
    }

    @Nullable
    @Override
    public String getLabel(RegistryWrapper.WrapperLookup registries, ItemStack stack) {
        return DataComponentUtil.getCustomName(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> list, TooltipType options) {
        if (options.isAdvanced() || !stack.contains(DataComponentTypes.CUSTOM_NAME)) {
            var id = stack.get(ModRegistry.DataComponents.COMPUTER_ID.get());
            if (id != null) {
                list.add(Text.translatable("gui.computercraft.tooltip.computer_id", id.id())
                        .formatted(Formatting.GRAY));
            }
        }
    }

    @Override
    public @javax.annotation.Nullable Mount createDataMount(ItemStack stack, ServerWorld level) {
        var id = stack.get(ModRegistry.DataComponents.COMPUTER_ID.get());
        return id != null
                ? ComputerCraftAPI.createSaveDirMount(level.getServer(), "computer/" + id.id(),
                        Config.computerSpaceLimit)
                : null;
    }
}
