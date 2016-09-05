package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.ExCompressumConfig;
import net.blay09.mods.excompressum.ModItems;
import net.blay09.mods.excompressum.registry.CompressedHammerRegistry;
import net.blay09.mods.excompressum.registry.data.SmashableReward;
import net.minecraft.item.ItemStack;

import java.util.Collection;

public class TileEntityAutoCompressedHammer extends TileEntityAutoHammer {

    @Override
    public int getEffectiveEnergy() {
        return ExCompressumConfig.autoCompressedHammerEnergy;
    }

    @Override
    public float getEffectiveSpeed() {
        return ExCompressumConfig.autoCompressedHammerSpeed * getSpeedBoost();
    }

    @Override
    public boolean isRegistered(ItemStack itemStack) {
        return CompressedHammerRegistry.isRegistered(itemStack);
    }

    @Override
    public boolean isHammerUpgrade(ItemStack itemStack) {
        return itemStack.getItem() == ModItems.compressedHammerDiamond;
    }

    @Override
    protected Collection<SmashableReward> getSmashableRewards(ItemStack itemStack) {
        return CompressedHammerRegistry.getSmashables(itemStack);
    }

}
