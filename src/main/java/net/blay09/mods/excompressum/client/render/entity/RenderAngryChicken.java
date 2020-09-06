package net.blay09.mods.excompressum.client.render.entity;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.entity.AngryChickenEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.ChickenModel;
import net.minecraft.util.ResourceLocation;

public class RenderAngryChicken extends MobRenderer<AngryChickenEntity, ChickenModel<AngryChickenEntity>> {
    private static final ResourceLocation chickenTextures = new ResourceLocation(ExCompressum.MOD_ID, "textures/entity/angry_chicken.png");

    public RenderAngryChicken(EntityRendererManager renderManagerIn, ChickenModel<AngryChickenEntity> entityModelIn, float shadowSizeIn) {
        super(renderManagerIn, entityModelIn, shadowSizeIn);
    }

    @Override
    public ResourceLocation getEntityTexture(AngryChickenEntity entity) {
        return chickenTextures;
    }

}
