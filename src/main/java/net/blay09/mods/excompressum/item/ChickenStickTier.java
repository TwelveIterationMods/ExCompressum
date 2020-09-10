package net.blay09.mods.excompressum.item;

import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemTier;
import net.minecraft.item.crafting.Ingredient;

public enum ChickenStickTier implements IItemTier {
    INSTANCE;

    @Override
    public int getMaxUses() {
        return 0;
    }

    @Override
    public float getEfficiency() {
        return ItemTier.DIAMOND.getEfficiency();
    }

    @Override
    public float getAttackDamage() {
        return 0;
    }

    @Override
    public int getHarvestLevel() {
        return ItemTier.DIAMOND.getHarvestLevel();
    }

    @Override
    public int getEnchantability() {
        return 0;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return Ingredient.EMPTY;
    }
}
