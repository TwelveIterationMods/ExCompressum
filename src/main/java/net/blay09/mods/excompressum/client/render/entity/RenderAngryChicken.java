package net.blay09.mods.excompressum.client.render.entity;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.entity.EntityAngryChicken;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderAngryChicken extends RenderLiving<EntityAngryChicken> {
	private static final ResourceLocation chickenTextures = new ResourceLocation(ExCompressum.MOD_ID, "textures/entity/angry_chicken.png");

	public RenderAngryChicken(RenderManager renderManager, ModelBase modelBase, float shadowSize) {
		super(renderManager, modelBase, shadowSize);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityAngryChicken entity) {
		return chickenTextures;
	}

}