package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ExCompressumConfig;
import net.blay09.mods.excompressum.registry.HeavySieveRegistry;
import net.blay09.mods.excompressum.tile.TileEntityHeavySieve;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nullable;
import java.util.List;

public class BlockHeavySieve extends BlockContainer {

    public BlockHeavySieve() {
        super(Material.WOOD);
        setCreativeTab(ExCompressum.creativeTab);
        setHardness(2f);
        setRegistryName("heavy_sieve");
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        for(int i = 0; i < 6; i++) {
            list.add(new ItemStack(item, 1, i));
        }
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
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityHeavySieve();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        // TODO wat is this
        /*PlayerInteractEvent event = new PlayerInteractEvent(player, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, x, y, z, side, world); // eh? why is this here?
        if (MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Event.Result.DENY || event.useBlock == Event.Result.DENY) {
            return false;
        }*/

        TileEntityHeavySieve tileEntity = (TileEntityHeavySieve) world.getTileEntity(pos);

        if (tileEntity.getMode() == TileEntitySieve.SieveMode.EMPTY && heldItem != null) {
            if (HeavySieveRegistry.isRegistered(Block.getBlockFromItem(heldItem.getItem()), heldItem.getItemDamage())) {
                tileEntity.addSievable(heldItem);
                if (!player.capabilities.isCreativeMode) {
                    heldItem.stackSize--;
                }
            }
        } else {
            if (world.isRemote) {
                tileEntity.processContents(player.capabilities.isCreativeMode);
            } else {
                if (tileEntity.getMode() != TileEntitySieve.SieveMode.EMPTY) {
                    if (ExCompressumConfig.allowHeavySieveAutomation || !(player instanceof FakePlayer)) {
                        tileEntity.processContents(player.capabilities.isCreativeMode);
                    }
                }
            }
        }

        return true;
    }

}
