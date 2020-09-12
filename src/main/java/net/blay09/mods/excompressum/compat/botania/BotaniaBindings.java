package net.blay09.mods.excompressum.compat.botania;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;

public class BotaniaBindings {

    public static IItemTier manaSteelItemTier = ItemTier.DIAMOND; // TODO botania compat

    public static boolean requestManaExactForTool(ItemStack stack, PlayerEntity attacker, int i, boolean b) {
        return true; // TODO botania compat
    }
}
