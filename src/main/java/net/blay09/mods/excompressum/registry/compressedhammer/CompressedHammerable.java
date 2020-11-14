package net.blay09.mods.excompressum.registry.compressedhammer;

import net.blay09.mods.excompressum.registry.RegistryEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;

public class CompressedHammerable extends RegistryEntry {
    private ResourceLocation source;
    private List<ItemStack> result;
    private ResourceLocation lootTable;

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

    @Override
    public ResourceLocation getId() {
        return getSource();
    }
}
