package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.config.ModConfig;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;

import java.util.Collection;
import java.util.Random;

public class AutoCompressedHammerTileEntity extends AutoHammerTileEntity {

    @Override
    public int getEffectiveEnergy() {
        return ModConfig.automation.autoCompressedHammerEnergy;
    }

    @Override
    public float getEffectiveSpeed() {
        return ModConfig.automation.autoCompressedHammerSpeed * getSpeedMultiplier();
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
        if (itemStack.getItem() == ModItems.compressedHammerDiamond) {
            return true;
        }
        if (itemStack.getItem() == Compat.TCONSTRUCT_HAMMER) {
            CompoundNBT tagCompound = itemStack.getTag();
            if (tagCompound != null) {
                ListNBT traits = tagCompound.getList("Traits", Constants.NBT.TAG_STRING);
                for (INBT tag : traits) {
                    if (tag.getString().equalsIgnoreCase(Compat.TCONSTRUCT_TRAIT_SMASHINGII)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
