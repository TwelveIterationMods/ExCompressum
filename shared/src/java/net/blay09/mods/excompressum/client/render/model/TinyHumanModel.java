package net.blay09.mods.excompressum.client.render.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blay09.mods.excompressum.block.entity.AbstractAutoSieveBlockEntity;
import net.blay09.mods.excompressum.block.entity.SieveAnimationType;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.LivingEntity;

public class TinyHumanModel extends PlayerModel<LivingEntity> {

    public TinyHumanModel(boolean smallArms) {
        super(0f, smallArms);
        isChild = true;
    }

    public void animate(AbstractAutoSieveBlockEntity tileEntity, float partialTicks) {
        if (tileEntity.getAnimationType() == SieveAnimationType.MAGIC) {
            if (tileEntity.shouldAnimate()) {
                tileEntity.armAngle += partialTicks * 0.05f;

                float base = (float) Math.toRadians(280);
                bipedRightArm.rotateAngleX = (float) (base + Math.sin(tileEntity.armAngle) * 0.1f);
                bipedLeftArm.rotateAngleX = (float) (base + Math.cos(tileEntity.armAngle) * 0.1f);
            } else {
                bipedRightArm.rotateAngleX = 0;
                bipedLeftArm.rotateAngleX = 0;
            }
        } else {
            if (tileEntity.shouldAnimate()) {
                tileEntity.armAngle += 0.5f * (Math.max(1f, tileEntity.getSpeedMultiplier() / 4f)) * partialTicks;
                bipedRightArm.rotateAngleX = tileEntity.armAngle;
            }
        }
    }

    @Override
    public void render(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        float scale = 0.0625f;

        matrixStack.pushPose();

        matrixStack.scale(0.75F, 0.75F, 0.75F);
        matrixStack.translate(0.0F, 16.0F * scale, 0.0F);
        this.bipedHead.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        matrixStack.popPose();
        matrixStack.pushPose();
        matrixStack.scale(0.5F, 0.5F, 0.5F);
        matrixStack.translate(0.0F, 24.0F * scale, 0.0F);
        this.bipedBody.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.bipedRightArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.bipedLeftArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.bipedRightLeg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.bipedLeftLeg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.bipedHeadwear.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);

        matrixStack.popPose();
    }

}
