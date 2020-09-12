package net.blay09.mods.excompressum.registry.sievemesh;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class SieveMeshRegistry {

    private static final Map<ResourceLocation, SieveMeshRegistryEntry> entries = new HashMap<>();

    public static void registerDefaults() {
        SieveMeshRegistryEntry ironMesh = new SieveMeshRegistryEntry(new ItemStack(ModItems.ironMesh));
        ironMesh.setHeavy(true);
        ironMesh.setMeshLevel(3);
        ironMesh.setSpriteLocation(new ResourceLocation(ExCompressum.MOD_ID, "blocks/iron_mesh"));
        add(ironMesh);
    }

    public static Map<ResourceLocation, SieveMeshRegistryEntry> getEntries() {
        return entries;
    }

    @Nullable
    public static SieveMeshRegistryEntry getEntry(ItemStack itemStack) {
        return entries.get(itemStack.getItem().getRegistryName());
    }

    public static void add(SieveMeshRegistryEntry sieveMesh) {
        entries.put(sieveMesh.getItemStack().getItem().getRegistryName(), sieveMesh);
    }

}
