package net.blay09.mods.excompressum.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.vertex.MatrixApplyingVertexBuilder;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.client.render.BlockRenderUtils;
import net.blay09.mods.excompressum.client.render.RenderUtils;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.tile.AutoHammerTileEntity;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.Random;

public class AutoHammerRenderer extends TileEntityRenderer<AutoHammerTileEntity> {

    private static final Random random = new Random();

    private final boolean isCompressed;

    private ItemStack hammerItemStack = ItemStack.EMPTY;

    public AutoHammerRenderer(TileEntityRendererDispatcher dispatcher, boolean isCompressed) {
        super(dispatcher);
        this.isCompressed = isCompressed;
    }

    @Override
    public void render(AutoHammerTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (!tileEntity.hasWorld() || tileEntity.isUgly()) {
            return;
        }

        if (hammerItemStack.isEmpty()) {
            if (isCompressed) {
                hammerItemStack = new ItemStack(ModItems.compressedHammerDiamond);
            } else {
                hammerItemStack = ExNihilo.getInstance().getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_DIAMOND);
                if (hammerItemStack.isEmpty()) {
                    hammerItemStack = new ItemStack(Items.COD); // This should never happen
                }
            }
        }

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        matrixStack.push();
        matrixStack.translate(0.5f, 0f, 0.5f);
        matrixStack.rotate(new Quaternion(0f, RenderUtils.getRotationAngle(tileEntity.getFacing()), 0f, true));

        if (tileEntity.shouldAnimate()) {
            tileEntity.hammerAngle += 0.4f * partialTicks;
        }

        // Render the hammers
        matrixStack.push();
        matrixStack.rotate(new Quaternion(0, 180f, 0f, true));
        matrixStack.rotate(new Quaternion(0, 0, (float) Math.sin(tileEntity.hammerAngle) * 15, true));
        matrixStack.translate(0.15f, 0.6f, 0f);
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        itemRenderer.renderItem(hammerItemStack, ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStack, bufferIn);

        ItemStack firstHammer = tileEntity.getUpgradeStack(0);
        if (!firstHammer.isEmpty()) {
            matrixStack.push();
            matrixStack.translate(0f, 0f, 0.33f);
            matrixStack.rotate(new Quaternion(0f, -10, 0, true));
            itemRenderer.renderItem(firstHammer, ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStack, bufferIn);
            matrixStack.pop();
        }

        ItemStack secondHammer = tileEntity.getUpgradeStack(1);
        if (!secondHammer.isEmpty()) {
            matrixStack.push();
            matrixStack.translate(0f, 0f, -0.33f);
            matrixStack.rotate(new Quaternion(0f, 10, 0, true));
            itemRenderer.renderItem(secondHammer, ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStack, bufferIn);
            matrixStack.pop();
        }

        matrixStack.pop();

        ItemStack currentStack = tileEntity.getCurrentStack();
        if (!currentStack.isEmpty()) {
            BlockState contentState = StupidUtils.getStateFromItemStack(currentStack);
            if (contentState != null) {
                matrixStack.push();
                matrixStack.translate(-0.09375f, 0.0625f, -0.25);
                matrixStack.scale(0.5f, 0.5f, 0.5f);
                BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
                dispatcher.renderBlock(contentState, matrixStack, bufferIn, combinedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);

                if (tileEntity.getProgress() > 0f) {
                    int blockDamage = Math.min(9, (int) (tileEntity.getProgress() * 9f));
                    BlockRenderUtils.renderBlockBreak(contentState, matrixStack, bufferIn, combinedLightIn, combinedOverlayIn, blockDamage + 1);
                }

                matrixStack.pop();
            }
        }

        matrixStack.pop();
    }

    public static AutoHammerRenderer normal(TileEntityRendererDispatcher dispatcher) {
        return new AutoHammerRenderer(dispatcher, false);
    }

    public static AutoHammerRenderer compressed(TileEntityRendererDispatcher dispatcher) {
        return new AutoHammerRenderer(dispatcher, true);
    }

}
