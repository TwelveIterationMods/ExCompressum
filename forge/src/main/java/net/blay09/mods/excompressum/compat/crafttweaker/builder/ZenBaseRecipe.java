package net.blay09.mods.excompressum.compat.crafttweaker.builder;


import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import net.blay09.mods.excompressum.registry.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import org.openzen.zencode.java.ZenCodeType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class ZenBaseRecipe<T> {

    private final List<LootPool.Builder> lootPoolBuilders = new ArrayList<>();
    private ResourceLocation lootTableLocation;

    public final LootTableProvider getLootTableProvider() {
        if (lootTableLocation != null && !lootPoolBuilders.isEmpty()) {
            throw new RuntimeException("Can not use 'useLootTable()' and 'addDrop()' simultaneously");
        }

        if (!lootPoolBuilders.isEmpty()) {
            LootTable.Builder lootTableBuilder = new LootTable.Builder();
            for (LootPool.Builder lootPoolBuilder : lootPoolBuilders) {
                lootTableBuilder.withPool(lootPoolBuilder);
            }
            return new LootTableProvider(lootTableBuilder.build());
        } else if (lootTableLocation != null) {
            return new LootTableProvider(lootTableLocation);
        } else {
            return LootTableProvider.EMPTY;
        }
    }

    protected final LootPool.Builder addLootPoolBuilder() {
        LootPool.Builder builder = new LootPool.Builder();
        builder.name("excompressum-ct-" + UUID.randomUUID());
        lootPoolBuilders.add(builder);
        return builder;
    }

    @ZenCodeType.Method
    @SuppressWarnings("unchecked")
    public final T setLootTable(String resourceLocation) {
        lootTableLocation = new ResourceLocation(resourceLocation);
        updateLootTable(getLootTableProvider());
        return (T) this;
    }

    public abstract void updateLootTable(LootTableProvider provider);
}
