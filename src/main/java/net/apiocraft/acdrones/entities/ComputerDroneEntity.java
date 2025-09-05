package net.apiocraft.acdrones.entities;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.core.computer.ComputerSide;
import dan200.computercraft.shared.ModRegistry;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.computer.core.ServerContext;
import dan200.computercraft.shared.config.Config;
import dan200.computercraft.shared.network.container.ComputerContainerData;
import dan200.computercraft.shared.util.ComponentMap;
import dan200.computercraft.shared.util.DirectionUtil;
import dan200.computercraft.shared.util.IDAssigner;
import dan200.computercraft.shared.util.NonNegativeId;
import net.apiocraft.acdrones.Acdrones;
import net.apiocraft.acdrones.accessories.simpleAccessories.chunkloader.DroneChunkloaderAccessory;
import net.apiocraft.acdrones.core.DroneBrain;
import net.apiocraft.acdrones.core.IDroneAccessory;
import net.apiocraft.acdrones.inventory.AccessoryInventory;
import net.apiocraft.acdrones.inventory.AttachmentInventory;
import net.apiocraft.acdrones.inventory.DroneInventory;
import net.apiocraft.acdrones.menu.DroneMenu;
import net.apiocraft.acdrones.registries.DroneAccessoryRegistry;
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
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;


public class ComputerDroneEntity extends Entity implements NamedScreenHandlerFactory {
    public static final int INVENTORY_SIZE = 4; // a drone will only have a few inventory slots
    private static final String NBT_LABEL = "Label";
    private static final String NBT_ON = "On";
    private static final String NBT_ID = "ComputerId";
    private static final String NBT_HOVERING = "Hovering";
    private static final String NBT_SELECTED_SLOT = "SelectedSlot";
    private static final String NBT_ACCESSORY = "Accessory";
    private static final TrackedData<Boolean> on = DataTracker.registerData(ComputerDroneEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Optional<IDroneAccessory>> accessory = DataTracker.registerData(ComputerDroneEntity.class, Acdrones.DRONE_ACCESSORY_HANDLER);
    // TODO: suppport a dynamic number of attachments
    // I gave the hell up trying to get arrays to work with datatrackers
    // i'll make it later... probably
    // for now, excuse me for this abomination
    private static final TrackedData<Optional<IDroneAccessory>> attachment_1 = DataTracker.registerData(ComputerDroneEntity.class, Acdrones.DRONE_ACCESSORY_HANDLER);
    private static final TrackedData<Optional<IDroneAccessory>> attachment_2 = DataTracker.registerData(ComputerDroneEntity.class, Acdrones.DRONE_ACCESSORY_HANDLER);
    private final DroneInventory inventory;
    private final AccessoryInventory accessoryInventory;
    private final AttachmentInventory attachmentInventory;
    private final DroneBrain brain;
    public boolean hovering = false;
    protected @Nullable String label = null;


    //private IDroneAccessory accessory;
    private ChunkPos lastChunkPos;
    private int computerId = -1;
    private boolean startByTurningTheHellOn = false;
    private UUID instanceID;
    private boolean itsBrandNew;
    private int selectedSlot = 0;
    private Vec3d lastVelocity = Vec3d.ZERO;

    public ComputerDroneEntity(EntityType<? extends ComputerDroneEntity> type, World world) {
        super(type, world);
        brain = new DroneBrain(this);
        inventory = new DroneInventory(this);
        accessoryInventory = new AccessoryInventory(this.brain);
        attachmentInventory = new AttachmentInventory(this.brain);
    }

    public static DefaultAttributeContainer.Builder createComputerDroneAttributes() {
        return DefaultAttributeContainer.builder().add(EntityAttributes.GENERIC_MAX_HEALTH).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE).add(EntityAttributes.GENERIC_MOVEMENT_SPEED).add(EntityAttributes.GENERIC_ARMOR).add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS).add(EntityAttributes.GENERIC_MAX_ABSORPTION).add(EntityAttributes.GENERIC_STEP_HEIGHT).add(EntityAttributes.GENERIC_SCALE).add(EntityAttributes.GENERIC_GRAVITY).add(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE).add(EntityAttributes.GENERIC_FALL_DAMAGE_MULTIPLIER).add(EntityAttributes.GENERIC_JUMP_STRENGTH).add(EntityAttributes.GENERIC_OXYGEN_BONUS).add(EntityAttributes.GENERIC_BURNING_TIME).add(EntityAttributes.GENERIC_EXPLOSION_KNOCKBACK_RESISTANCE).add(EntityAttributes.GENERIC_WATER_MOVEMENT_EFFICIENCY).add(EntityAttributes.GENERIC_MOVEMENT_EFFICIENCY).add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK);
    }

