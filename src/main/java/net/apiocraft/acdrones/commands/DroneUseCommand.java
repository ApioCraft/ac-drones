package net.apiocraft.acdrones.commands;

import net.apiocraft.acdrones.DroneCommand;
import net.apiocraft.acdrones.DroneCommandResult;
import net.apiocraft.acdrones.core.IDroneAccess;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.CompletableFuture;

public class DroneUseCommand implements DroneCommand {
    private final boolean canUse;

    public DroneUseCommand(boolean canUse) {
        this.canUse = canUse;
    }


    public static BlockHitResult getHitResult(BlockPos position, Direction side) {
        var hitX = 0.5 + side.getOffsetX() * 0.5;
        var hitY = 0.5 + side.getOffsetY() * 0.5;
        var hitZ = 0.5 + side.getOffsetZ() * 0.5;
        if (Math.abs(hitY - 0.5) < 0.01) hitY = 0.45;

        return new BlockHitResult(new Vec3d(position.getX() + hitX, position.getY() + hitY, position.getZ() + hitZ), side, position, false);
    }

    private boolean isBlockProtected(ServerWorld world, BlockPos position, FakePlayer player) {
        return world.getServer().isSpawnProtected(world, position, player);
    }

    private boolean letMePlace(ItemPlacementContext context, IDroneAccess drone, FakePlayer player, BlockPos position, Direction side, boolean allowReplacing) {
        var world = drone.getLevel();
        // first we check if we can even build there
        if(!world.isInBuildLimit(position)) return false;
        if(world.isAir(position)) return false;
        // we don't want to allow just replacing wotah.. this technically shouldn't even be able to happen but eh
        if(context.getStack().getItem() instanceof BlockItem && world.getBlockState(position).isLiquid()) return false;

        // is there an entity blocking the placement?
        var entities = world.getEntitiesByClass(Entity.class, new Box(position), (entity) -> true);
        if(!entities.isEmpty()) return false;

        var blockState = context.getWorld().getBlockState(position);
        var replaceable = blockState.canReplace(context);
        if (!allowReplacing && replaceable) return false;

        // Check spawn protection
        var isProtected = replaceable
                ? isBlockProtected(world, position, player) // we replace
                : isBlockProtected(world, position.offset(side), player); // we put down block
        return !isProtected;
    }

    @Override
    public CompletableFuture<DroneCommandResult> execute(IDroneAccess drone) {
        var selectedSlot = drone.getSelectedSlot();
        var slotContents = drone.getInventory().getStack(selectedSlot);
        if(slotContents.getCount() == 0) return CompletableFuture.completedFuture(DroneCommandResult.failure("No items to place or use"));

        FakePlayer fakePlayer = FakePlayer.get(drone.getLevel());
        fakePlayer.setPos(drone.getPosition().x, drone.getPosition().y, drone.getPosition().z);
        fakePlayer.setPitch(drone.getEntity().getPitch());
        fakePlayer.setYaw(drone.getEntity().getYaw());

        var positionToUse = drone.getEntity().getBlockPos().offset(Direction.DOWN);

        BlockHitResult hit = new BlockHitResult(drone.getPosition(), Direction.UP, drone.getEntity().getBlockPos(), false);

        var useContext = new ItemUsageContext(fakePlayer, fakePlayer.getActiveHand(), hit);

        var placeContext = new ItemPlacementContext(useContext);

        var canPlace = letMePlace(placeContext, drone, fakePlayer, positionToUse, Direction.UP, true);
        if (!canPlace) {
            fakePlayer.remove(Entity.RemovalReason.DISCARDED);
            return CompletableFuture.completedFuture(DroneCommandResult.failure("Cannot place block here"));
        }

        var result = slotContents.useOnBlock(useContext);
        if (result.isAccepted()) {
            fakePlayer.remove(Entity.RemovalReason.DISCARDED);
            slotContents.decrement(1);
            return CompletableFuture.completedFuture(DroneCommandResult.success());
        } else {
            fakePlayer.remove(Entity.RemovalReason.DISCARDED);
            return CompletableFuture.completedFuture(DroneCommandResult.failure("Cannot place block here"));
        }
    }
}
