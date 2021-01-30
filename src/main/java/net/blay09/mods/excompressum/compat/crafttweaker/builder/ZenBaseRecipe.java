package net.blay09.mods.excompressum.compat.crafttweaker.builder;


import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import net.blay09.mods.excompressum.registry.LootTableProvider;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.exnihilosequentia.ZenSieveRecipe")
public abstract class ZenBaseRecipe<T> {

    private LootPool.Builder lootPoolBuilder;
    private ResourceLocation lootTableLocation;

    public final LootTableProvider getLootTableProvider() {
        if (lootTableLocation != null && lootPoolBuilder != null) {
            throw new RuntimeException("Can not use 'useLootTable()' and 'addDrop()' simultaneously");
        }

        if (lootPoolBuilder != null) {
            LootTable.Builder lootTableBuilder = new LootTable.Builder();
            lootTableBuilder.addLootPool(lootPoolBuilder);
            return new LootTableProvider(lootTableBuilder.build());
        } else if (lootTableLocation != null) {
            return new LootTableProvider(lootTableLocation);
        } else {
            return LootTableProvider.EMPTY;
        }
    }

    protected final LootPool.Builder getLootPoolBuilder() {
        if (lootPoolBuilder == null) {
            lootPoolBuilder = new LootPool.Builder();
        }

        return lootPoolBuilder;
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
