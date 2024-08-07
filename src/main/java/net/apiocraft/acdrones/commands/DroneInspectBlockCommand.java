package net.apiocraft.acdrones.commands;

import dan200.computercraft.api.detail.BlockReference;
import dan200.computercraft.api.detail.VanillaDetailRegistries;
import net.apiocraft.acdrones.DroneCommand;
import net.apiocraft.acdrones.DroneCommandResult;
import net.apiocraft.acdrones.core.IDroneAccess;

import java.util.concurrent.CompletableFuture;

public class DroneInspectBlockCommand implements DroneCommand {

    @Override
    public CompletableFuture<DroneCommandResult> execute(IDroneAccess drone) {
        if(drone.getLevel().getBlockState(drone.getEntity().getBlockPos().down()).isAir()) {
            return CompletableFuture.completedFuture(DroneCommandResult.failure("No block to look at"));
        }
        // get block below drone
        var table = VanillaDetailRegistries.BLOCK_IN_WORLD.getDetails(new BlockReference(drone.getLevel(), drone.getEntity().getBlockPos().down()));
        return CompletableFuture.completedFuture(DroneCommandResult.success(new Object[] { table }));
    }
}
