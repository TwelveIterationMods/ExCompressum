package net.blay09.mods.excompressum.compat.tconstruct;

import exnihilo.registries.HammerRegistry;
import exnihilo.registries.helpers.Smashable;
import net.blay09.mods.excompressum.registry.CompressedHammerRegistry;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import tconstruct.library.ActiveToolMod;
import tconstruct.library.tools.AbilityHelper;
import tconstruct.library.tools.ToolCore;

import java.util.Collection;

public class ActiveSmashingMod extends ActiveToolMod {

    @Override
    public boolean beforeBlockBreak(ToolCore tool, ItemStack itemStack, int x, int y, int z, EntityLivingBase entity) {
        NBTTagCompound tags = itemStack.getTagCompound().getCompoundTag("InfiTool");
        if(tags.getBoolean(ModSmashingII.NAME)) {
            World world = entity.worldObj;
            if(world.isRemote || EnchantmentHelper.getSilkTouchModifier(entity)) {
                return false;
            }
            Block block = world.getBlock(x, y, z);
            int metadata = world.getBlockMetadata(x, y, z);
            Collection<Smashable> rewards = CompressedHammerRegistry.getSmashables(block, metadata);
            if (rewards == null || rewards.isEmpty()) {
                rewards = HammerRegistry.getRewards(block, metadata);
                if(rewards == null || rewards.isEmpty()) {
                    return false;
                }
            }
            int fortune = EnchantmentHelper.getFortuneModifier(entity);
            for (Smashable reward : rewards) {
                if (world.rand.nextFloat() <= reward.chance + (reward.luckMultiplier * fortune)) {
                    EntityItem entityItem = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, new ItemStack(reward.item, 1, reward.meta));
                    double motion = 0.05;
                    entityItem.motionX = world.rand.nextGaussian() * motion;
                    entityItem.motionY = 0.2;
                    entityItem.motionZ = world.rand.nextGaussian() * motion;
                    world.spawnEntityInWorld(entityItem);
                }
            }
            AbilityHelper.onBlockChanged(itemStack, world, block, x, y, z, entity, AbilityHelper.random);
            world.setBlockToAir(x, y, z);
        }
        return super.beforeBlockBreak(tool, itemStack, x, y, z, entity);
    }
}
