package net.blay09.mods.excompressum.registry.heavysieve.newregistry;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class ExCompressumRecipe implements IRecipe<IInventory> {

    private final ResourceLocation id;
    private final IRecipeType<?> type;

    public ExCompressumRecipe(ResourceLocation id, IRecipeType<?> type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return  ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public abstract IRecipeSerializer<?> getSerializer();

    @Override
    public IRecipeType<?> getType() {
        return type;
    }

}
