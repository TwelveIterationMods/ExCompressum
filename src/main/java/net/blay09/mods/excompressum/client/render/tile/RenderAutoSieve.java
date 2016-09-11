package net.blay09.mods.excompressum.client.render.tile;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.block.BlockAutoSieveBase;
import net.blay09.mods.excompressum.client.render.RenderUtils;
import net.blay09.mods.excompressum.client.render.model.ModelTinyHuman;
import net.blay09.mods.excompressum.compat.SieveModelBounds;
import net.blay09.mods.excompressum.registry.ExNihiloProvider;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.tile.TileEntityAutoSieveBase;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.util.Map;

public class RenderAutoSieve extends TileEntitySpecialRenderer<TileEntityAutoSieveBase> {

    private final ModelTinyHuman biped = new ModelTinyHuman();
    private final boolean isHeavy;
    private IBlockState sieveState;

    public RenderAutoSieve(boolean isHeavy) {
        this.isHeavy = isHeavy;
    }

    @Override
    public void renderTileEntityAt(TileEntityAutoSieveBase tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        if(!tileEntity.hasWorldObj()) {
            return;
        }
        IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
        if(!(state.getBlock() instanceof BlockAutoSieveBase)) {
            return;
        }
        if(sieveState == null) {
            sieveState = ModBlocks.heavySieve.getDefaultState();
            if(!isHeavy) {
                ItemStack nihiloSieve = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.SIEVE);
                if(nihiloSieve != null) {
                    sieveState = Block.getBlockFromItem(nihiloSieve.getItem()).getDefaultState();
                }
            }
        }

        Minecraft mc = Minecraft.getMinecraft();
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer renderer = tessellator.getBuffer();

        RenderHelper.disableStandardItemLighting();

        GlStateManager.pushMatrix();
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.translate((float) x + 0.5f, (float) y, (float) z + 0.5f);
        GlStateManager.rotate(getRotationAngle(state.getValue(BlockAutoSieveBase.FACING)), 0f, 1f, 0f);

        // Render the tiny human
        GlStateManager.pushMatrix();
        GlStateManager.rotate(90, 0, 1, 0);
        GlStateManager.rotate(180, 1, 0, 0);
        GlStateManager.translate(0, -1.2f, 0.25f);
        GlStateManager.scale(0.75f, 0.75f, 0.75f);
        bindPlayerTexture(tileEntity.getCustomSkin());
        biped.renderAll(tileEntity.getArmAngle());
        GlStateManager.popMatrix();

        // Sieve & Content
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.translate(-0.25f, 0f, -0.5f);

        // Render the sieve
        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        mc.getBlockRendererDispatcher().renderBlock(sieveState, new BlockPos(0, 0, 0), tileEntity.getWorld(), renderer);
        tessellator.draw();

        SieveModelBounds bounds = ExRegistro.getSieveBounds();

        // Render the sieve mesh
        ItemStack meshStack = tileEntity.getMeshStack();
        if (meshStack != null) {
            SieveMeshRegistryEntry sieveMesh = SieveMeshRegistry.getEntry(meshStack);
            if (sieveMesh != null) {
                renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
                TextureAtlasSprite sprite = sieveMesh.getSpriteLocation() != null ? mc.getTextureMapBlocks().getTextureExtry(sieveMesh.getSpriteLocation().toString()) : null;
                if (sprite == null) {
                    sprite = mc.getTextureMapBlocks().getMissingSprite();
                }
                int brightness = tileEntity.getWorld().getCombinedLight(tileEntity.getPos(), 0);
                float meshXZ = bounds.contentOffset;
                float meshXZ2 = 1f - meshXZ;
                float meshY = bounds.meshY - 0.025f;
                RenderUtils.renderQuadUp(renderer, meshXZ, meshY, meshXZ, meshXZ2, meshY, meshXZ2, 0xFFFFFFFF, brightness, sprite);
                tessellator.draw();
            }
        }

        // Render the content
        ItemStack currentStack = tileEntity.getCurrentStack();
        if (currentStack != null) {
            IBlockState contentState = StupidUtils.getStateFromItemStack(currentStack);
            //noinspection ConstantConditions /// Forge needs @Nullable
            if(contentState != null) {
                float progress = tileEntity.getProgress();
                renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
                mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                GlStateManager.pushMatrix();
                GlStateManager.translate(bounds.contentOffset, bounds.meshY, bounds.contentOffset);
                GlStateManager.scale(bounds.contentScaleXZ, bounds.contentBaseScaleY - progress * bounds.contentBaseScaleY, bounds.contentScaleXZ);
                mc.getBlockRendererDispatcher().renderBlock(contentState, new BlockPos(0, 0, 0), tileEntity.getWorld(), renderer);
                tessellator.draw();
                GlStateManager.popMatrix();
            }
        }
        GlStateManager.popMatrix();

        GlStateManager.popMatrix();

        RenderHelper.enableStandardItemLighting();
    }

    private void bindPlayerTexture(GameProfile customSkin) {
        ResourceLocation resourceLocation = DefaultPlayerSkin.getDefaultSkinLegacy();
        if (customSkin != null) {
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = Minecraft.getMinecraft().getSkinManager().loadSkinFromCache(customSkin);
            if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                resourceLocation = Minecraft.getMinecraft().getSkinManager().loadSkin(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
            }
        }
        bindTexture(resourceLocation);
    }

    private float getRotationAngle(EnumFacing facing) {
        switch(facing) {
            case NORTH:
                return 0;
            case EAST:
                return -90;
            case SOUTH:
                return 180;
            case WEST:
                return 90;
            default:
                return -90;
        }
    }
}