//    private void setupChunkloading() {
//        if(!getWorld().isClient()) {
//            ServerWorld sw = (ServerWorld) getWorld();
//
//            sw.getChunkManager().addTicket(
//                    ChunkTicketType.PLAYER,
//                    new ChunkPos(getBlockPos()),
//                    1,
//                    new ChunkPos(getBlockPos())
//            );
//        }
//    }

    public static float lerpDegrees(float start, float end, float amount)
    {
        float difference = Math.abs(end - start);
        // i know, i know, now shut, it's fine
        if (difference > 180)
        {
            if (end > start)
            {
                start += 360;
            }
            else
            {
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

        // TODO: get this the f* outta the drone entity
        if(!getWorld().isClient() && Arrays.stream(getAccessoryAttachments()).anyMatch(a -> a instanceof DroneChunkloaderAccessory)) {
            ServerWorld serverWorld = (ServerWorld) getWorld();
            ChunkPos chunkPos = new ChunkPos(getBlockPos());
            if(!chunkPos.equals(lastChunkPos)) {
                System.out.println("Supposedly loading chunk");

                // first load our chunk to not get unlodead
                serverWorld.getChunkManager().addTicket(
                        ChunkTicketType.FORCED,
                        chunkPos,
                        3,
                        chunkPos
                );

                if(lastChunkPos != null) {

                    serverWorld.getChunkManager().removeTicket(
                            ChunkTicketType.FORCED,
                            lastChunkPos,
                            3,
                            lastChunkPos
                    );
                }

                lastChunkPos = chunkPos;
            }
        }

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
            float newYaw = lerpDegrees(currentYaw, (float) targetYaw, 0.25f);
            setYaw(newYaw);
        }

        // Linear interpolation for pitch
        float newPitch = lerpDegrees(currentPitch, (float) targetPitch, 0.25f);
        setPitch(newPitch);

        lastVelocity = new Vec3d(vel.getX(), vel.getY(), vel.getZ());
    }


    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        System.out.println("Interacting with drone");
        if (player.isSneaking()) {
            if (!getWorld().isClient()) {
                System.out.println("not on client, so we turn on and open gui");
                var serverComputer = createServerComputer();
                serverComputer.turnOn();
                //player.openHandledScreen(this);
                new ComputerContainerData(serverComputer, ItemStack.EMPTY).open(player, this);
            }
            return ActionResult.SUCCESS;
        } else {
            System.out.println("not sneaking");
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
    public boolean isAttackable() {
        return true;
    }

    @Override
    public boolean handleAttack(Entity attacker) {
        if(attacker instanceof PlayerEntity) {
            if(!getWorld().isClient()) {
                ItemScatterer.spawn(getWorld(), getBlockPos(), getInventory());
                ItemScatterer.spawn(getWorld(), getBlockPos(), getAccessoryInventory());
                ItemStack i = new ItemStack(Acdrones.COMPUTER_DRONE_ITEM);
                i.set(ModRegistry.DataComponents.COMPUTER_ID.get(), NonNegativeId.of(computerId));

                ItemScatterer.spawn(getWorld(), getBlockPos().getX(), getBlockPos().getY() + 1, getBlockPos().getZ(), i);
                remove(RemovalReason.KILLED);
            }
        }
        return true;
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        if(!getWorld().isClient() && lastChunkPos != null) {
            ((ServerWorld)getWorld()).getChunkManager().removeTicket(
                    ChunkTicketType.PLAYER,
                    lastChunkPos,
                    1,
                    lastChunkPos
            );
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
        builder.add(accessory, Optional.empty());
        builder.add(attachment_1, Optional.empty());
        builder.add(attachment_2, Optional.empty());
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
        dataTracker.set(accessory, (nbt.contains(NBT_ACCESSORY) ? Optional.of(DroneAccessoryRegistry.createFromNbt(nbt.getCompound(NBT_ACCESSORY))) : Optional.empty()));
        // if the accessory exists, set the drone
        if(dataTracker.get(accessory).isPresent()) {
            getAccessory().setDrone(brain);
        }
        // now the same for all the attachments
        dataTracker.set(attachment_1, (nbt.contains("Attachment1") ? Optional.of(DroneAccessoryRegistry.createFromNbt(nbt.getCompound("Attachment1"))) : Optional.empty()));
        if(dataTracker.get(attachment_1).isPresent()) {
            getAccessoryAttachment(0).setDrone(brain);
        }
        dataTracker.set(attachment_2, (nbt.contains("Attachment2") ? Optional.of(DroneAccessoryRegistry.createFromNbt(nbt.getCompound("Attachment2"))) : Optional.empty()));
        if(dataTracker.get(attachment_2).isPresent()) {
            getAccessoryAttachment(1).setDrone(brain);
        }

        // I don't even remember why this is here
        NbtCompound accessoryInventoryNbt = nbt.getCompound("AccessoryInventory");



    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        NbtList list = inventory.writeNbt(new NbtList());
        nbt.put("Inventory", list);
        if (computerId >= 0) nbt.putInt(NBT_ID, computerId);
        if (label != null) nbt.putString(NBT_LABEL, label);
        if (dataTracker.get(on)) nbt.putBoolean(NBT_ON, true);
        if (hovering) nbt.putBoolean(NBT_HOVERING, true);
        if(dataTracker.get(accessory).isPresent()) {
            // forgive my warcrime
            NbtCompound accessoryNbt = getAccessory().toNbt();
            accessoryNbt.putString("id", DroneAccessoryRegistry.getId(getAccessory()).toString());
            nbt.put(NBT_ACCESSORY, accessoryNbt);
        }
        if(dataTracker.get(attachment_1).isPresent()) {
            NbtCompound accessoryNbt = getAccessoryAttachment(0).toNbt();
            accessoryNbt.putString("id", DroneAccessoryRegistry.getId(getAccessoryAttachment(0)).toString());
            nbt.put("Attachment1", accessoryNbt);
        }
        if(dataTracker.get(attachment_2).isPresent()) {
            NbtCompound accessoryNbt = getAccessoryAttachment(1).toNbt();
            accessoryNbt.putString("id", DroneAccessoryRegistry.getId(getAccessoryAttachment(1)).toString());
            nbt.put("Attachment2", accessoryNbt);
        }


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
            if (DirectionUtil.toLocal(Direction.EAST, direction) == ComputerSide.TOP) {
                computer.setPeripheral(DirectionUtil.toLocal(Direction.UP, direction), getAccessoryAttachment(0));
            } else if (DirectionUtil.toLocal(Direction.EAST, direction) == ComputerSide.BACK) {
                computer.setPeripheral(DirectionUtil.toLocal(Direction.EAST, direction), getAccessoryAttachment(1));
            } else {
                computer.setPeripheral(DirectionUtil.toLocal(Direction.EAST, direction), null);
            }
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


    public IDroneAccessory getAccessory() {
        var a = dataTracker.get(accessory);
        return a.orElse(null);
    }

    public void setAccessory(IDroneAccessory accessory) {
        if(accessory != null) {
            accessory.setDrone(brain);
            dataTracker.set(ComputerDroneEntity.accessory, Optional.of(accessory));
        } else {
            dataTracker.set(ComputerDroneEntity.accessory, Optional.empty());
        }
    }

    public IDroneAccessory[] getAccessoryAttachments() {
        return new IDroneAccessory[] {
            dataTracker.get(attachment_1).orElse(null),
            dataTracker.get(attachment_2).orElse(null),
        };
    }

    public IDroneAccessory getAccessoryAttachment(int index) {
        switch(index) {
            case 0:
                return dataTracker.get(attachment_1).orElse(null);
            case 1:
                return dataTracker.get(attachment_2).orElse(null);
        }
        return null;
    }

    public void setAccessoryAttachment(int slot, IDroneAccessory accessory) {
        if(accessory != null) {
            accessory.setDrone(brain);
            switch(slot) {
                case 0:
                    accessory.setDrone(brain);
                    dataTracker.set(attachment_1, Optional.of(accessory));
                    updateInputsImmediately(createServerComputer());
                    break;
                case 1:
                    accessory.setDrone(brain);
                    dataTracker.set(attachment_2, Optional.of(accessory));
                    updateInputsImmediately(createServerComputer());
                    break;
            }
        } else {
            switch(slot) {
                case 0:
                    dataTracker.set(attachment_1, Optional.empty());
                    break;
                case 1:
                    dataTracker.set(attachment_2, Optional.empty());
                    break;
            }
        }
    }



    public AccessoryInventory getAccessoryInventory() {
        return accessoryInventory;
    }

    public TrackedData<Optional<IDroneAccessory>> getTrackedAccessory() {
        return accessory;
    }

    public int getComputerId() {
        return computerId;
    }

    public void setComputerId(int computerId) {
        this.computerId = computerId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String customName) {
        this.label = customName;
    }

    public AttachmentInventory getAttachmentInventory() {
        return attachmentInventory;
    }
}
