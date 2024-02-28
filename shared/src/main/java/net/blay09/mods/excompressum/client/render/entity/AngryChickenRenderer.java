package net.blay09.mods.excompressum.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.entity.AngryChickenEntity;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class AngryChickenRenderer extends MobRenderer<AngryChickenEntity, ChickenModel<AngryChickenEntity>> {
    private static final ResourceLocation chickenTextures = new ResourceLocation(ExCompressum.MOD_ID, "textures/entity/angry_chicken.png");

    public AngryChickenRenderer(EntityRendererProvider.Context context, ChickenModel<AngryChickenEntity> model, float shadowSize) {
        super(context, model, shadowSize);
    }

    @Override
    public ResourceLocation getTextureLocation(AngryChickenEntity entity) {
        return chickenTextures;
    }

    @Override
    public void render(AngryChickenEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLightIn) {
        poseStack.pushPose();
        float scale = entityIn.getScale();
        poseStack.scale(scale, scale, scale);
        super.render(entityIn, entityYaw, partialTicks, poseStack, buffer, packedLightIn);
        poseStack.popPose();
    }


}
