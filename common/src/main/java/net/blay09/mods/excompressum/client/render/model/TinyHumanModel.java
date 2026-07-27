package net.blay09.mods.excompressum.client.render.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blay09.mods.excompressum.block.entity.SieveAnimationType;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;

public class TinyHumanModel extends PlayerModel {

    public static class TinyHumanRenderState extends AvatarRenderState {
        public SieveAnimationType animationType = SieveAnimationType.DEFAULT;
        public float armAngle;
    }

    public TinyHumanModel(ModelPart modelPart, boolean smallArms) {
        super(modelPart, smallArms);
    }

    @Override
    public void setupAnim(AvatarRenderState renderState) {
        super.setupAnim(renderState);

        if (!(renderState instanceof TinyHumanRenderState tinyHumanRenderState)) {
            return;
        }

        if (tinyHumanRenderState.animationType == SieveAnimationType.MAGIC) {
            float base = (float) Math.toRadians(280);
            rightArm.xRot = (float) (base + Math.sin(tinyHumanRenderState.armAngle) * 0.1f);
            leftArm.xRot = (float) (base + Math.cos(tinyHumanRenderState.armAngle) * 0.1f);
        } else {
            rightArm.xRot = tinyHumanRenderState.armAngle;
            leftArm.xRot = 0f;
        }
    }

    public void render(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        float scale = 0.0625f;

        poseStack.pushPose();

        poseStack.scale(0.75F, 0.75F, 0.75F);
        poseStack.translate(0.0F, 16.0F * scale, 0.0F);
        this.head.render(poseStack, buffer, packedLight, packedOverlay, color);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.translate(0.0F, 24.0F * scale, 0.0F);
        this.body.render(poseStack, buffer, packedLight, packedOverlay, color);
        this.rightArm.render(poseStack, buffer, packedLight, packedOverlay, color);
        this.leftArm.render(poseStack, buffer, packedLight, packedOverlay, color);
        this.rightLeg.render(poseStack, buffer, packedLight, packedOverlay, color);
        this.leftLeg.render(poseStack, buffer, packedLight, packedOverlay, color);
        this.hat.render(poseStack, buffer, packedLight, packedOverlay, color);

        poseStack.popPose();
    }

}
