package net.blay09.mods.excompressum.client;

import exnihilo.blocks.models.ModelCrucible;
import exnihilo.blocks.renderers.blockItems.ItemRenderCrucible;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class ItemRenderWoodenCrucible implements IItemRenderer {

    private ModelCrucible model;

    public ItemRenderWoodenCrucible(ModelCrucible model) {
        this.model = model;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type != ItemRenderType.FIRST_PERSON_MAP;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return type != ItemRenderType.FIRST_PERSON_MAP;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack itemStack, Object... data) {
        GL11.glPushMatrix();
        GL11.glScalef(-1f, -1f, 1f);

        switch (type) {
            case EQUIPPED:
                GL11.glTranslatef(-0.5f, -1.5f, 0.5f);
                break;
            case EQUIPPED_FIRST_PERSON:
                GL11.glTranslatef(0f, -1.6f, 0.6f);
                break;
            case ENTITY:
                GL11.glTranslatef(0f, -1.0f, 0f);
                break;
            case INVENTORY:
                GL11.glTranslatef(0f, -1.0f, 0f);
                break;
            default:
                GL11.glTranslatef(0f, 0f, 0f);
                break;
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(RenderWoodenCrucible.texture[itemStack.getItemDamage()]);
        model.simpleRender(0.0625f);

        GL11.glPopMatrix();
    }

}
