package net.blay09.mods.excompressum.compat.jei;

import net.blay09.mods.excompressum.utils.Messages;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class JeiUtils {
    private static final NumberFormat chanceFormat = new DecimalFormat("##.##");

    public static void addLootTableEntryTooltips(MergedLootTableEntry entry, List<Component> tooltip) {
        LootTableEntry mainEntry = entry.getEntries().get(0);
        tooltip.add(Messages.lang("tooltip.jei.main_roll", getCountTextComponent(mainEntry), formatChance(mainEntry)));
        for (int i = 1; i < entry.getEntries().size(); i++) {
            LootTableEntry bonusEntry = entry.getEntries().get(i);
            tooltip.add(Messages.lang("tooltip.jei.bonus_roll", getCountTextComponent(bonusEntry), formatChance(bonusEntry)));
        }
    }

    public static void addLootTableEntryTooltips(LootTableEntry entry, List<Component> tooltip) {
        tooltip.add(Messages.lang("tooltip.jei.main_roll", getCountTextComponent(entry), formatChance(entry)));
    }

    private static String formatChance(LootTableEntry entry) {
        return chanceFormat.format((entry.getBaseChance() * 100f));
    }


    private static Component getCountTextComponent(LootTableEntry entry) {
        if (entry.getCountRange() instanceof UniformGenerator uniform) {
            int min = Math.max(0, (int) uniform.min);
            int max = (int) uniform.max;
            return Messages.lang("tooltip.jei.drop_count.random_range", min, max);
        } else if (entry.getCountRange() instanceof BinomialDistributionGenerator binomial) {
            return Messages.lang("tooltip.jei.drop_count.binomial_range", (int) (binomial.n * binomial.p));
        } else if (entry.getCountRange() instanceof ConstantValue constant) {
            return Messages.lang("tooltip.jei.drop_count.constant", (int) constant.value);
        } else {
            return new TextComponent("");
        }
    }
}
