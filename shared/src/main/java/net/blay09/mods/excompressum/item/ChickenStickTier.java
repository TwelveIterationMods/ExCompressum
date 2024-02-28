package net.blay09.mods.excompressum.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;

public enum ChickenStickTier implements Tier {
    INSTANCE;

    @Override
    public int getUses() {
        return 0;
    }

    @Override
    public float getSpeed() {
        return Tiers.DIAMOND.getSpeed();
    }

    @Override
    public float getAttackDamageBonus() {
        return 0;
    }

    @Override
    public int getLevel() {
        return Tiers.DIAMOND.getLevel();
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.EMPTY;
    }

}
