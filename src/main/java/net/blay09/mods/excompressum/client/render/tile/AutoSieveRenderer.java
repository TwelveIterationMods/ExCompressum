package net.blay09.mods.excompressum.client.render.tile;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.excompressum.api.SieveModelBounds;
import net.blay09.mods.excompressum.block.HeavySieveBlock;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.client.ModModels;
import net.blay09.mods.excompressum.client.render.RenderUtils;
import net.blay09.mods.excompressum.client.render.model.TinyHumanModel;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.tile.AutoSieveTileEntityBase;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.EmptyModelData;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;

public class AutoSieveRenderer extends TileEntityRenderer<AutoSieveTileEntityBase> {

    private static final Random random = new Random();

    private final TinyHumanModel biped = new TinyHumanModel();
    private final boolean isHeavy;
    private IBakedModel sieveModel;

    public AutoSieveRenderer(TileEntityRendererDispatcher dispatcher, boolean isHeavy) {
        super(dispatcher);
        this.isHeavy = isHeavy;
    }

    @Override
    public void render(AutoSieveTileEntityBase tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        World world = tileEntity.getWorld();
        if (world == null || tileEntity.isUgly()) {
            return;
        }

        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();

        if (sieveModel == null) {
            sieveModel = isHeavy ? dispatcher.getModelForState(ModBlocks.heavySieves[0].getDefaultState()) : ModModels.sieves[0];
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
        biped.render(matrixStack, buffer.getBuffer(RenderType.getEntitySolid(getPlayerSkinTexture(tileEntity.getCustomSkin()))), combinedLightIn, combinedOverlayIn, 1f, 1f, 1f, 1f);
        matrixStack.pop();

        // Sieve & Content
        matrixStack.push();
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        matrixStack.translate(-0.25f, 0f, -0.5f);

        // Render the sieve
        matrixStack.push();
        matrixStack.translate(0f, 0.01f, 0f);
        dispatcher.getBlockModelRenderer().renderModel(world, sieveModel, tileEntity.getBlockState(), tileEntity.getPos(), matrixStack, buffer.getBuffer(RenderType.getTranslucent()), false, random, 0, Integer.MAX_VALUE, EmptyModelData.INSTANCE);
        matrixStack.pop();

        SieveModelBounds bounds = HeavySieveBlock.SIEVE_BOUNDS;

        // Render the sieve mesh
        ItemStack meshStack = tileEntity.getMeshStack();
        if (!meshStack.isEmpty()) {
            dispatcher.getBlockModelRenderer().renderModel(world, ModModels.mesh, tileEntity.getBlockState(), tileEntity.getPos(), matrixStack, buffer.getBuffer(RenderType.getTranslucent()), false, random, 0, Integer.MAX_VALUE, EmptyModelData.INSTANCE);
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
                dispatcher.renderBlock(contentState, matrixStack, buffer, combinedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);
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
