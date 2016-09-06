package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.ExCompressumConfig;
import net.blay09.mods.excompressum.ModItems;
import net.blay09.mods.excompressum.registry.hammer.CompressedHammerRegistry;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.Random;

public class TileAutoCompressedHammer extends TileAutoHammer {

    @Override
    public int getEffectiveEnergy() {
        return ExCompressumConfig.autoCompressedHammerEnergy; // TODO hammer enchantments
    }

    @Override
    public float getEffectiveSpeed() {
        return ExCompressumConfig.autoCompressedHammerSpeed * getSpeedBoost(); // TODO hammer enchantments
    }

    @Override
    public boolean isRegistered(ItemStack itemStack) {
        return CompressedHammerRegistry.isHammerable(itemStack);
    }

    @Override
    public Collection<ItemStack> rollHammerRewards(ItemStack itemStack, float luck, Random rand) {
        return CompressedHammerRegistry.rollHammerRewards(itemStack, luck, rand);
    }

    @Override
    public boolean isHammerUpgrade(ItemStack itemStack) {
        return itemStack.getItem() == ModItems.compressedHammerDiamond;
    }

}
