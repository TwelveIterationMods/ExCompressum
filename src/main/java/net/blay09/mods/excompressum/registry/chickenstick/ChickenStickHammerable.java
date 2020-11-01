package net.blay09.mods.excompressum.registry.chickenstick;

import net.blay09.mods.excompressum.registry.RegistryEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ChickenStickHammerable extends RegistryEntry {
    private ResourceLocation source;
    private ItemStack result;

    public ResourceLocation getSource() {
        return source;
    }

    public void setSource(ResourceLocation source) {
        this.source = source;
    }

    @Nullable
    public ItemStack getResult() {
        return result;
    }

    public void setResult(@Nullable ItemStack result) {
        this.result = result;
    }

    @Override
    public ResourceLocation getId() {
        return getSource();
    }
}
