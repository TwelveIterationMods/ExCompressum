package net.blay09.mods.excompressum.client.render.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blay09.mods.excompressum.block.entity.AbstractAutoSieveBlockEntity;
import net.blay09.mods.excompressum.block.entity.SieveAnimationType;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

public class TinyHumanModel extends PlayerModel<LivingEntity> {

    public TinyHumanModel(ModelPart modelPart, boolean smallArms) {
        super(modelPart, smallArms);
    }

    public void animate(AbstractAutoSieveBlockEntity tileEntity, float partialTicks) {
        if (tileEntity.getAnimationType() == SieveAnimationType.MAGIC) {
            if (tileEntity.shouldAnimate()) {
                tileEntity.armAngle += partialTicks * 0.05f;

                float base = (float) Math.toRadians(280);
                rightArm.xRot = (float) (base + Math.sin(tileEntity.armAngle) * 0.1f);
                leftArm.xRot = (float) (base + Math.cos(tileEntity.armAngle) * 0.1f);
            } else {
                rightArm.xRot = 0;
                leftArm.xRot = 0;
            }
        } else {
            if (tileEntity.shouldAnimate()) {
                tileEntity.armAngle += 0.5f * (Math.max(1f, tileEntity.getSpeedMultiplier() / 4f)) * partialTicks;
                rightArm.xRot = tileEntity.armAngle;
            }
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        float scale = 0.0625f;

        poseStack.pushPose();

        poseStack.scale(0.75F, 0.75F, 0.75F);
        poseStack.translate(0.0F, 16.0F * scale, 0.0F);
        this.head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.translate(0.0F, 24.0F * scale, 0.0F);
        this.body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.rightArm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.leftArm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.rightLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.leftLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.hat.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);

        poseStack.popPose();
    }

}
