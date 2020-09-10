package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.handler.GuiHandler;
import net.blay09.mods.excompressum.tile.RationingAutoCompressorTileEntity;
import net.minecraft.block.BlockContainer;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RationingAutoCompressorBlock extends AutoCompressorBlock {

    public static final String name = "auto_compressor_rationing";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new RationingAutoCompressorTileEntity();
    }

}
