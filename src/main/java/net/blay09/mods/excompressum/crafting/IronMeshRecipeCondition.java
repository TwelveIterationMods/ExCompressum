package net.blay09.mods.excompressum.crafting;

import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.registry.ExNihiloProvider;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.function.BooleanSupplier;

public class IronMeshRecipeCondition implements IConditionFactory {
	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		return () -> ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.IRON_MESH).isEmpty();
	}
}
