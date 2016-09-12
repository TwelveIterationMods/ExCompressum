package net.blay09.mods.excompressum.compat.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistry;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistryEntry;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistryEntry;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

@JEIPlugin
public class JEIAddon implements IModPlugin {

	@Override
	public void register(@Nonnull IModRegistry registry) {
		registry.addRecipeCategories(new HeavySieveRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeHandlers(new HeavySieveRecipeHandler());

		List<HeavySieveRecipe> heavySieveRecipes = Lists.newArrayList();
		if(ExRegistro.doMeshesSplitLootTables()) {
			Collection<HeavySieveRegistryEntry> entries = HeavySieveRegistry.INSTANCE.getEntries().values();
			for (SieveMeshRegistryEntry sieveMesh : SieveMeshRegistry.getEntries().values()) {
				for(HeavySieveRegistryEntry entry : entries) {
					if(!entry.getRewardsForMesh(sieveMesh).isEmpty()) {
						heavySieveRecipes.add(new HeavySieveRecipe(entry, sieveMesh));
					}
				}
			}
		} else {
			for(HeavySieveRegistryEntry entry : HeavySieveRegistry.INSTANCE.getEntries().values()) {
				heavySieveRecipes.add(new HeavySieveRecipe(entry));
			}
		}
		registry.addRecipes(heavySieveRecipes);
	}

	@Override
	public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {

	}

}
