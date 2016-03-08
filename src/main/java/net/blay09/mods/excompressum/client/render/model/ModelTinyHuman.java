package net.blay09.mods.excompressum.client.render.model;

import net.minecraft.client.model.ModelBiped;
import org.lwjgl.opengl.GL11;

public class ModelTinyHuman extends ModelBiped {

    private float swingTimer;

    public ModelTinyHuman() {
        isChild = true;
    }

    public void renderAll(boolean active, float speedBoost) {
        if(active) {
            swingTimer += 0.05f * (Math.max(1f, speedBoost / 2f));
            bipedRightArm.rotateAngleX = swingTimer * (int) (Math.PI * 2);
        }
        float f = 0.0625f;
        float scale = 2f;
        GL11.glPushMatrix();
        GL11.glScalef(1.5f / scale, 1.5f / scale, 1.5f / scale);
        GL11.glTranslatef(0f, 16f * f, 0f);
        bipedHead.render(f);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef(1f / scale, 1f / scale, 1f / scale);
        GL11.glTranslatef(0f, 24f * f, 0f);
        bipedBody.render(f);
        bipedRightArm.render(f);
        bipedLeftArm.render(f);
        bipedRightLeg.render(f);
        bipedLeftLeg.render(f);
        bipedHeadwear.render(f);
        GL11.glPopMatrix();
    }

}
