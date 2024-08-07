package net.apiocraft.acdrones.client.renderer;

import net.apiocraft.acdrones.Acdrones;
import net.apiocraft.acdrones.accessories.droneClaw.DroneClawAccessory;
import net.apiocraft.acdrones.client.IAccessoryRenderer;
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
    public DroneClawAccessoryRenderer() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData Claw = modelPartData.addChild("Claw", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData Claw1 = Claw.addChild("Claw1", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -16F, -1.0F, 16.0F, 16.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        // thats it
        this.base = Claw.createPart(32,32);
    }
    @Override
    public void render(ComputerDroneEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        //this.base.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(Identifier.of(Acdrones.MOD_ID, "textures/gui/dronegui.png"))), light, OverlayTexture.DEFAULT_UV);
        DroneClawAccessory accessory = (DroneClawAccessory) entity.getAccessory();
        if(accessory == null) return;
        //System.out.println(accessory.getCarryData());

        if(accessory.isCarryingBlock()) {
            BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
            BlockModelRenderer modelRenderer = blockRenderManager.getModelRenderer();

            BlockState state = accessory.getCarriedBlock();
            BakedModel model = blockRenderManager.getModel(state);
            //System.out.println("hello");


            matrices.push();
            //matrices.translate(-entity.getBlockX(), -entity.getBlockY(), -entity.getBlockZ());
            //matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(180));
            matrices.translate(-0.5, -0.9, -0.5);
//                modelRenderer.render(
//                        MinecraftClient.getInstance().world,
//                        model,
//                        state,
//                        entity.getBlockPos(),
//                        matrices,
//                        vertexConsumers.getBuffer(RenderLayers.getBlockLayer(state)),
//                        false,
//                        MinecraftClient.getInstance().world.random,
//                        state.getRenderingSeed(new BlockPos(0, 0, 0)),
//                        OverlayTexture.DEFAULT_UV
//                );
            blockRenderManager.renderBlockAsEntity(state, matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV);
            matrices.pop();
        }

    }
}
