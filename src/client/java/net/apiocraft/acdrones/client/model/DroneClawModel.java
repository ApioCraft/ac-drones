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

import net.apiocraft.acdrones.accessories.droneClaw.DroneClawAccessory;
import net.minecraft.block.Block;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;

public class DroneClawModel {
    private final ModelPart hand2;
    private final ModelPart handEnd2;
    private final ModelPart hand1;
    private final ModelPart handEnd;
    private final ModelPart bb_main;
    public DroneClawModel(ModelPart root) {
        this.hand2 = root.getChild("hand2");
        this.handEnd2 = hand2.getChild("handEnd2");
        this.hand1 = root.getChild("hand1");
        this.handEnd = hand1.getChild("handEnd");
        this.bb_main = root.getChild("bb_main");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData hand2 = modelPartData.addChild("hand2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 11.5F, -2.0F));

        ModelPartData cube_r1 = hand2.addChild("cube_r1", ModelPartBuilder.create().uv(0, 12).cuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -2.0F, -2.8798F, 0.0F, 0.0F));

        ModelPartData cube_r2 = hand2.addChild("cube_r2", ModelPartBuilder.create().uv(16, 13).cuboid(-2.0F, -0.5F, 6.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 6.0F, -3.1416F, 0.0F, 0.0F));

        ModelPartData handEnd2 = hand2.addChild("handEnd2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.0F, -6.0F));

        ModelPartData cube_r3 = handEnd2.addChild("cube_r3", ModelPartBuilder.create().uv(10, 14).cuboid(-0.5F, 0.5F, 4.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.0F, 4.0F, -2.618F, 0.0F, 0.0F));

        ModelPartData hand1 = modelPartData.addChild("hand1", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 11.5F, 2.0F));

        ModelPartData cube_r4 = hand1.addChild("cube_r4", ModelPartBuilder.create().uv(11, 7).cuboid(-1.0F, -0.5F, -5.0F, 2.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 2.0F, 2.8798F, 0.0F, 0.0F));

        ModelPartData cube_r5 = hand1.addChild("cube_r5", ModelPartBuilder.create().uv(0, 18).cuboid(-2.0F, -0.5F, 0.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 2.0F, -3.1416F, 0.0F, 0.0F));

        ModelPartData handEnd = hand1.addChild("handEnd", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.0F, 6.0F));

        ModelPartData cube_r6 = handEnd.addChild("cube_r6", ModelPartBuilder.create().uv(16, 0).cuboid(-0.5F, 0.5F, -8.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.0F, -4.0F, 2.618F, 0.0F, 0.0F));

        ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -14.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.0F))
                .uv(12, 0).cuboid(-1.0F, -16.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 7).cuboid(-2.0F, -17.0F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 32, 32);
    }
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        hand2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        hand1.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        bb_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    public void rotato(DroneClawAccessory accessory) {
        if (accessory.isCarryingBlock()) {
            var block = accessory.getCarriedBlock();
            VoxelShape collisionShape = null;
            try {
                block.getCollisionShape(null, null, null);
            } catch (Exception e) {
                // Ignore exceptions when getting the collision shape
            }

            if (collisionShape == null || collisionShape.isEmpty()) {
                // Default to a full block shape if the collision shape is null or empty
                collisionShape = Block.createCuboidShape(0, 0, 0, 16, 16, 16);
            }

            // Calculate block dimensions
            float blockWidth = (float) (collisionShape.getMax(Direction.Axis.X) - collisionShape.getMin(Direction.Axis.X));
            float blockDepth = (float) (collisionShape.getMax(Direction.Axis.Z) - collisionShape.getMin(Direction.Axis.Z));
            float blockHeight = (float) (collisionShape.getMax(Direction.Axis.Y) - collisionShape.getMin(Direction.Axis.Y));

            // Calculate angles for hands
            float handAngle = MathHelper.clamp(blockWidth / 2, 0.2f, 0.8f);
            float handPitch = (float) Math.toRadians(30 * handAngle);

            // Set hand rotations
            hand1.pitch = -handPitch;
            hand2.pitch = handPitch;
            hand1.yaw = (float) Math.toRadians(-5);
            hand2.yaw = (float) Math.toRadians(5);

            // Calculate and set hand end rotations
            float handEndAngle = MathHelper.clamp(blockHeight / 2, 0.2f, 0.8f);
            float handEndPitch = (float) Math.toRadians(70 * handEndAngle);

            handEnd.pitch = -handEndPitch;
            handEnd2.pitch = handEndPitch;

            // Adjust positions to prevent intersection
            float verticalOffset = Math.max(blockHeight / 2, 1.0f);
            float horizontalOffset = Math.max(blockWidth / 2 + 0.5f, 2.5f);

            hand1.pivotY = 11.5F - verticalOffset;
            hand2.pivotY = 11.5F - verticalOffset;
            hand1.pivotZ = horizontalOffset;
            hand2.pivotZ = -horizontalOffset;

            // Adjust main body position if needed
            bb_main.pivotY = 24.0F - verticalOffset;
        } else {
            // Reset to default position when not carrying a block
            hand1.pitch = 0;
            hand2.pitch = 0;
            hand1.yaw = 0;
            hand2.yaw = 0;
            handEnd.pitch = 0;
            handEnd2.pitch = 0;
            hand1.pivotY = 11.5F;
            hand2.pivotY = 11.5F;
            hand1.pivotZ = 2.0F;
            hand2.pivotZ = -2.0F;
            bb_main.pivotY = 24.0F;
        }
    }
}
