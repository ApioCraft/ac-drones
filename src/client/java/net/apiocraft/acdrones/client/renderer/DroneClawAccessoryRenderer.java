package net.apiocraft.acdrones.client.renderer;

import net.apiocraft.acdrones.Acdrones;
import net.apiocraft.acdrones.accessories.droneClaw.DroneClawAccessory;
import net.apiocraft.acdrones.client.IAccessoryRenderer;
import net.apiocraft.acdrones.client.model.DroneClawModel;
import net.apiocraft.acdrones.entities.ComputerDroneEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3i;

public class DroneClawAccessoryRenderer implements IAccessoryRenderer {
    ModelPart base;
    DroneClawModel model;
    public DroneClawAccessoryRenderer() {
        this.base = DroneClawModel.getTexturedModelData().createModel();
        this.model = new DroneClawModel(base);
    }

    @Override
    public void render(ComputerDroneEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        //this.base.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(Identifier.of(Acdrones.MOD_ID, "textures/gui/dronegui.png"))), light, OverlayTexture.DEFAULT_UV);
        DroneClawAccessory accessory = (DroneClawAccessory) entity.getAccessory();
        if(accessory == null) return;
        //System.out.println(accessory.getCarryData());

        model.rotato(accessory);

        matrices.push();
        matrices.translate(0, 0.5, 0);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.getYaw()+90));
        base.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(Identifier.of(Acdrones.MOD_ID, "textures/entity/droneclaw.png"))), light, OverlayTexture.DEFAULT_UV);
        matrices.pop();

        if(accessory.isCarryingBlock()) {
            BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
            BlockModelRenderer modelRenderer = blockRenderManager.getModelRenderer();

            BlockState state = accessory.getCarriedBlock();

            BakedModel model = blockRenderManager.getModel(state);
            //System.out.println("hello");

            matrices.push();
            matrices.translate(-0.5, -0.8-0.5, -0.5);
            blockRenderManager.renderBlockAsEntity(state, matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV);
            matrices.pop();
        }

    }
}
