package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.config.ModConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.ToolItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.HashSet;

public class ItemCompressedCrook extends ToolItem implements ICompressedCrook {

    public static final String name = "compressed_crook";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public ItemCompressedCrook(Properties properties) {
        super(0f, 0f, ItemTier.WOOD, new HashSet<>(), properties);
        setMaxDamage((int) (ItemTier.WOOD.getMaxUses() * 2 * ModConfig.tools.compressedCrookDurabilityMultiplier));
    }

    @Override
    public boolean onLeftClickEntity(ItemStack itemStack, PlayerEntity player, Entity entity) {
        pushEntity(itemStack, player, entity);
        return true;
    }

    @Override
    public ActionResultType itemInteractionForEntity(ItemStack itemStack, PlayerEntity player, LivingEntity entity, Hand hand) {
        pushEntity(itemStack, player, entity);
        return ActionResultType.SUCCESS;
    }

    private void pushEntity(ItemStack itemStack, PlayerEntity player, Entity entity) {
        if(!player.world.isRemote) {
            double distance = Math.sqrt(Math.pow(player.getPosX() - entity.getPosX(), 2) + Math.pow(player.getPosZ() - entity.getPosZ(), 2));
            double scalarX = (player.getPosX() - entity.getPosX()) / distance;
            double scalarZ = (player.getPosZ() - entity.getPosZ()) / distance;
            double strength = 2.0;
            double velX = 0.0 - scalarX * strength;
            double velY = player.getPosY() < entity.getPosY() ? 0.5 : 0.0;
            double velZ = 0.0 - scalarZ * strength;
            entity.addVelocity(velX, velY, velZ);
        }
        itemStack.damageItem(1, player, it -> {});
    }

    @Override
    public boolean canHarvestBlock(BlockState block) {
        return block.getMaterial() == Material.LEAVES;
    }

    @Override
    public float getDestroySpeed(ItemStack item, BlockState block) {
        return block.getMaterial() == Material.LEAVES ? getTier().getEfficiency() * ModConfig.tools.compressedCrookSpeedMultiplier : 0f;
    }

    @Override
    public boolean canCrook(ItemStack itemStack, World world, BlockState state, PlayerEntity entityPlayer) {
        return true;
    }

}
