package net.blay09.mods.excompressum.client.render.tile;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.excompressum.api.SieveModelBounds;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.block.HeavySieveBlock;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.client.render.RenderUtils;
import net.blay09.mods.excompressum.client.render.model.TinyHumanModel;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.tile.AutoSieveTileEntityBase;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.Map;

public class RenderAutoSieve extends TileEntityRenderer<AutoSieveTileEntityBase> {

    private final TinyHumanModel biped = new TinyHumanModel();
    private final boolean isHeavy;
    private BlockState sieveState;

    public RenderAutoSieve(TileEntityRendererDispatcher dispatcher, boolean isHeavy) {
        super(dispatcher);
        this.isHeavy = isHeavy;
    }

    @Override
    public void render(AutoSieveTileEntityBase tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (!tileEntity.hasWorld() || tileEntity.isUgly()) {
            return;
        }
        if (sieveState == null) {
            sieveState = ModBlocks.heavySieve.getDefaultState();
            if (!isHeavy) {
                sieveState = ExRegistro.getSieveRenderState();
            }
        }

        Minecraft mc = Minecraft.getInstance();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder renderer = tessellator.getBuffer();

        RenderHelper.disableStandardItemLighting();

        matrixStack.push();
        matrixStack.translate(0.5f, 0f, 0.5f);
        matrixStack.rotate(new Quaternion(0f, RenderUtils.getRotationAngle(tileEntity.getFacing()), 0f, true));

        // Render the tiny human
        matrixStack.push();
        matrixStack.rotate(new Quaternion(0, 90, 0, true));
        matrixStack.rotate(new Quaternion(180, 0, 0, true));
        matrixStack.translate(0, -1.2f, 0.25f);
        matrixStack.scale(0.75f, 0.75f, 0.75f);
        bindPlayerTexture(tileEntity.getCustomSkin());
        biped.renderAll(tileEntity, partialTicks);
        matrixStack.pop();

        // Sieve & Content
        matrixStack.push();
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        matrixStack.translate(-0.25f, 0f, -0.5f);

        // Render the sieve
        matrixStack.push();
        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        mc.textureManager.bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
        matrixStack.translate(0f, 0.01f, 0f);
        RenderUtils.renderBlockWithTranslate(mc, sieveState, tileEntity.getWorld(), tileEntity.getPos(), renderer);
        tessellator.draw();
        matrixStack.pop();

        SieveModelBounds bounds = ExRegistro.getSieveBounds();
        if (isHeavy) {
            bounds = HeavySieveBlock.SIEVE_BOUNDS;
        }

        // Render the sieve mesh
        ItemStack meshStack = tileEntity.getMeshStack();
        if (!meshStack.isEmpty()) {
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
        if (!currentStack.isEmpty()) {
            BlockState contentState = StupidUtils.getStateFromItemStack(currentStack);
            if (contentState != null) {
                float progress = tileEntity.getProgress();
                renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
                mc.textureManager.bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
                matrixStack.push();
                matrixStack.translate(bounds.contentOffset, bounds.meshY, bounds.contentOffset);
                matrixStack.scale(bounds.contentScaleXZ, bounds.contentBaseScaleY - progress * bounds.contentBaseScaleY, bounds.contentScaleXZ);
                RenderUtils.renderBlockWithTranslate(mc, contentState, tileEntity.getWorld(), tileEntity.getPos(), renderer);
                tessellator.draw();
                matrixStack.pop();
            }
        }
        matrixStack.pop();

        matrixStack.pop();

        RenderHelper.enableStandardItemLighting();
    }

    private void bindPlayerTexture(@Nullable GameProfile customSkin) {
        ResourceLocation resourceLocation = DefaultPlayerSkin.getDefaultSkinLegacy();
        final Minecraft mc = Minecraft.getInstance();
        if (customSkin != null) {
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = mc.getSkinManager().loadSkinFromCache(customSkin);
            if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                resourceLocation = mc.getSkinManager().loadSkin(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
            }
        }
        mc.textureManager.bindTexture(resourceLocation);
    }

    public static RenderAutoSieve normal(TileEntityRendererDispatcher dispatcher) {
        return new RenderAutoSieve(dispatcher, false);
    }

    public static RenderAutoSieve heavy(TileEntityRendererDispatcher dispatcher) {
        return new RenderAutoSieve(dispatcher, true);
    }
}
