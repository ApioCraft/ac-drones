package net.apiocraft.acdrones.commands;

import net.apiocraft.acdrones.DroneCommand;
import net.apiocraft.acdrones.DroneCommandResult;
import net.apiocraft.acdrones.core.IDroneAccess;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;

import java.util.concurrent.CompletableFuture;

public class DroneLidarCommand implements DroneCommand {
    private final double x;
    private double y;
    private static final double MAX_DISTANCE = 100.0; // Maximum scan distance

    public DroneLidarCommand(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public CompletableFuture<DroneCommandResult> execute(IDroneAccess drone) {
        Vec3d dronePos = drone.getPosition();

        // Calculate the ray direction based on x and y inputs
        // Start with a downward vector (shooting from bottom of drone)
        Vec3d baseRayDir = Vec3d.of(Direction.DOWN.getVector());

        // make sure y is within bounds (-180 to 90)
        y = Math.min(90, Math.max(-180, y));

        // Rotate this vector based on x and y inputs
        Vec3d rayDir = baseRayDir
                .rotateX((float)Math.toRadians(y))
                .rotateY((float)Math.toRadians(-x))
                .normalize();

        Vec3d endPos = dronePos.add(rayDir.multiply(MAX_DISTANCE));

        // Perform the raycast
        RaycastContext context = new RaycastContext(dronePos, endPos,
                RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, drone.getEntity());
        HitResult hitResult = drone.getLevel().raycast(context);

        // for testing, we are going to spawn barrier particles at the hit position
        if (hitResult.getType() != HitResult.Type.MISS) {
            drone.getLevel().spawnParticles(ParticleTypes.HEART, hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z, 1, 0, 0, 0, 0);
        }

        if (hitResult.getType() == HitResult.Type.MISS) {
            return CompletableFuture.completedFuture(DroneCommandResult.success());
        } else {
            double distance = dronePos.distanceTo(hitResult.getPos());
            return CompletableFuture.completedFuture(DroneCommandResult.success(new Object[]{distance}));
        }
    }
}