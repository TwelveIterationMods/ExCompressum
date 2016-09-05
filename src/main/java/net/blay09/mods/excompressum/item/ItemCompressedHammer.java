package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.StupidUtils;
import net.blay09.mods.excompressum.registry.CompressedHammerRegistry;
import net.blay09.mods.excompressum.registry.HammerRegistry;
import net.blay09.mods.excompressum.registry.data.SmashableReward;
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
        super(5f, 0f, material, new HashSet<Block>()); // TODO check attack speed, should be slow
        setRegistryName("compressed_hammer_" + name);
        setUnlocalizedName(getRegistryName().toString());
        setCreativeTab(ExCompressum.creativeTab);
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
        return HammerRegistry.isRegistered(state);
    }

    /*@Override // TODO still need to find out what happened to getDigSpeed
    public float getDigSpeed(ItemStack item, Block block, int meta) {
        if ((CompressedHammerRegistry.isRegistered(block, meta) || HammerRegistry.registered(new ItemStack(block, 1, meta))) && block.getHarvestLevel(meta) <= toolMaterial.getHarvestLevel()) {
            return efficiencyOnProperMaterial * 0.75f;
        }
        return 0.8f;
    }*/

    @Override // TODO omnia uses onBlockDestroyed, can we use that too? well of course we can, but go find out what's the big difference (and then switch because onBlockDestroyed is probably better and onBlockStartBreak was only taken over from old Ex Nihilo)
    public boolean onBlockStartBreak(ItemStack itemStack, BlockPos pos, EntityPlayer player) {
        World world = player.worldObj;
        if (world.isRemote || StupidUtils.hasSilkTouchModifier(player)) {
            return false;
        }
        IBlockState state = world.getBlockState(pos);
        Collection<SmashableReward> rewards = CompressedHammerRegistry.getSmashables(state);
        if (rewards.isEmpty()) {
            return false;
        }
        int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemStack);
        for (SmashableReward reward : rewards) {
            if (world.rand.nextFloat() <= reward.getChance() + (reward.getLuckMultiplier() * fortune)) {
                EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, reward.createItemStack());
                double motion = 0.05;
                entityItem.motionX = world.rand.nextGaussian() * motion;
                entityItem.motionY = 0.2;
                entityItem.motionZ = world.rand.nextGaussian() * motion;
                world.spawnEntityInWorld(entityItem);
            }
        }
        world.setBlockToAir(pos);
        itemStack.damageItem(1, player);
        if (itemStack.stackSize == 0) {
            player.renderBrokenItemStack(itemStack); // TODO is this enough? or do I have to do more
        }
        return true;
    }

}
