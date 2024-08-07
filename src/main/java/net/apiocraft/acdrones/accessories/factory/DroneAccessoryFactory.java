package net.apiocraft.acdrones.accessories.factory;

import net.apiocraft.acdrones.core.IDroneAccessory;

import java.util.function.Supplier;

public class DroneAccessoryFactory {
    private final Supplier<IDroneAccessory> supplier;

    public DroneAccessoryFactory(Supplier<IDroneAccessory> supplier) {
        this.supplier = supplier;
    }

    public static DroneAccessoryFactory of(IDroneAccessory accessory) {
        return new DroneAccessoryFactory(() -> accessory.getClass().cast(accessory));
    }

    public IDroneAccessory create() {
        return supplier.get();
    }

    public Supplier<IDroneAccessory> getSupplier() {
        return supplier;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof DroneAccessoryFactory) {
            return supplier.equals(((DroneAccessoryFactory) obj).supplier);
        }
        return false;
    }
}
