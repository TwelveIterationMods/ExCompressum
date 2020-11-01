package net.blay09.mods.excompressum.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.entity.AngryChickenEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.ChickenModel;
import net.minecraft.util.ResourceLocation;

public class AngryChickenRenderer extends MobRenderer<AngryChickenEntity, ChickenModel<AngryChickenEntity>> {
    private static final ResourceLocation chickenTextures = new ResourceLocation(ExCompressum.MOD_ID, "textures/entity/angry_chicken.png");

    public AngryChickenRenderer(EntityRendererManager renderManagerIn, ChickenModel<AngryChickenEntity> entityModelIn, float shadowSizeIn) {
        super(renderManagerIn, entityModelIn, shadowSizeIn);
    }

    @Override
    public ResourceLocation getEntityTexture(AngryChickenEntity entity) {
        return chickenTextures;
    }

    @Override
    public void render(AngryChickenEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        float scale = entityIn.getRenderScale();
        matrixStackIn.scale(scale, scale, scale);
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.pop();
    }


}
