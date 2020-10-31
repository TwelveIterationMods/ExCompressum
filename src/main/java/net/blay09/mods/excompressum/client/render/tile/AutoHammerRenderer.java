package net.blay09.mods.excompressum.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.vertex.MatrixApplyingVertexBuilder;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.client.render.RenderUtils;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.tile.AutoHammerTileEntity;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverride;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import org.lwjgl.opengl.GL11;

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
                hammerItemStack = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_DIAMOND);
                if (hammerItemStack.isEmpty()) {
                    hammerItemStack = new ItemStack(Items.COD); // This should never happen
                }
            }
        }

        Minecraft mc = Minecraft.getInstance();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder renderer = tessellator.getBuffer();

        RenderHelper.disableStandardItemLighting();

        matrixStack.push();
        matrixStack.translate(0.5f, 0f, +0.5f);
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
            matrixStack.rotate(new Quaternion(0f, 170f, 0f, true));
            itemRenderer.renderItem(firstHammer, ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStack, bufferIn);
            matrixStack.pop();
        }

        ItemStack secondHammer = tileEntity.getUpgradeStack(1);
        if (!secondHammer.isEmpty()) {
            matrixStack.push();
            matrixStack.translate(0f, 0f, -0.33f);
            matrixStack.rotate(new Quaternion(0f, 190f, 0f, true));
            itemRenderer.renderItem(secondHammer, ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStack, bufferIn);
            matrixStack.pop();
        }

        matrixStack.pop();

        RenderHelper.disableStandardItemLighting();

        ItemStack currentStack = tileEntity.getCurrentStack();
        if (!currentStack.isEmpty()) {
            BlockState contentState = StupidUtils.getStateFromItemStack(currentStack);
            if (contentState != null) {

                // Build a matrix for the destroy stages because it does some weird stuff with MatrixApplyingVertexBuilder that I don't understand but it kinda works
                matrixStack.push();
                matrixStack.translate(-0.09375f, 0.0625f, -0.25);
                // I have no idea what these numbers mean. This is the result of one hour of trial-and-error, trying to get the destroy_stage pixels to overlay as well as possible. It's still not perfect.
                matrixStack.scale(1f,1f , 1.4375f);
                MatrixStack.Entry matrix = matrixStack.getLast();
                matrixStack.pop();

                matrixStack.push();
                matrixStack.translate(-0.09375f, 0.0625f, -0.25);
                matrixStack.scale(0.5f, 0.5f, 0.5f);
                World world = tileEntity.getWorld();
                BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
                dispatcher.renderBlock(contentState, matrixStack, bufferIn, combinedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);

                int blockDamage = Math.min(9, (int) (tileEntity.getProgress() * 9f));
                RenderTypeBuffers renderTypeBuffers = Minecraft.getInstance().getRenderTypeBuffers();
                IVertexBuilder damageBuffer = new MatrixApplyingVertexBuilder(renderTypeBuffers.getCrumblingBufferSource().getBuffer(ModelBakery.DESTROY_RENDER_TYPES.get(blockDamage)), matrix.getMatrix(), matrix.getNormal());
                dispatcher.renderBlockDamage(contentState, tileEntity.getPos(), world, matrixStack, damageBuffer, EmptyModelData.INSTANCE);
                matrixStack.pop();
            }
        }

        matrixStack.pop();

        RenderHelper.enableStandardItemLighting();
    }

    public static AutoHammerRenderer normal(TileEntityRendererDispatcher dispatcher) {
        return new AutoHammerRenderer(dispatcher, false);
    }

    public static AutoHammerRenderer compressed(TileEntityRendererDispatcher dispatcher) {
        return new AutoHammerRenderer(dispatcher, true);
    }

}
