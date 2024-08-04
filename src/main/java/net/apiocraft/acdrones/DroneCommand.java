package net.apiocraft.acdrones;

import java.util.concurrent.CompletableFuture;

public interface DroneCommand {
    CompletableFuture<DroneCommandResult> execute(IDroneAccess drone);
    default void update(IDroneAccess drone) {

    }
}
