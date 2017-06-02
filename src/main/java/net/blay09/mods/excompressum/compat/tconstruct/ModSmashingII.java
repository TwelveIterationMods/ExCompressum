package net.blay09.mods.excompressum.compat.tconstruct;

import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRegistry;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;
import slimeknights.tconstruct.library.utils.ToolHelper;

import java.util.Collection;

// FUTURE This one needs a cool graphic!
public class ModSmashingII extends ModifierTrait {

	public static final String ID = "excompressum:smashingii";

	private static final float SPEED_DECREASE = 0.5f;
	private static final float DAMAGE_INCREASE = 3;

	public ModSmashingII() {
		super(ID, 0xFF0000);
	}

	@Override
	public boolean canApplyTogether(Enchantment enchantment) {
		return enchantment != Enchantments.SILK_TOUCH;
	}

	@Override
	public void miningSpeed(ItemStack tool, PlayerEvent.BreakSpeed event) {
		super.miningSpeed(tool, event);
		event.setNewSpeed(Math.max(0.1f, event.getNewSpeed() - SPEED_DECREASE));
	}

	@Override
	public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage, boolean isCritical) {
		return damage + DAMAGE_INCREASE;
	}

	@Override
	public void blockHarvestDrops(ItemStack tool, BlockEvent.HarvestDropsEvent event) {
		Collection<ItemStack> rewards = CompressedHammerRegistry.rollHammerRewards(event.getState(), event.getFortuneLevel(), event.getHarvester().world.rand);
		if (rewards.isEmpty()) {
			rewards = ExRegistro.rollHammerRewards(event.getState(), ToolHelper.getHarvestLevelStat(tool), event.getFortuneLevel(), event.getHarvester().world.rand);
			if (rewards.isEmpty()) {
				return;
			}
		}
		event.getDrops().clear();
		event.setDropChance(1f);
		for (ItemStack itemStack : rewards) {
			event.getDrops().add(itemStack);
		}
	}

}
