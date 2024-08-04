package net.apiocraft.acdrones.commands;

import net.apiocraft.acdrones.DroneCommand;
import net.apiocraft.acdrones.DroneCommandResult;
import net.apiocraft.acdrones.IDroneAccess;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.CompletableFuture;

public class DroneMoveCommand implements DroneCommand {
    CompletableFuture<DroneCommandResult> resultPromise = new CompletableFuture<>();

    Vec3d wantedDeltaMovement;
    Vec3d initialWantedDeltaMovement;

    Vec3d lastPosition;

    int doneIterations = 0;

    float speedPercent = 0.01f;

    public DroneMoveCommand(double x, double y, double z) {
        this.wantedDeltaMovement = new Vec3d(x, y, z);
        this.initialWantedDeltaMovement = new Vec3d(x, y, z);
    }

    @Override
    public CompletableFuture<DroneCommandResult> execute(IDroneAccess drone) {
        lastPosition = drone.getPosition();
        return resultPromise;
    }

    @Override
    public void update(IDroneAccess drone) {
        doneIterations++;
        Vec3d currentPosition = drone.getPosition();

        // we move the drone towards the target (our position plus the wanted delta movement)
        // then subtract the amount we actually moved from the wanted delta movement

        Vec3d targetPosition = currentPosition.add(wantedDeltaMovement);
        // the percentage of the way we are already to the target
        double progress = 1-(currentPosition.distanceTo(targetPosition) / initialWantedDeltaMovement.length());
        //System.out.println(progress);
        // we can use some kind of tweening

        if (progress < 0.5) {
            if(speedPercent < 0.25) {
                speedPercent += 0.01;
            }
        } else {
            if(speedPercent > 0.05 && currentPosition.distanceTo(targetPosition) <= 2) {
                // decrease by how close we are to the target.
                // if we are very close, we want to slow down alot
                speedPercent -= 0.01 * (2-currentPosition.distanceTo(targetPosition));
            }
        }

        Vec3d move = targetPosition.subtract(currentPosition).normalize().multiply(speedPercent);
        //System.out.println("Move: " + move);

        drone.setVelocity(move);
        wantedDeltaMovement = wantedDeltaMovement.subtract(currentPosition.subtract(lastPosition));
        //System.out.println("Wanted: " + wantedDeltaMovement);
        var distance = wantedDeltaMovement.length();
        if(distance < 0.1) {
            drone.setVelocity(Vec3d.ZERO);
            resultPromise.complete(DroneCommandResult.success());
        } else {
            // we need to get how much we actually moved

        }



        if(doneIterations > 100) {
            resultPromise.complete(DroneCommandResult.failure("Movement took too long"));
        }

        lastPosition = currentPosition;
    }
}
