package net.apiocraft.acdrones.accessories.droneClaw.commands;

import net.apiocraft.acdrones.DroneCommand;
import net.apiocraft.acdrones.DroneCommandResult;
import net.apiocraft.acdrones.core.IDroneAccess;
import net.apiocraft.acdrones.accessories.droneClaw.DroneClawAccessory;

import java.util.concurrent.CompletableFuture;

public class ClawGrabCommand implements DroneCommand {
    IDroneAccess drone;
    DroneClawAccessory accessory;
    public ClawGrabCommand(IDroneAccess drone, DroneClawAccessory accessory) {
        this.drone = drone;
        this.accessory = accessory;
    }
    @Override
    public CompletableFuture<DroneCommandResult> execute(IDroneAccess drone) {
        return null;
    }
}
