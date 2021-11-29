package net.blay09.mods.excompressum.registry.hammer;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.IHammerRecipe;
import net.blay09.mods.excompressum.registry.LootTableProvider;
import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class HammerRecipe extends ExCompressumRecipe implements IHammerRecipe {
    public static final IRecipeType<HammerRecipe> TYPE = IRecipeType.register(ExCompressum.MOD_ID + ":hammer");

    private Ingredient input;
    private LootTableProvider lootTable;

    public HammerRecipe(ResourceLocation id, Ingredient input, LootTableProvider lootTable) {
        super(id, TYPE);
        this.input = input;
        this.lootTable = lootTable;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.hammerRecipe;
    }

    @Override
    public Ingredient getInput() {
        return input;
    }

    @Override
    public ResourceLocation getRecipeId() {
        return getId();
    }

    @Override
    public LootTableProvider getLootTable() {
        return lootTable;
    }

    public void setInput(Ingredient input) {
        this.input = input;
    }

    public void setLootTable(LootTableProvider lootTable) {
        this.lootTable = lootTable;
    }
}
