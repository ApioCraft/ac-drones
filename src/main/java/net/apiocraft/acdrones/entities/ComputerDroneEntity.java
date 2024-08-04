package net.apiocraft.acdrones.entities;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.computer.core.ServerContext;
import dan200.computercraft.shared.config.Config;
import dan200.computercraft.shared.network.container.ComputerContainerData;
import dan200.computercraft.shared.util.ComponentMap;
import dan200.computercraft.shared.util.DirectionUtil;
import dan200.computercraft.shared.util.IDAssigner;
import net.apiocraft.acdrones.Acdrones;
import net.apiocraft.acdrones.DroneBrain;
import net.apiocraft.acdrones.DroneInventory;
import net.apiocraft.acdrones.menu.DroneMenu;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;


public class ComputerDroneEntity extends Entity implements NamedScreenHandlerFactory {
    public static final int INVENTORY_SIZE = 4; // a drone will only have a few inventory slots

    private static final String NBT_LABEL = "Label";
    private static final String NBT_ON = "On";
    private static final String NBT_ID = "ComputerId";
    private static final String NBT_HOVERING = "Hovering";

    private static final String NBT_SELECTED_SLOT = "SelectedSlot";
    private static final TrackedData<Boolean> on = DataTracker.registerData(ComputerDroneEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private final DroneInventory inventory = new DroneInventory(this);
    public boolean hovering = false;
    protected @Nullable String label = null;
    private int computerId = -1;
    private boolean startByTurningTheHellOn = false;
    private UUID instanceID;
    private boolean itsBrandNew;
    private int selectedSlot = 0;


    private final DroneBrain brain = new DroneBrain(this);
    private Vec3d lastVelocity = Vec3d.ZERO;

    public ComputerDroneEntity(EntityType<? extends ComputerDroneEntity> type, World world) {
        super(type, world);
        setCustomNameVisible(true);
    }

    public static DefaultAttributeContainer.Builder createComputerDroneAttributes() {
        return DefaultAttributeContainer.builder().add(EntityAttributes.GENERIC_MAX_HEALTH).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE).add(EntityAttributes.GENERIC_MOVEMENT_SPEED).add(EntityAttributes.GENERIC_ARMOR).add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS).add(EntityAttributes.GENERIC_MAX_ABSORPTION).add(EntityAttributes.GENERIC_STEP_HEIGHT).add(EntityAttributes.GENERIC_SCALE).add(EntityAttributes.GENERIC_GRAVITY).add(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE).add(EntityAttributes.GENERIC_FALL_DAMAGE_MULTIPLIER).add(EntityAttributes.GENERIC_JUMP_STRENGTH).add(EntityAttributes.GENERIC_OXYGEN_BONUS).add(EntityAttributes.GENERIC_BURNING_TIME).add(EntityAttributes.GENERIC_EXPLOSION_KNOCKBACK_RESISTANCE).add(EntityAttributes.GENERIC_WATER_MOVEMENT_EFFICIENCY).add(EntityAttributes.GENERIC_MOVEMENT_EFFICIENCY).add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK);
    }

    public static float lerpDegrees(float start, float end, float amount)
    {
        float difference = Math.abs(end - start);
        if (difference > 180)
        {
            // We need to add on to one of the values.
            if (end > start)
            {
                // We'll add it on to start...
                start += 360;
            }
            else
            {
                // Add it on to end.
                end += 360;
            }
        }

        // Interpolate it.
        float value = (start + ((end - start) * amount));

        // Wrap it..
        float rangeZero = 360;

        if (value >= 0 && value <= 360)
            return value;

        return (value % rangeZero);
    }

    @Override
    protected double getGravity() {
        return 0.08;
    }

    @Override
    public void tick() {
        super.tick();
        //System.out.println(hasNoGravity());
        var movement = getMovement();
        move(MovementType.SELF, movement);
        updateDirection();

        if (!getWorld().isClient()) {


            var computer = createServerComputer();

            if (itsBrandNew || (startByTurningTheHellOn && !dataTracker.get(on))) {
                createServerComputer().turnOn();
                itsBrandNew = false;
                startByTurningTheHellOn = false;
            }

            setNoGravity(computer.isOn());
            applyGravity();
            // drag
            setVelocity(getVelocity().multiply(0.9, 0.9, 0.9));
            // if there is a entity on top of us, move it too!


            computer.keepAlive();

            itsBrandNew = false;
            computerId = computer.getID();

            dataTracker.set(on, computer.isOn());

            if (label != computer.getLabel()) {
                label = computer.getLabel();
            }

            brain.update();
        }

    }

    private void updateDirection() {
        Vec3d vel = getVelocity();
        Vec3d deltaVel = vel.subtract(lastVelocity);

        // Calculate target yaw (in degrees)
        double targetYaw = Math.toDegrees(Math.atan2(-vel.getX(), vel.getZ()));
        targetYaw = ((float)targetYaw);

        double horizontalSpeed = Math.sqrt(vel.getX() * vel.getX() + vel.getZ() * vel.getZ());
        double horizontalDeltaSpeed = Math.sqrt(deltaVel.getX() * deltaVel.getX() + deltaVel.getZ() * deltaVel.getZ());


        // Calculate target pitch (in degrees)
        double velocityWeight = 1;
        double deltaVelocityWeight = 0;
        double pitchFactor = horizontalSpeed * velocityWeight + horizontalDeltaSpeed * deltaVelocityWeight;
        double targetPitch = -Math.toDegrees(Math.atan(pitchFactor)) * 1.5;

        // Clamp pitch to avoid flipping
        //targetPitch = Math.max(-90, Math.min(90, targetPitch));

        float currentYaw = (getYaw());
        float currentPitch = (getPitch());

        // Linear interpolation for yaw
        if (horizontalSpeed > 0.01) {
            float newYaw = (float) lerpDegrees(currentYaw, (float) targetYaw, 0.25f);
            setYaw(newYaw);
        }

        // Linear interpolation for pitch
        float newPitch = (float) lerpDegrees(currentPitch, (float) targetPitch, 0.25f);
        setPitch(newPitch);

        lastVelocity = new Vec3d(vel.getX(), vel.getY(), vel.getZ());
    }


    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (player.isSneaking()) {
            if (!getWorld().isClient()) {
                var serverComputer = createServerComputer();
                serverComputer.turnOn();
                //player.openHandledScreen(this);
                new ComputerContainerData(serverComputer, ItemStack.EMPTY).open(player, this);
            }
            return ActionResult.SUCCESS;
        } else {
            // is the player riding already? if so, open gui
            if (player.hasVehicle()) {
                if (!getWorld().isClient()) {
                    var serverComputer = createServerComputer();
                    serverComputer.turnOn();
                    //player.openHandledScreen(this);
                    new ComputerContainerData(serverComputer, ItemStack.EMPTY).open(player, this);
                }
                return ActionResult.SUCCESS;
            } else {
                player.startRiding(this);
                return ActionResult.SUCCESS;
            }

        }
    }

    @Override
    public boolean isCollidable() {
        return true;
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(on, false);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        NbtList list = nbt.getList("Inventory", 10);
        inventory.readNbt(list);
        computerId = nbt.contains(NBT_ID) ? nbt.getInt(NBT_ID) : -1;
        label = nbt.contains(NBT_LABEL) ? nbt.getString(NBT_LABEL) : null;
        startByTurningTheHellOn = nbt.contains(NBT_ON) && nbt.getBoolean(NBT_ON);
        dataTracker.set(on, startByTurningTheHellOn);
        hovering = nbt.contains(NBT_HOVERING) && nbt.getBoolean(NBT_HOVERING);
        selectedSlot = nbt.contains(NBT_SELECTED_SLOT) ? nbt.getInt(NBT_SELECTED_SLOT) : 0;


    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        NbtList list = inventory.writeNbt(new NbtList());
        nbt.put("Inventory", list);
        if (computerId >= 0) nbt.putInt(NBT_ID, computerId);
        if (label != null) nbt.putString(NBT_LABEL, label);
        if (dataTracker.get(on)) nbt.putBoolean(NBT_ON, true);
        if (hovering) nbt.putBoolean(NBT_HOVERING, true);

        nbt.putInt(NBT_SELECTED_SLOT, selectedSlot);
    }

    public final ServerComputer createServerComputer() {
        var server = getWorld().getServer();
        if (server == null) throw new IllegalStateException("Cannot access server computer on the client.");

        var changed = false;

        var computer = ServerContext.get(server).registry().get(instanceID);
        if (computer == null) {
            System.out.println("Creating new computer bc no exist");
            if (computerId < 0) {
                System.out.println("Creating new computer id");
                computerId = ComputerCraftAPI.createUniqueNumberedSaveDir(server, IDAssigner.COMPUTER);
            }

            computer = createComputer(computerId);
            System.out.println("putting instance id");
            instanceID = computer.register();
            System.out.println(instanceID);
            itsBrandNew = true;
            changed = true;
        }

        if (changed) updateInputsImmediately(computer);
        return computer;
    }

    private void updateInputsImmediately(ServerComputer computer) {
        for (var direction : DirectionUtil.FACINGS) {
            computer.setPeripheral(DirectionUtil.toLocal(Direction.EAST, direction), null);
        }
    }

    protected ServerComputer createComputer(int id) {
        return new ServerComputer((ServerWorld) getWorld(), getBlockPos(), id, label, ComputerFamily.ADVANCED, Config.turtleTermWidth, Config.turtleTermHeight, ComponentMap.builder().add(Acdrones.DRONE, brain).build());

    }


    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return DroneMenu.ofBrain(syncId, playerInventory, brain);
    }

    public ServerComputer getServerComputer() {
        var server = getWorld().getServer();
        if (server == null) throw new IllegalStateException("Cannot access server computer on the client.");
        var computer = ServerContext.get(server).registry().get(instanceID);
        return computer;
    }

    public DroneInventory getInventory() {
        return inventory;
    }

    public boolean isOn() {
        return dataTracker.get(on);
    }

    public boolean isHovering() {
        return hovering;
    }

    public void setHovering(boolean hovering) {
        this.hovering = hovering;
    }

    public int getSelectedSlot() {
        return selectedSlot;
    }

    public void setSelectedSlot(int slot) {
        this.selectedSlot = slot;
    }


}
