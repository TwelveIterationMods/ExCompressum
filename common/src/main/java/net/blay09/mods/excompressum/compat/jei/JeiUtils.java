package net.blay09.mods.excompressum.compat.jei;

import net.blay09.mods.excompressum.loot.LootTableEntry;
import net.blay09.mods.excompressum.loot.LootTableUtils;
import net.blay09.mods.excompressum.loot.MergedLootTableEntry;
import net.blay09.mods.excompressum.mixin.BinomialDistributionGeneratorAccessor;
import net.blay09.mods.excompressum.mixin.ConstantValueAccessor;
import net.blay09.mods.excompressum.mixin.UniformGeneratorAccessor;
import net.minecraft.network.chat.Component;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class JeiUtils {
    private static final NumberFormat chanceFormat = new DecimalFormat("##.##");

    public static void addLootTableEntryTooltips(MergedLootTableEntry entry, List<Component> tooltip) {
        LootTableEntry mainEntry = entry.getEntries().get(0);
        tooltip.add(Component.translatable("tooltip.jei.main_roll", getCountTextComponent(mainEntry), formatChance(mainEntry)));
        for (int i = 1; i < entry.getEntries().size(); i++) {
            LootTableEntry bonusEntry = entry.getEntries().get(i);
            tooltip.add(Component.translatable("tooltip.jei.bonus_roll", getCountTextComponent(bonusEntry), formatChance(bonusEntry)));
        }
    }

    public static void addLootTableEntryTooltips(LootTableEntry entry, List<Component> tooltip) {
        tooltip.add(Component.translatable("tooltip.jei.main_roll", getCountTextComponent(entry), formatChance(entry)));
    }

    private static String formatChance(LootTableEntry entry) {
        return chanceFormat.format((entry.getBaseChance() * 100f));
    }


    private static Component getCountTextComponent(LootTableEntry entry) {
        if (entry.getCountRange() instanceof UniformGeneratorAccessor uniform) {
            int min = (int) LootTableUtils.getMinCount(uniform.getMin());
            int max = (int) LootTableUtils.getMaxCount(uniform.getMax());
            return Component.translatable("tooltip.jei.drop_count.random_range", min, max);
        } else if (entry.getCountRange() instanceof BinomialDistributionGeneratorAccessor binomial) {
            int n = (int) LootTableUtils.getMinCount(binomial.getN());
            int p = (int) LootTableUtils.getMinCount(binomial.getP());
            return Component.translatable("tooltip.jei.drop_count.binomial_range", n * p);
        } else if (entry.getCountRange() instanceof ConstantValueAccessor constant) {
            return Component.translatable("tooltip.jei.drop_count.constant", (int) constant.getValue());
        } else {
            return Component.empty();
        }
    }
}
