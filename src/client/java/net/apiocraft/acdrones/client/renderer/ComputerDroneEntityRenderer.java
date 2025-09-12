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

package net.apiocraft.acdrones.client.renderer;

import net.apiocraft.acdrones.Acdrones;
import net.apiocraft.acdrones.client.AcdronesClient;
import net.apiocraft.acdrones.client.IAccessoryRenderer;
import net.apiocraft.acdrones.client.model.ComputerDroneEntityModel;
import net.apiocraft.acdrones.entities.ComputerDroneEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;

public class ComputerDroneEntityRenderer extends EntityRenderer<ComputerDroneEntity> {
    private final ModelPart base;
    ComputerDroneEntityModel model;


    private static final RenderLayer TEXTURE = RenderLayer.getEntityCutout(Identifier.of(Acdrones.MOD_ID, "textures/entity/drone.png"));
    public ComputerDroneEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.base = ComputerDroneEntityModel.getTexturedModelData().createModel();
        model = new ComputerDroneEntityModel(base);

    }

    public static double lerpDegrees(double start, double end, double t) {
        // Normalize angles to be between 0 and 360
        start = normalizeDegrees(start);
        end = normalizeDegrees(end);

        // Calculate the difference
        double diff = end - start;

        // Adjust for shortest path
        if (diff > 180) {
            diff -= 360;
        } else if (diff < -180) {
            diff += 360;
        }

        // Perform the lerp
        double result = start + diff * t;

        // Normalize the result
        return normalizeDegrees(result);
    }

    private static double normalizeDegrees(double degrees) {
        degrees = degrees % 360;
        if (degrees < 0) {
            degrees += 360;
        }
        return degrees;
    }

    @Override
    public void render(ComputerDroneEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {

        entity.propellerRotation += (float) (0.5 * tickDelta);

        // Smooth pitch and yaw changes
        float targetPitch = entity.getPitch();
        float targetYaw = entity.getYaw();

        entity.smoothPitch = (float) lerpDegrees(entity.smoothPitch, targetPitch, 0.3);
        entity.smoothYaw = (float) lerpDegrees(entity.smoothYaw, targetYaw, 0.3);


        matrices.push();
        model.customRotate(entity, entity.propellerRotation, entity.smoothPitch, entity.smoothYaw);
        IAccessoryRenderer accessoryRenderer = entity.getAccessory() != null ? AcdronesClient.accessoryRenderers.get(entity.getAccessory().getClass()) : null;
        if(accessoryRenderer != null) {
            accessoryRenderer.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        }
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
        matrices.translate(0, -1.5, 0);

        this.base.render(matrices, vertexConsumers.getBuffer(TEXTURE), light, OverlayTexture.DEFAULT_UV);
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(ComputerDroneEntity entity) {
        return null;
    }
}
