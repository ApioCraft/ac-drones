package net.apiocraft.acdrones.client.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class DroneClawModel {
    private final ModelPart hand2;
    private final ModelPart hand1;
    private final ModelPart bb_main;
    public DroneClawModel(ModelPart root) {
        this.hand2 = root.getChild("hand2");
        this.hand1 = root.getChild("hand1");
        this.bb_main = root.getChild("bb_main");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData hand2 = modelPartData.addChild("hand2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 11.5F, -2.0F));

        ModelPartData cube_r1 = hand2.addChild("cube_r1", ModelPartBuilder.create().uv(10, 14).cuboid(-0.5F, 0.5F, 4.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -2.0F, -2.618F, 0.0F, 0.0F));

        ModelPartData cube_r2 = hand2.addChild("cube_r2", ModelPartBuilder.create().uv(0, 12).cuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -2.0F, -2.8798F, 0.0F, 0.0F));

        ModelPartData cube_r3 = hand2.addChild("cube_r3", ModelPartBuilder.create().uv(16, 13).cuboid(-2.0F, -0.5F, 6.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 6.0F, -3.1416F, 0.0F, 0.0F));

        ModelPartData hand1 = modelPartData.addChild("hand1", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 11.5F, 2.0F));

        ModelPartData cube_r4 = hand1.addChild("cube_r4", ModelPartBuilder.create().uv(16, 0).cuboid(-0.5F, 0.5F, -8.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 2.0F, 2.618F, 0.0F, 0.0F));

        ModelPartData cube_r5 = hand1.addChild("cube_r5", ModelPartBuilder.create().uv(11, 7).cuboid(-1.0F, -0.5F, -5.0F, 2.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 2.0F, 2.8798F, 0.0F, 0.0F));

        ModelPartData cube_r6 = hand1.addChild("cube_r6", ModelPartBuilder.create().uv(0, 18).cuboid(-2.0F, -0.5F, 0.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 2.0F, -3.1416F, 0.0F, 0.0F));

        ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -14.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.0F))
                .uv(12, 0).cuboid(-1.0F, -16.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 7).cuboid(-2.0F, -17.0F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 32, 32);
    }
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        hand2.render(matrices, vertexConsumer, light, overlay, color);
        hand1.render(matrices, vertexConsumer, light, overlay, color);
        bb_main.render(matrices, vertexConsumer, light, overlay, color);
    }
}
