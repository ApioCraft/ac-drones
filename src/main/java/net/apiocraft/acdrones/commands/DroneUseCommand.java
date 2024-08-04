package net.apiocraft.acdrones.commands;

import net.apiocraft.acdrones.DroneCommand;
import net.apiocraft.acdrones.DroneCommandResult;
import net.apiocraft.acdrones.IDroneAccess;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.CompletableFuture;

public class DroneUseCommand implements DroneCommand {

    public static BlockHitResult getHitResult(BlockPos position, Direction side) {
        var hitX = 0.5 + side.getOffsetX() * 0.5;
        var hitY = 0.5 + side.getOffsetY() * 0.5;
        var hitZ = 0.5 + side.getOffsetZ() * 0.5;
        if (Math.abs(hitY - 0.5) < 0.01) hitY = 0.45;

        return new BlockHitResult(new Vec3d(position.getX() + hitX, position.getY() + hitY, position.getZ() + hitZ), side, position, false);
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

        ItemUsageContext context = new ItemUsageContext();
    }
}
