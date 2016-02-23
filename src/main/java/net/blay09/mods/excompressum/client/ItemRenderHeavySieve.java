package net.blay09.mods.excompressum.client;

import exnihilo.blocks.BlockSieve;
import exnihilo.blocks.models.ModelSieve;
import exnihilo.blocks.models.ModelSieveMesh;
import exnihilo.blocks.renderers.blockItems.ItemRenderSieve;
import net.blay09.mods.excompressum.block.BlockHeavySieve;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class ItemRenderHeavySieve implements IItemRenderer {

    private final ModelSieve model;
    private final ModelSieveMesh mesh;

    public ItemRenderHeavySieve(ModelSieve model, ModelSieveMesh mesh) {
        this.model = model;
        this.mesh = mesh;
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
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        renderTable(type, item);
        renderMesh(type);
    }

    private void renderTable(ItemRenderType type, ItemStack item) {
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
                GL11.glTranslatef(0f, -1f, 0f);
                break;
            case INVENTORY:
                GL11.glTranslatef(0f, -1f, 0f);
                break;
            default:
                GL11.glTranslatef(0f, 0f, 0f);
                break;
        }

        bindTexture(item.getItemDamage());
        model.simpleRender(0.0625F);

        GL11.glPopMatrix();
    }

    private void renderMesh(ItemRenderType type) {
        GL11.glPushMatrix();
        GL11.glScalef(-1f, -1f, 1f);

        switch (type) {
            case EQUIPPED:
                GL11.glTranslatef(-0.5f, -0.69f, 0.5f);
                break;
            case EQUIPPED_FIRST_PERSON:
                GL11.glTranslatef(-0.5f, -0.79f, 0.5f);
                break;
            case ENTITY:
                GL11.glTranslatef(0f, -0.2f, 0f);
                break;
            case INVENTORY:
                GL11.glTranslatef(0f, -0.2f, 0f);
                break;
            default:
                GL11.glTranslatef(0f, -0.2f, 0f);
                break;
        }

        bindMeshTexture();
        mesh.render(BlockHeavySieve.meshIcon);

        GL11.glPopMatrix();
    }

    private void bindTexture(int metadata) {
        if (metadata >= 0) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(RenderHeavySieve.textures[metadata]);
        }
    }

    private void bindMeshTexture() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
    }
}
