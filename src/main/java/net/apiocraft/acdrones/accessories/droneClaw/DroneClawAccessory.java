package net.apiocraft.acdrones.accessories.droneClaw;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.apiocraft.acdrones.core.DroneAccessoryAttachment;
import net.apiocraft.acdrones.core.DroneAccessoryType;
import net.apiocraft.acdrones.core.IDroneAccess;
import net.apiocraft.acdrones.core.IDroneAccessory;
import net.apiocraft.acdrones.registries.DroneAccessoryTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Clearable;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DroneClawAccessory implements IDroneAccessory {
    public static final PacketCodec<RegistryByteBuf, DroneClawAccessory> CODEC = PacketCodec.of(
            ((value, buf) -> {
                buf.writeNbt(value.toNbt());
            }),
            (buf -> {
                DroneClawAccessory accessory = new DroneClawAccessory();
                accessory.fromNbt(buf.readNbt());
                return accessory;
            })
    );

    private IDroneAccess drone;

    public DroneClawAccessory() {
    }



    NbtCompound carryData = new NbtCompound();

    @LuaFunction(mainThread = true)
    @SuppressWarnings("unused")
    public final MethodResult grab() {
        // block below drone
        BlockPos pos = drone.getEntity().getBlockPos();
        if(drone.getEntity().getEntityWorld().getBlockState(pos).isAir()) {
            pos = pos.down();
        }
        // is there any block there?
        if(drone.getEntity().getEntityWorld().getBlockState(pos).isAir()) {
            return MethodResult.of(null, "no block to grab");
        }
        // is there any block in the claw?
        if(!carryData.isEmpty()) {
            return MethodResult.of(null, "claw is full");
        }
        // get block data
        BlockState state = drone.getEntity().getEntityWorld().getBlockState(pos);
        BlockEntity entity = drone.getEntity().getEntityWorld().getBlockEntity(pos);
        // save block data
        carryData.putByte("carryType", (byte) 0);
        carryData.put("carryState", NbtHelper.fromBlockState(state));
        if(entity != null) {
            carryData.put("carryEntity", entity.createNbtWithId(drone.getEntity().getRegistryManager()));
            Clearable.clear(entity);

        }
        // remove block
        drone.getEntity().getEntityWorld().setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
        drone.getEntity().getDataTracker().set(drone.getEntity().getTrackedAccessory(), Optional.of(this), true); // TODO: unfrick this
        return MethodResult.of(true);
    }

    @LuaFunction(mainThread = true)
    @SuppressWarnings("unused")
    public final MethodResult drop() {
        // block below drone
        BlockPos pos = drone.getEntity().getBlockPos().down();
        if(!drone.getEntity().getEntityWorld().getBlockState(pos).isAir()) {
            return MethodResult.of(null, "block below drone is not air");
        }
//        if(drone.getEntity().getEntityWorld().getBlockState(pos.down()).isAir()) {
//            return MethodResult.of(null, "no block to drop on");
//        }
        // is there any block in the claw?
        if(carryData.isEmpty()) {
            return MethodResult.of(null, "claw is empty");
        }

        // get block data
        BlockState state = NbtHelper.toBlockState(Registries.BLOCK.getReadOnlyWrapper(), carryData.getCompound("carryState"));
        //BlockEntity entity = null;
        if(carryData.contains("carryEntity")) {
            //entity = BlockEntity.createFromNbt(pos, state, carryData.getCompound("carryEntity"), drone.getEntity().getRegistryManager());
        }
        var falling = FallingBlockEntity.spawnFromBlock(drone.getLevel(), pos, state);
        if(carryData.contains("carryEntity")) {
            falling.blockEntityData = carryData.getCompound("carryEntity");
        }
        // place block
//        drone.getLevel().setBlockState(pos, state, 2);
//        if(entity != null) {
//            drone.getLevel().addBlockEntity(entity);
//        }
        // clear block data
        carryData = new NbtCompound();
        drone.getEntity().getDataTracker().set(drone.getEntity().getTrackedAccessory(), Optional.of(this), true); // TODO: unfrick this
        return MethodResult.of(true);
    }

    @Override
    public void update() {

    }

    @Override
    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.put("carryData", carryData);
        return nbt;
    }

    @Override
    public void fromNbt(NbtCompound nbt) {
        carryData = nbt.getCompound("carryData");
    }


    @Override
    public String getType() {
        return "claw";
    }

    @Override
    public DroneAccessoryType<?> getAccessoryType() {
        return DroneAccessoryTypes.DRONE_CLAW;
    }

    @Override
    public DroneAccessoryAttachment getAttachmentType() {
        return DroneAccessoryAttachment.BOTTOM;
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof DroneClawAccessory;
    }

    @Override
    public void setDrone(IDroneAccess drone) {
        this.drone = drone;
    }

    public boolean isCarryingBlock() {
//        System.out.println(!carryData.isEmpty());
//        System.out.println(carryData.getByte("carryType") == (byte) 0);
        return !carryData.isEmpty() && carryData.getByte("carryType") == (byte) 0;
    }

    public BlockState getCarriedBlock() {
        return NbtHelper.toBlockState(Registries.BLOCK.getReadOnlyWrapper(), carryData.getCompound("carryState"));
    }

    public BlockEntity getCarriedBlockEntity() {
        if(carryData.contains("carryEntity")) {
            return BlockEntity.createFromNbt(drone.getEntity().getBlockPos().down(), getCarriedBlock(), carryData.getCompound("carryEntity"), drone.getEntity().getRegistryManager());
        }
        return null;
    }

    public NbtCompound getCarryData() {
        return carryData;
    }
}
