package net.blay09.mods.excompressum.client.render.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.entity.EntityAngryChicken;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderAngryChicken extends RenderLiving {
	private static final ResourceLocation chickenTextures = new ResourceLocation(ExCompressum.MOD_ID, "textures/entity/angry_chicken.png");

	public RenderAngryChicken(ModelBase model, float shadowSize) {
		super(model, shadowSize);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return chickenTextures;
	}

}