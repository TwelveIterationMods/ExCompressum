package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.compat.botania.BotaniaBindings;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;

public class ManaHammerItem extends ToolItem implements IHammer {

    public static final String name = "hammer_mana";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    private static final int MANA_PER_DAMAGE = 60;

    public ManaHammerItem(Item.Properties properties) {
        super(6f, -3.2f, BotaniaBindings.manaSteelItemTier, new HashSet<>(), properties);
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, BlockState state) {
        return ExRegistro.isHammerable(state);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (ExRegistro.isHammerable(state) && state.getBlock().getHarvestLevel(state) <= getTier().getHarvestLevel()) {
            return efficiency * 0.75f;
        }
        return 0.8f;
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean manaRequested = attacker instanceof PlayerEntity && BotaniaBindings.requestManaExactForTool(stack, (PlayerEntity) attacker, 2 * MANA_PER_DAMAGE, true);
        if (!manaRequested) {
            stack.damageItem(2, attacker, it -> {
            });
        }
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (state.getBlockHardness(world, pos) != 0.0) {
            boolean manaRequested = entityLiving instanceof PlayerEntity && BotaniaBindings.requestManaExactForTool(stack, (PlayerEntity) entityLiving, MANA_PER_DAMAGE, true);
            if (!manaRequested) {
                stack.damageItem(1, entityLiving, it -> {
                });
            }
        }
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (!world.isRemote && entity instanceof PlayerEntity && stack.getDamage() > 0 && BotaniaBindings.requestManaExactForTool(stack, (PlayerEntity) entity, 2 * MANA_PER_DAMAGE, true)) {
            stack.setDamage(stack.getDamage() - 1);
        }
    }

    @Override
    public boolean canHammer(ItemStack itemStack, World world, BlockState state, PlayerEntity entityPlayer) {
        return true;
    }

    @Override
    public int getHammerLevel(ItemStack itemStack, World world, BlockState state, PlayerEntity entityPlayer) {
        return getTier().getHarvestLevel();
    }

}
