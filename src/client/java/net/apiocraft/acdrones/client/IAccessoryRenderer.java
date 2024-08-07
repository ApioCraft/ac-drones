package net.apiocraft.acdrones.client;

import net.apiocraft.acdrones.entities.ComputerDroneEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public interface IAccessoryRenderer {
    void render(ComputerDroneEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);
}
