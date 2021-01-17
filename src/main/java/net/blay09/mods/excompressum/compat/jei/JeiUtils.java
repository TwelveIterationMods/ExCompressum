package net.blay09.mods.excompressum.compat.jei;

import net.blay09.mods.excompressum.utils.Messages;
import net.minecraft.loot.BinomialRange;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class JeiUtils {
    public static void addLootTableEntryTooltips(LootTableEntry entry, List<ITextComponent> tooltip) {
        if (entry.getCountRange() instanceof RandomValueRange) {
            int min = Math.max(0, (int) ((RandomValueRange) entry.getCountRange()).getMin());
            int max = (int) ((RandomValueRange) entry.getCountRange()).getMax();
            tooltip.add(Messages.lang("tooltip.jei.drop_count.random_range", min, max));
        } else if (entry.getCountRange() instanceof BinomialRange) {
            int n = ((BinomialRange) entry.getCountRange()).n;
            float p = ((BinomialRange) entry.getCountRange()).p;
            tooltip.add(Messages.lang("tooltip.jei.drop_count.binomial_range", (int) (n * p)));
        }
        if (entry.getBaseChance() > 0f) {
            tooltip.add(Messages.lang("tooltip.jei.drop_chance", (int) (entry.getBaseChance() * 100f)));
        }
    }
}
