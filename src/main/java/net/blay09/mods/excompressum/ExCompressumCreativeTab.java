package net.blay09.mods.excompressum;

import com.google.common.collect.Lists;
import net.blay09.mods.excompressum.item.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;

public class ExCompressumCreativeTab extends ItemGroup {
    private final List<ItemStack> additionalItems = Lists.newArrayList();

    public ExCompressumCreativeTab() {
        super(ExCompressum.MOD_ID);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModItems.compressedHammerIron);
    }

    @Override
    public void fill(NonNullList<ItemStack> items) {
        super.fill(items);
        items.addAll(additionalItems);
    }

    public void addAdditionalItem(ItemStack itemStack) {
        additionalItems.add(itemStack);
    }
}
