package net.blay09.mods.excompressum.client.render.model;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;

public class ModelTinyHuman extends ModelPlayer {

	public ModelTinyHuman() {
		super(0f, false);
		isChild = true;
	}

	public void renderAll(float armAngle) {
		bipedRightArm.rotateAngleX = armAngle * (int) (Math.PI * 2);

		float scale = 0.0625f;

		GlStateManager.pushMatrix();

		GlStateManager.scale(0.75F, 0.75F, 0.75F);
		GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
		this.bipedHead.render(scale);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
		this.bipedBody.render(scale);
		this.bipedRightArm.render(scale);
		this.bipedLeftArm.render(scale);
		this.bipedRightLeg.render(scale);
		this.bipedLeftLeg.render(scale);
		this.bipedHeadwear.render(scale);

		GlStateManager.popMatrix();
	}

}
