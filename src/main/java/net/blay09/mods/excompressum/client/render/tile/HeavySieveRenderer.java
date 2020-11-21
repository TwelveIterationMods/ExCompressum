package net.blay09.mods.excompressum.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.client.ModModels;
import net.blay09.mods.excompressum.tile.HeavySieveTileEntity;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.Random;

public class HeavySieveRenderer extends TileEntityRenderer<HeavySieveTileEntity> {

    private static final Random random = new Random();

    public HeavySieveRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(HeavySieveTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        World world = tileEntity.getWorld();
        if (world == null) {
            return;
        }

        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();

        matrixStack.push();

        // Render mesh
        SieveMeshRegistryEntry mesh = tileEntity.getSieveMesh();
        if (mesh != null) {
            IBakedModel meshModel = ModModels.meshes.get(mesh.getModelName());
            if (meshModel != null) {
                dispatcher.getBlockModelRenderer().renderModel(world, meshModel, tileEntity.getBlockState(), tileEntity.getPos(), matrixStack, buffer.getBuffer(RenderType.getTranslucent()), false, random, 0, Integer.MAX_VALUE, EmptyModelData.INSTANCE);
            }
        }

        ItemStack currentStack = tileEntity.getCurrentStack();
        if (!currentStack.isEmpty()) {
            BlockState contentState = StupidUtils.getStateFromItemStack(currentStack);
            if (contentState != null) {
                float progress = tileEntity.getProgress();
                matrixStack.push();
                matrixStack.translate(0.0625f, 0.5625f, 0.0625f);
                float tt = 0.42f;
                matrixStack.scale(0.88f, tt - progress * tt, 0.88f);
                dispatcher.renderBlock(contentState, matrixStack, buffer, combinedLight, combinedOverlay, EmptyModelData.INSTANCE);
                matrixStack.pop();
            }
        }

        matrixStack.pop();
    }

}
