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

package net.apiocraft.acdrones.client.model;

import com.google.common.collect.ImmutableList;
import dan200.computercraft.core.computer.Computer;
import net.apiocraft.acdrones.entities.ComputerDroneEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class ComputerDroneEntityModel extends EntityModel<ComputerDroneEntity> {
    private final ModelPart Drone;
    private final ModelPart Core;
    private final ModelPart Rotor_BL;
    private final ModelPart Propeller;
    private final ModelPart Rotor_BR;
    private final ModelPart Propeller2;
    private final ModelPart Rotor_FL;
    private final ModelPart Propeller3;
    private final ModelPart Rotor_FR;
    private final ModelPart Propeller4;

    public ComputerDroneEntityModel(ModelPart root) {
        this.Drone = root.getChild("Drone");
        this.Core = Drone.getChild("Core");
        this.Rotor_BL = Drone.getChild("Rotor_BL");
        this.Propeller = Rotor_BL.getChild("Propeller");
        this.Rotor_BR = Drone.getChild("Rotor_BR");
        this.Propeller2 = Rotor_BR.getChild("Propeller2");
        this.Rotor_FL = Drone.getChild("Rotor_FL");
        this.Propeller3 = Rotor_FL.getChild("Propeller3");
        this.Rotor_FR = Drone.getChild("Rotor_FR");
        this.Propeller4 = Rotor_FR.getChild("Propeller4");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData Drone = modelPartData.addChild("Drone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData Core = Drone.addChild("Core", ModelPartBuilder.create().uv(0, 21).cuboid(-4.0F, -3.0F, -4.0F, 8.0F, 3.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData Rotor_BL = Drone.addChild("Rotor_BL", ModelPartBuilder.create().uv(0, 3).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F))
                .uv(8, 5).cuboid(-0.5F, -5.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(7.0F, 0.0F, 7.0F));

        ModelPartData Connector_r1 = Rotor_BL.addChild("Connector_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -1.0F, -1.0F, 6.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, -1.0F, -2.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData Propeller = Rotor_BL.addChild("Propeller", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -4.0F, 0.0F));

        ModelPartData Propeller_r1 = Propeller.addChild("Propeller_r1", ModelPartBuilder.create().uv(7, 3).cuboid(-0.5F, -0.75F, -2.5F, 1.0F, 0.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData Rotor_BR = Drone.addChild("Rotor_BR", ModelPartBuilder.create().uv(0, 3).cuboid(-8.0F, -3.0F, 6.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F))
                .uv(8, 5).cuboid(-7.5F, -5.0F, 6.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData Connector_r2 = Rotor_BR.addChild("Connector_r2", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -1.0F, -1.0F, 6.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-5.0F, -1.0F, 5.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData Propeller2 = Rotor_BR.addChild("Propeller2", ModelPartBuilder.create(), ModelTransform.pivot(-7.0F, -4.0F, 7.0F));

        ModelPartData Propeller_r2 = Propeller2.addChild("Propeller_r2", ModelPartBuilder.create().uv(7, 3).cuboid(-0.5F, -0.75F, -2.5F, 1.0F, 0.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData Rotor_FL = Drone.addChild("Rotor_FL", ModelPartBuilder.create().uv(0, 3).cuboid(-8.0F, -3.0F, -8.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F))
                .uv(8, 5).cuboid(-7.5F, -5.0F, -7.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData Connector_r3 = Rotor_FL.addChild("Connector_r3", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -1.0F, -1.0F, 6.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-5.0F, -1.0F, -5.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData Propeller3 = Rotor_FL.addChild("Propeller3", ModelPartBuilder.create(), ModelTransform.pivot(-7.0F, -4.0F, -7.0F));

        ModelPartData Propeller_r3 = Propeller3.addChild("Propeller_r3", ModelPartBuilder.create().uv(7, 3).cuboid(-0.5F, -0.75F, -2.5F, 1.0F, 0.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData Rotor_FR = Drone.addChild("Rotor_FR", ModelPartBuilder.create().uv(0, 3).cuboid(6.0F, -3.0F, -8.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F))
                .uv(8, 5).cuboid(6.5F, -5.0F, -7.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData Connector_r4 = Rotor_FR.addChild("Connector_r4", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -1.0F, -1.0F, 6.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, -1.0F, -5.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData Propeller4 = Rotor_FR.addChild("Propeller4", ModelPartBuilder.create(), ModelTransform.pivot(7.0F, -4.0F, -7.0F));

        ModelPartData Propeller_r4 = Propeller4.addChild("Propeller_r4", ModelPartBuilder.create().uv(7, 3).cuboid(-0.5F, -0.75F, -2.5F, 1.0F, 0.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        Drone.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }

    @Override
    public void setAngles(ComputerDroneEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }

    double yaw;
    double pitch;

    private static final double TWO_PI = 2 * Math.PI;

    // Angle-aware lerp function




    private final Vec3d lastVelocity = Vec3d.ZERO;

    public void customRotate(ComputerDroneEntity entity, float counter, float pitch, float yaw) {
        //Drone.pitch = (float) Math.toRadians(lerpDegrees(Math.toDegrees(Drone.pitch), entity.getPitch(), 0.3));
        //Drone.yaw = (float) Math.toRadians(lerpDegrees(Math.toDegrees(Drone.yaw), entity.getYaw(), 0.3));
        Drone.pitch = (float) Math.toRadians(pitch);
        Drone.yaw = (float) Math.toRadians(yaw);
        // Rotate propellers
        float propellerSpeed = ((float) entity.getVelocity().length() + 1) * (entity.isOn() ? 1 : 0);
        Propeller.yaw = counter * propellerSpeed;
        Propeller2.yaw = -counter * propellerSpeed;
        Propeller3.yaw = counter * propellerSpeed;
        Propeller4.yaw = -counter * propellerSpeed;

        // Update last velocity for next frame
    }
}
