package net.blay09.mods.excompressum.crafting;

import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.registry.ExNihiloProvider;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.function.BooleanSupplier;

public class HasNihiloItemCondition implements IConditionFactory {
	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		return () -> {
			String key = json.get("value").getAsString();
			ExNihiloProvider.NihiloItems nihiloItem = ExNihiloProvider.NihiloItems.valueOf(key);
			return !ExRegistro.getNihiloItem(nihiloItem).isEmpty();
		};
	}
}
