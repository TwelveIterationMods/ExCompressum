package net.blay09.mods.excompressum.registry.sievemesh;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.excompressum.api.sievemesh.CommonMeshType;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SieveMeshRegistry {

    private static final Map<CommonMeshType, SieveMeshRegistryEntry> entriesByType = new HashMap<>();
    private static final Map<ResourceLocation, SieveMeshRegistryEntry> entriesByItem = new HashMap<>();

    public static void registerDefaults(@Nullable Object backingMesh) {
        SieveMeshRegistryEntry ironMesh = new SieveMeshRegistryEntry(CommonMeshType.IRON, new ItemStack(ModItems.ironMesh), backingMesh);
        ironMesh.setHeavy(true);
        ironMesh.setMeshLevel(3);
        ironMesh.setModelName("iron");
        add(ironMesh);
    }

    public static Map<ResourceLocation, SieveMeshRegistryEntry> getEntries() {
        return entriesByItem;
    }

    @Nullable
    public static SieveMeshRegistryEntry getEntry(ItemStack itemStack) {
        ExNihilo.getInstance(); // Kinda hacky - makes sure nihilistic provider is initialized if no Nihilo is installed
        final var itemId = Balm.getRegistries().getKey(itemStack.getItem());
        return entriesByItem.get(itemId);
    }

    public static void add(SieveMeshRegistryEntry sieveMesh) {
        entriesByType.put(sieveMesh.getMeshType(), sieveMesh);
        final var itemId = Balm.getRegistries().getKey(sieveMesh.getItemStack().getItem());
        entriesByItem.put(itemId, sieveMesh);
    }

    public static SieveMeshRegistryEntry getEntry(CommonMeshType type) {
        return entriesByType.get(type);
    }

}
