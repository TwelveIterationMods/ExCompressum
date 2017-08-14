package net.blay09.mods.excompressum.item;

import exnihilocreatio.items.tools.ICrook;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.config.ToolsConfig;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

import java.util.HashSet;

@Optional.Interface(iface = "exnihilocreatio.items.tools.ICrook", modid = Compat.EXNIHILO_CREATIO)
public class ItemCompressedCrook extends ItemTool implements ICompressedCrook, ICrook {

    public static final String name = "compressed_crook";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public ItemCompressedCrook() {
        super(0f, 0f, ToolMaterial.WOOD, new HashSet<>());
        setUnlocalizedName(registryName.toString());
        setCreativeTab(ExCompressum.creativeTab);
        setMaxDamage((int) (ToolMaterial.WOOD.getMaxUses() * 2 * ToolsConfig.compressedCrookDurabilityMultiplier));
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
        if(!player.world.isRemote) {
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
        return block.getMaterial() == Material.LEAVES ? toolMaterial.getEfficiencyOnProperMaterial() * ToolsConfig.compressedCrookSpeedMultiplier : 0f;
    }

    @Override
    public boolean canCrook(ItemStack itemStack, World world, IBlockState state, EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public boolean isCrook(ItemStack itemStack) {
        return true;
    }
}
