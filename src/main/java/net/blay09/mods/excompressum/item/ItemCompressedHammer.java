package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.StupidUtils;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.HashSet;

public class ItemCompressedHammer extends ItemTool {

    public ItemCompressedHammer(ToolMaterial material, String name) {
        super(6f, -6f, material, new HashSet<Block>());
        setRegistryName("compressed_hammer_" + name);
        setUnlocalizedName(getRegistryName().toString());
        setCreativeTab(ExCompressum.creativeTab);
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
        return CompressedHammerRegistry.isHammerable(state);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        if ((CompressedHammerRegistry.isHammerable(state) || ExRegistro.isHammerable(state)) && state.getBlock().getHarvestLevel(state) <= toolMaterial.getHarvestLevel()) {
            return efficiencyOnProperMaterial * 0.75f;
        }
        return 0.8f;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemStack, BlockPos pos, EntityPlayer player) {
        World world = player.worldObj;
        if (world.isRemote || StupidUtils.hasSilkTouchModifier(player)) {
            return false;
        }
        IBlockState state = world.getBlockState(pos);
        int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemStack);
        Collection<ItemStack> rewards = CompressedHammerRegistry.rollHammerRewards(state, fortune, world.rand);
        if (rewards.isEmpty()) {
            return false;
        }
        for (ItemStack rewardStack : rewards) {
            EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, rewardStack);
            double motion = 0.05;
            entityItem.motionX = world.rand.nextGaussian() * motion;
            entityItem.motionY = 0.2;
            entityItem.motionZ = world.rand.nextGaussian() * motion;
            world.spawnEntityInWorld(entityItem);
        }
        world.setBlockToAir(pos);
        itemStack.damageItem(1, player);
        if (itemStack.stackSize == 0) {
            player.renderBrokenItemStack(itemStack);
        }
        return true;
    }

}
