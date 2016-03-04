package net.blay09.mods.excompressum.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelAutoFrame extends ModelBase {
    public ModelRenderer Bottom;
    public ModelRenderer Top;
    public ModelRenderer Corner1;
    public ModelRenderer Corner2;
    public ModelRenderer Corner3;
    public ModelRenderer Corner4;
    public ModelRenderer Glass;

    public ModelAutoFrame() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.Corner1 = new ModelRenderer(this, 0, 0);
        this.Corner1.setRotationPoint(-8.0F, 1.0F, -8.0F);
        this.Corner1.addBox(0.0F, 0.0F, 0.0F, 1, 14, 1, 0.0F);
        this.Top = new ModelRenderer(this, 0, 0);
        this.Top.setRotationPoint(-8.0F, 15.0F, -8.0F);
        this.Top.addBox(0.0F, 0.0F, 0.0F, 16, 1, 16, 0.0F);
        this.Bottom = new ModelRenderer(this, 0, 0);
        this.Bottom.setRotationPoint(-8.0F, 0.0F, -8.0F);
        this.Bottom.addBox(0.0F, 0.0F, 0.0F, 16, 1, 16, 0.0F);
        this.Corner2 = new ModelRenderer(this, 0, 0);
        this.Corner2.setRotationPoint(7.0F, 1.0F, 7.0F);
        this.Corner2.addBox(0.0F, 0.0F, 0.0F, 1, 14, 1, 0.0F);
        this.Glass = new ModelRenderer(this, 0, 20);
        this.Glass.setRotationPoint(-7.0F, 1.0F, -7.0F);
        this.Glass.addBox(0.0F, 0.0F, 0.0F, 14, 14, 14, 0.0F);
        this.Corner4 = new ModelRenderer(this, 0, 0);
        this.Corner4.setRotationPoint(-8.0F, 1.0F, 7.0F);
        this.Corner4.addBox(0.0F, 0.0F, 0.0F, 1, 14, 1, 0.0F);
        this.Corner3 = new ModelRenderer(this, 0, 0);
        this.Corner3.setRotationPoint(7.0F, 1.0F, -8.0F);
        this.Corner3.addBox(0.0F, 0.0F, 0.0F, 1, 14, 1, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.Corner2.render(f5);
        this.Corner3.render(f5);
        this.Glass.render(f5);
        this.Bottom.render(f5);
        this.Top.render(f5);
        this.Corner4.render(f5);
        this.Corner1.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void renderSolid() {
        float f5 = 0.0625f;
        this.Corner2.render(f5);
        this.Corner3.render(f5);
        this.Bottom.render(f5);
        this.Top.render(f5);
        this.Corner4.render(f5);
        this.Corner1.render(f5);
    }

    public void renderGlass() {
        float f5 = 0.0625f;
        this.Glass.render(f5);
    }
}
