package net.blay09.mods.excompressum.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.excompressum.client.ModModels;
import net.blay09.mods.excompressum.client.render.RenderUtils;
import net.blay09.mods.excompressum.tile.WoodenCrucibleTileEntity;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class WoodenCrucibleRenderer extends TileEntityRenderer<WoodenCrucibleTileEntity> {

    private final Random random = new Random();

    public WoodenCrucibleRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(WoodenCrucibleTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        World world = tileEntity.getWorld();
        if (world == null) {
            return;
        }

        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();

        ItemStack outputStack = tileEntity.getItemHandler().getStackInSlot(0);
        if (!outputStack.isEmpty()) {
            BlockState outputState = StupidUtils.getStateFromItemStack(outputStack);
            if (outputState != null) {
                matrixStack.push();
                matrixStack.translate(0.0625f, 0.2f, 0.0625f);
                matrixStack.scale(0.875f, 0.75f, 0.875f);
                dispatcher.renderBlock(outputState, matrixStack, buffer, combinedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);
                matrixStack.pop();
            }
        }

        FluidStack fluidStack = tileEntity.getFluidTank().getFluid();
        if (!fluidStack.isEmpty()) {
            matrixStack.push();
            float fillLevel = (float) fluidStack.getAmount() / (float) tileEntity.getFluidTank().getCapacity();
            matrixStack.translate(0f, fillLevel * 11 / 16f, 0f);
            int color = fluidStack.getFluid().getAttributes().getColor(world, tileEntity.getPos());
            float red = (float)(color >> 16 & 255) / 255.0F;
            float green = (float)(color >> 8 & 255) / 255.0F;
            float blue = (float)(color & 255) / 255.0F;
            dispatcher.getBlockModelRenderer().renderModel(matrixStack.getLast(), buffer.getBuffer(RenderType.getTranslucent()), null, ModModels.woodenCrucibleLiquid, red, green, blue, combinedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);
            matrixStack.pop();
        }

        int solidVolume = tileEntity.getSolidVolume();
        if (solidVolume > 0) {
            matrixStack.push();
            matrixStack.translate(0.0625f, 0.251f, 0.0625f);
            matrixStack.scale(0.875f, (float) (0.71 * (float) solidVolume / (float) tileEntity.getSolidCapacity()), 0.875f);
            BlockState solidState = Blocks.DARK_OAK_LEAVES.getDefaultState();
            dispatcher.renderModel(solidState, tileEntity.getPos(), tileEntity.getWorld(), matrixStack, buffer.getBuffer(RenderType.getTranslucent()), false, random, EmptyModelData.INSTANCE);
            matrixStack.pop();
        }
    }
}
