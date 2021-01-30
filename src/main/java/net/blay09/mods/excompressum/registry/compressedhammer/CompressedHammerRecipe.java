package net.blay09.mods.excompressum.registry.compressedhammer;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.LootTableProvider;
import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class CompressedHammerRecipe extends ExCompressumRecipe {
    public static final IRecipeType<CompressedHammerRecipe> TYPE = IRecipeType.register(ExCompressum.MOD_ID + ":compressed_hammer");

    private final Ingredient input;
    private final LootTableProvider lootTable;

    public CompressedHammerRecipe(ResourceLocation id, Ingredient input, LootTableProvider lootTable) {
        super(id, TYPE);
        this.input = input;
        this.lootTable = lootTable;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.compressedHammerRecipe;
    }

    public Ingredient getInput() {
        return input;
    }

    public LootTableProvider getLootTable() {
        return lootTable;
    }

}
