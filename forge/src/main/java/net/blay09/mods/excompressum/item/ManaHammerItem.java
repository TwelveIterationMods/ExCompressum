package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.compat.botania.BotaniaCompat;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ManaHammerItem extends DiggerItem /*TODO implements IManaUsingItem*/ {

    public static final String name = "hammer_mana";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    private static final int MANA_PER_DAMAGE = 60;

    public ManaHammerItem(Item.Properties properties) {
        super(6f, -3.2f, BotaniaCompat.getManaSteelItemTier(), ModTags.MINEABLE_WITH_HAMMER, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean manaRequested = attacker instanceof Player && BotaniaCompat.requestManaExactForTool(stack, (Player) attacker, 2 * MANA_PER_DAMAGE, true);
        if (!manaRequested) {
            stack.hurtAndBreak(2, attacker, it -> {
            });
        }
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (state.getDestroySpeed(level, pos) != 0.0) {
            boolean manaRequested = entityLiving instanceof Player && BotaniaCompat.requestManaExactForTool(stack, (Player) entityLiving, MANA_PER_DAMAGE, true);
            if (!manaRequested) {
                stack.hurtAndBreak(1, entityLiving, it -> {
                });
            }
        }
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int itemSlot, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player && stack.getDamageValue() > 0 && BotaniaCompat.requestManaExactForTool(stack, (Player) entity, 2 * MANA_PER_DAMAGE, true)) {
            stack.setDamageValue(stack.getDamageValue() - 1);
        }
    }

//    TODO @Override
    public boolean usesMana(ItemStack itemStack) {
        return true;
    }
}
