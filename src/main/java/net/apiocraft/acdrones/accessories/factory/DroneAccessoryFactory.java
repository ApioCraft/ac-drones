/*
 * Copyright (c) 2025 qrmcat
 *
 * This file is part of ac-drones.
 *
 * ac-drones is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * ac-drones is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ac-drones; if not, see <https://www.gnu.org/licenses/>.
 *
 */

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
