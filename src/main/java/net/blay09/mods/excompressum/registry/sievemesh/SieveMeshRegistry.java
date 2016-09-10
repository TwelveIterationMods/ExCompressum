package net.blay09.mods.excompressum.registry.sievemesh;

import com.google.common.collect.Maps;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.registry.RegistryKey;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Map;

public class SieveMeshRegistry {

	private static final Map<RegistryKey, SieveMeshRegistryEntry> entries = Maps.newHashMap();

	public static void registerDefaults() {
		SieveMeshRegistryEntry ironMesh = new SieveMeshRegistryEntry(new ItemStack(ModItems.heavySilkMesh, 1, OreDictionary.WILDCARD_VALUE));
		ironMesh.setHeavy(true);
		ironMesh.setMeshLevel(3);
		ironMesh.setSpriteLocation(new ResourceLocation(ExCompressum.MOD_ID, "blocks/iron_mesh"));
		add(ironMesh);
	}

	public static SieveMeshRegistryEntry getEntry(ItemStack itemStack) {
		RegistryKey key = new RegistryKey(itemStack);
		SieveMeshRegistryEntry entry = entries.get(key);
		if(entry == null) {
			return entries.get(key.withWildcard());
		}
		return entry;
	}

	public static void add(SieveMeshRegistryEntry sieveMesh) {
		entries.put(new RegistryKey(sieveMesh.getItemStack()), sieveMesh);
	}

}
