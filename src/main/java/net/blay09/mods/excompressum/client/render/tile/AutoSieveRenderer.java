package net.blay09.mods.excompressum.client.render.tile;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.excompressum.api.SieveModelBounds;
import net.blay09.mods.excompressum.block.HeavySieveBlock;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.client.render.RenderUtils;
import net.blay09.mods.excompressum.client.render.model.TinyHumanModel;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.tile.AutoSieveTileEntityBase;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraftforge.client.model.data.EmptyModelData;

import javax.annotation.Nullable;
import java.util.Map;

public class AutoSieveRenderer extends TileEntityRenderer<AutoSieveTileEntityBase> {

    private final TinyHumanModel biped = new TinyHumanModel();
    private final boolean isHeavy;
    private BlockState sieveState;

    public AutoSieveRenderer(TileEntityRendererDispatcher dispatcher, boolean isHeavy) {
        super(dispatcher);
        this.isHeavy = isHeavy;
    }

    @Override
    public void render(AutoSieveTileEntityBase tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (!tileEntity.hasWorld() || tileEntity.isUgly()) {
            return;
        }

        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();

        if (sieveState == null) {
            sieveState = ModBlocks.heavySieves[0].getDefaultState();
            if (!isHeavy) {
                sieveState = ExRegistro.getSieveRenderState();
            }
        }

        matrixStack.push();
        matrixStack.translate(0.5f, 0f, 0.5f);
        matrixStack.rotate(new Quaternion(0f, RenderUtils.getRotationAngle(tileEntity.getFacing()), 0f, true));

        // Render the tiny human
        matrixStack.push();
        matrixStack.rotate(new Quaternion(0, 90, 0, true));
        matrixStack.rotate(new Quaternion(180, 0, 0, true));
        matrixStack.translate(0, -1.2f, 0.25f);
        matrixStack.scale(0.75f, 0.75f, 0.75f);
        biped.animate(tileEntity, partialTicks);
        biped.render(matrixStack, bufferIn.getBuffer(RenderType.getEntitySolid(getPlayerSkinTexture(tileEntity.getCustomSkin()))), combinedLightIn, combinedOverlayIn, 1f, 1f, 1f, 1f);
        matrixStack.pop();

        // Sieve & Content
        matrixStack.push();
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        matrixStack.translate(-0.25f, 0f, -0.5f);

        // Render the sieve
        matrixStack.push();
        matrixStack.translate(0f, 0.01f, 0f);
        dispatcher.renderBlock(sieveState, matrixStack, bufferIn, combinedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);
        matrixStack.pop();

        SieveModelBounds bounds = ExRegistro.getSieveBounds();
        if (isHeavy) {
            bounds = HeavySieveBlock.SIEVE_BOUNDS;
        }

        // Render the sieve mesh
        ItemStack meshStack = tileEntity.getMeshStack();
        if (!meshStack.isEmpty()) {
            /* TODO SieveMeshRegistryEntry sieveMesh = SieveMeshRegistry.getEntry(meshStack);
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
            }*/
        }

        // Render the content
        ItemStack currentStack = tileEntity.getCurrentStack();
        if (!currentStack.isEmpty()) {
            BlockState contentState = StupidUtils.getStateFromItemStack(currentStack);
            if (contentState != null) {
                float progress = tileEntity.getProgress();
                matrixStack.push();
                matrixStack.translate(bounds.contentOffset, bounds.meshY, bounds.contentOffset);
                matrixStack.scale(bounds.contentScaleXZ, bounds.contentBaseScaleY - progress * bounds.contentBaseScaleY, bounds.contentScaleXZ);
                dispatcher.renderBlock(contentState, matrixStack, bufferIn, combinedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);
                matrixStack.pop();
            }
        }
        matrixStack.pop();

        matrixStack.pop();
    }

    private ResourceLocation getPlayerSkinTexture(@Nullable GameProfile customSkin) {
        ResourceLocation resourceLocation = DefaultPlayerSkin.getDefaultSkinLegacy();
        final Minecraft mc = Minecraft.getInstance();
        if (customSkin != null) {
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = mc.getSkinManager().loadSkinFromCache(customSkin);
            if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                resourceLocation = mc.getSkinManager().loadSkin(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
            }
        }
        return resourceLocation;
    }

    public static AutoSieveRenderer normal(TileEntityRendererDispatcher dispatcher) {
        return new AutoSieveRenderer(dispatcher, false);
    }

    public static AutoSieveRenderer heavy(TileEntityRendererDispatcher dispatcher) {
        return new AutoSieveRenderer(dispatcher, true);
    }
}
