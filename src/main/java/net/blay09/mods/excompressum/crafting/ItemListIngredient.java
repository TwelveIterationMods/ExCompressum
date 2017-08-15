package net.blay09.mods.excompressum.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;

import javax.annotation.Nonnull;

public class ItemListIngredient implements IIngredientFactory {
	@Nonnull
	@Override
	public Ingredient parse(JsonContext context, JsonObject json) {
		JsonArray itemsArray = json.getAsJsonArray("items");
		NonNullList<ItemStack> items = NonNullList.create();
		for(int i = 0; i < itemsArray.size(); i++) {
			try {
				items.add(CraftingHelper.getItemStackBasic(itemsArray.get(i).getAsJsonObject(), context));
			} catch (JsonSyntaxException ignored) {
				// ignore missing items
			}
		}
		return Ingredient.fromStacks(items.toArray(new ItemStack[items.size()]));
	}
}
