package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.config.AutomationConfig;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.registry.hammer.CompressedHammerRegistry;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.Random;

public class TileAutoCompressedHammer extends TileAutoHammer {

    @Override
    public int getEffectiveEnergy() {
        return AutomationConfig.autoCompressedHammerEnergy; // TODO hammer enchantments
    }

    @Override
    public float getEffectiveSpeed() {
        return AutomationConfig.autoCompressedHammerSpeed * getSpeedBoost(); // TODO hammer enchantments
    }

    @Override
    public boolean isRegistered(ItemStack itemStack) {
        return CompressedHammerRegistry.isHammerable(itemStack);
    }

    @Override
    public Collection<ItemStack> rollHammerRewards(ItemStack itemStack, int miningLevel, float luck, Random rand) {
        return CompressedHammerRegistry.rollHammerRewards(itemStack, luck, rand);
    }

    @Override
    public boolean isHammerUpgrade(ItemStack itemStack) {
        return itemStack.getItem() == ModItems.compressedHammerDiamond;
    }

}
