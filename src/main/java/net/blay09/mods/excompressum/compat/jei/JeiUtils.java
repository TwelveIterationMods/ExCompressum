package net.blay09.mods.excompressum.compat.jei;

import net.blay09.mods.excompressum.utils.Messages;
import net.minecraft.loot.BinomialRange;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Random;

public class JeiUtils {
    private static final Random random = new Random();
    private static final NumberFormat chanceFormat = new DecimalFormat("##.##");

    public static void addLootTableEntryTooltips(MergedLootTableEntry entry, List<ITextComponent> tooltip) {
        LootTableEntry mainEntry = entry.getEntries().get(0);
        tooltip.add(Messages.lang("tooltip.jei.main_roll", getCountTextComponent(mainEntry), formatChance(mainEntry)));
        for (int i = 1; i < entry.getEntries().size(); i++) {
            LootTableEntry bonusEntry = entry.getEntries().get(i);
            tooltip.add(Messages.lang("tooltip.jei.bonus_roll", getCountTextComponent(bonusEntry), formatChance(bonusEntry)));
        }
    }

    public static void addLootTableEntryTooltips(LootTableEntry entry, List<ITextComponent> tooltip) {
        tooltip.add(getCountTextComponent(entry));
        if (entry.getBaseChance() > 0f) {
            tooltip.add(Messages.lang("tooltip.jei.drop_chance", formatChance(entry)));
        }
    }

    private static String formatChance(LootTableEntry entry) {
        return chanceFormat.format((entry.getBaseChance() * 100f));
    }


    private static ITextComponent getCountTextComponent(LootTableEntry entry) {
        if (entry.getCountRange() instanceof RandomValueRange) {
            int min = Math.max(0, (int) ((RandomValueRange) entry.getCountRange()).getMin());
            int max = (int) ((RandomValueRange) entry.getCountRange()).getMax();
            return Messages.lang("tooltip.jei.drop_count.random_range", min, max);
        } else if (entry.getCountRange() instanceof BinomialRange) {
            int n = ((BinomialRange) entry.getCountRange()).n;
            float p = ((BinomialRange) entry.getCountRange()).p;
            return Messages.lang("tooltip.jei.drop_count.binomial_range", (int) (n * p));
        } else if (entry.getCountRange() instanceof ConstantRange) {
            return Messages.lang("tooltip.jei.drop_count.constant", entry.getCountRange().generateInt(random));
        } else {
            return new StringTextComponent("");
        }
    }
}
