package net.blay09.mods.excompressum.compat.crafttweaker.builder;


import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import net.blay09.mods.excompressum.compat.jei.LootTableUtils;
import net.blay09.mods.excompressum.registry.LootTableProvider;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.excompressum.ChickenStickRecipe")
public class ZenChickenStickRecipe extends ZenBaseRecipe<ZenChickenStickRecipe> {

    private final ChickenStickRecipe recipe;

    private ZenChickenStickRecipe(ResourceLocation recipeId) {
        this.recipe = new ChickenStickRecipe(recipeId, Ingredient.EMPTY, LootTableProvider.EMPTY);
    }

    @ZenCodeType.Method
    public static ZenChickenStickRecipe builder(ResourceLocation recipeId) {
        return new ZenChickenStickRecipe(recipeId);
    }

    @ZenCodeType.Method
    public ZenChickenStickRecipe addDrop(IItemStack drop) {
        addLootPoolBuilder().addEntry(LootTableUtils.buildLootEntry(drop.getInternal(), 1f));
        recipe.setLootTable(getLootTableProvider());
        return this;
    }

    @ZenCodeType.Method
    public ZenChickenStickRecipe addDrop(IItemStack drop, float chance) {
        addLootPoolBuilder().addEntry(LootTableUtils.buildLootEntry(drop.getInternal(), chance));
        recipe.setLootTable(getLootTableProvider());
        return this;
    }

    @ZenCodeType.Method
    public ZenChickenStickRecipe setInput(IIngredient input) {
        recipe.setInput(input.asVanillaIngredient());
        return this;
    }

    @Override
    public void updateLootTable(LootTableProvider provider) {
        recipe.setLootTable(provider);
    }

    public ChickenStickRecipe build() {
        return recipe;
    }

}
