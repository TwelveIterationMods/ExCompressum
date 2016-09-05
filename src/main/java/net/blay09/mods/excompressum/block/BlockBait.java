package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.tile.TileBait;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockBait extends BlockContainer {

    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0, 0, 0, 1, 0.1, 1);

    public BlockBait() {
        super(Material.GROUND);
        setHardness(0.1f);
        setCreativeTab(ExCompressum.creativeTab);
        setRegistryName("bait");
    }

    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDING_BOX;
    }

    @Nullable
    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return null;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i <= 6; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileBait();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileBait tileEntity = (TileBait) world.getTileEntity(pos);
        if(tileEntity != null) {
            TileBait.EnvironmentalCondition environmentStatus = tileEntity.checkSpawnConditions(true);
            if (!world.isRemote) {
                ITextComponent chatComponent = new TextComponentTranslation(environmentStatus.langKey);
                chatComponent.getStyle().setColor(environmentStatus != TileBait.EnvironmentalCondition.CanSpawn ? TextFormatting.RED : TextFormatting.GREEN);
                player.addChatComponentMessage(chatComponent);
            }
        }
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if(placer instanceof EntityPlayer) {
            TileBait tileEntity = (TileBait) world.getTileEntity(pos);
            if(tileEntity != null) {
                TileBait.EnvironmentalCondition environmentStatus = tileEntity.checkSpawnConditions(true);
                if (!world.isRemote) {
                    ITextComponent chatComponent = new TextComponentTranslation(environmentStatus.langKey);
                    chatComponent.getStyle().setColor(environmentStatus != TileBait.EnvironmentalCondition.CanSpawn ? TextFormatting.RED : TextFormatting.GREEN);
                    ((EntityPlayer) placer).addChatComponentMessage(chatComponent);
                }
            }
        }
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        TileBait tileEntity = (TileBait) world.getTileEntity(pos);
        if(tileEntity != null && tileEntity.checkSpawnConditions(false) == TileBait.EnvironmentalCondition.CanSpawn) {
            if (rand.nextFloat() <= 0.2f) {
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + rand.nextFloat(), pos.getY() + rand.nextFloat() * 0.5f, pos.getZ() + rand.nextFloat(), 0.0, 0.0, 0.0);
            }
        }
    }

}
