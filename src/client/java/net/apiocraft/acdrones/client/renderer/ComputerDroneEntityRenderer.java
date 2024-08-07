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

public class ComputerDroneEntityRenderer extends EntityRenderer<ComputerDroneEntity> {
    private final ModelPart base;
    ComputerDroneEntityModel model;
    private float rotationCounter;

    private static final RenderLayer TEXTURE = RenderLayer.getEntityCutout(Identifier.of(Acdrones.MOD_ID, "textures/entity/drone.png"));
    public ComputerDroneEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.base = ComputerDroneEntityModel.getTexturedModelData().createModel();
        model = new ComputerDroneEntityModel(base);

    }

    @Override
    public void render(ComputerDroneEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        rotationCounter += 0.5 * tickDelta;
        model.customRotate(entity, rotationCounter);

        matrices.push();
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
