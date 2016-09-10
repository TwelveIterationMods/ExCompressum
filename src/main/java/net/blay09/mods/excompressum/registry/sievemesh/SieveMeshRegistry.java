package net.blay09.mods.excompressum.registry.sievemesh;

import com.google.common.collect.Maps;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class SieveMeshRegistry {

	private static final Map<String, SieveMeshRegistryEntry> meshes = Maps.newHashMap();

	public static void registerDefaults() {
		SieveMeshRegistryEntry ironMesh = new SieveMeshRegistryEntry(new ItemStack(ModItems.heavySilkMesh), true);
		ironMesh.setHeavy(true);
		ironMesh.setMeshLevel(3);
		ironMesh.setSpriteLocation(new ResourceLocation(ExCompressum.MOD_ID, "blocks/iron_mesh"));
		add(ironMesh);
	}

	public static SieveMeshRegistryEntry getEntry(ItemStack itemStack) {
		String registryName = itemStack.getItem().getRegistryName().toString();
		SieveMeshRegistryEntry sieveMesh = meshes.get(registryName + ":" + itemStack.getItemDamage());
		if(sieveMesh == null) {
			sieveMesh = meshes.get(registryName + ":*");
		}
		return sieveMesh;
	}

	public static void add(SieveMeshRegistryEntry sieveMesh) {
		meshes.put(sieveMesh.getKey(), sieveMesh);
	}

}
