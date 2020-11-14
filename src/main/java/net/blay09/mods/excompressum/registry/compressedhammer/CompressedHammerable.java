package net.blay09.mods.excompressum.registry.compressedhammer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.RegistryEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootSerializers;
import net.minecraft.loot.LootTable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;
import java.util.List;

public class CompressedHammerable extends RegistryEntry {

    private static final Gson GSON_INSTANCE = LootSerializers.func_237388_c_().create();

    private ResourceLocation source;
    private List<ItemStack> result;
    private ResourceLocation lootTable;
    private JsonObject loot;
    private LootTable cachedLootTable;

    public ResourceLocation getSource() {
        return source;
    }

    public void setSource(ResourceLocation source) {
        this.source = source;
    }

    @Nullable
    public List<ItemStack> getResult() {
        return result;
    }

    public void setResult(@Nullable List<ItemStack> result) {
        this.result = result;
    }

    @Nullable
    public ResourceLocation getLootTable() {
        return lootTable;
    }

    public void setLootTable(@Nullable ResourceLocation lootTable) {
        this.lootTable = lootTable;
    }

    @Nullable
    public JsonObject getLoot() {
        return loot;
    }

    public void setLoot(@Nullable JsonObject loot) {
        this.loot = loot;
    }

    @Nullable
    public LootTable getLootTable(LootContext context) {
        if (lootTable != null) {
            return context.getLootTable(lootTable);
        } else if (loot != null) {
            if (cachedLootTable == null) {
                cachedLootTable = ForgeHooks.loadLootTable(GSON_INSTANCE, new ResourceLocation(ExCompressum.MOD_ID, getSource().getPath() + "_embed"), loot, true, context.getWorld().getServer().getLootTableManager());
            }
        }
        return cachedLootTable;
    }

    @Override
    public ResourceLocation getId() {
        return getSource();
    }
}
