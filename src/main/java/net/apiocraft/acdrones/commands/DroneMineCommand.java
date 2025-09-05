package net.apiocraft.acdrones.commands;

import com.mojang.authlib.GameProfile;
import eu.pb4.common.protection.api.CommonProtection;
import net.apiocraft.acdrones.DroneCommand;
import net.apiocraft.acdrones.DroneCommandResult;
import net.apiocraft.acdrones.core.IDroneAccess;
import net.minecraft.block.BlockState;

import java.util.concurrent.CompletableFuture;

public class DroneMineCommand implements DroneCommand {
    @Override
    public CompletableFuture<DroneCommandResult> execute(IDroneAccess drone) {
        if(!drone.getLevel().isAir(drone.getEntity().getBlockPos())) {
            BlockState block = drone.getLevel().getBlockState(drone.getEntity().getBlockPos());
            if(block.getHardness(drone.getLevel(), drone.getEntity().getBlockPos()) >= 0 && block.getHardness(drone.getLevel(), drone.getEntity().getBlockPos()) <= 1) {
                drone.getLevel().breakBlock(drone.getEntity().getBlockPos(), true, drone.getEntity());
                return CompletableFuture.completedFuture(DroneCommandResult.success());
            } else {
                return CompletableFuture.completedFuture(DroneCommandResult.failure("Block is unbreakable"));
            }
        } else {
            return CompletableFuture.completedFuture(DroneCommandResult.failure("No block to mine"));
        }
    }
}
