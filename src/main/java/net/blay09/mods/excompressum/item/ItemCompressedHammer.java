package net.blay09.mods.excompressum.item;

import com.google.common.collect.Lists;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;

public class ItemCompressedHammer extends ItemTool {

    public ItemCompressedHammer(ToolMaterial material, String name) {
        super(6f, -3.2f, material, new HashSet<Block>());
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
    public boolean onBlockDestroyed(ItemStack itemStack, World world, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if(!world.isRemote && !StupidUtils.hasSilkTouchModifier(entityLiving) && canHarvestBlock(state, itemStack)) {
            int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemStack);
            List<ItemStack> rewards = Lists.newArrayList();
            rewards.addAll(CompressedHammerRegistry.rollHammerRewards(state, fortune, world.rand));
            rewards.addAll(ExRegistro.rollHammerRewards(state, toolMaterial.getHarvestLevel(), fortune, world.rand));
            for (ItemStack rewardStack : rewards) {
                world.spawnEntityInWorld(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, rewardStack));
            }
            world.setBlockToAir(pos);
        }
        return super.onBlockDestroyed(itemStack, world, state, pos, entityLiving);
    }
}
