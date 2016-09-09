package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ExCompressumConfig;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;

public class ItemCompressedCrook extends ItemTool {

    public ItemCompressedCrook() {
        super(0f, 0f, ToolMaterial.WOOD, new HashSet<Block>());
        setRegistryName("compressed_crook");
        setUnlocalizedName(getRegistryName().toString());
        setMaxDamage((int) (ToolMaterial.WOOD.getMaxUses() * 2 * ExCompressumConfig.compressedCrookDurabilityMultiplier));
        setCreativeTab(ExCompressum.creativeTab);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack itemStack, EntityPlayer player, Entity entity) {
        pushEntity(itemStack, player, entity);
        return true;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
        pushEntity(itemStack, player, entity);
        return true;
    }

    private void pushEntity(ItemStack itemStack, EntityPlayer player, Entity entity) {
        if(!player.worldObj.isRemote) {
            double distance = Math.sqrt(Math.pow(player.posX - entity.posX, 2) + Math.pow(player.posZ - entity.posZ, 2));
            double scalarX = (player.posX - entity.posX) / distance;
            double scalarZ = (player.posZ - entity.posZ) / distance;
            double strength = 2.0;
            double velX = 0.0 - scalarX * strength;
            double velY = player.posY < entity.posY ? 0.5 : 0.0;
            double velZ = 0.0 - scalarZ * strength;
            entity.addVelocity(velX, velY, velZ);
        }
        itemStack.damageItem(1, player);
    }

    @Override
    public boolean canHarvestBlock(IBlockState block) {
        return block.getMaterial() == Material.LEAVES;
    }

    @Override
    public float getStrVsBlock(ItemStack item, IBlockState block) {
        return block.getMaterial() == Material.LEAVES ? toolMaterial.getEfficiencyOnProperMaterial() * ExCompressumConfig.compressedCrookSpeedMultiplier : 0f;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World world, IBlockState block, BlockPos pos, EntityLivingBase player) {
        if(!player.worldObj.isRemote) {
            IBlockState state = player.worldObj.getBlockState(pos);
            boolean isLeaves = state.getMaterial() == Material.LEAVES;
            if (isLeaves || state instanceof BlockTallGrass) { // Looks like Omnia also makes seeds from grass more frequent, that's cool, so do it for compressed as well
                int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemStack);
                state.getBlock().dropBlockAsItem(player.worldObj, pos, player.worldObj.getBlockState(pos), fortune);
                if (isLeaves) {
                    ItemStack silkWorm = ExRegistro.rollSilkWorm(player, state, fortune);
                    if(silkWorm != null) {
                        player.worldObj.spawnEntityInWorld(new EntityItem(player.worldObj, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, silkWorm));
                    }
                }
            }
        }
        itemStack.damageItem(1, player);
        return true;
    }

}
