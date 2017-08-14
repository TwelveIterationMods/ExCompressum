package net.blay09.mods.excompressum;

import com.google.common.collect.Lists;
import net.blay09.mods.excompressum.item.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;

public class ExCompressumCreativeTab extends CreativeTabs {
    private final List<ItemStack> additionalItems = Lists.newArrayList();

    public ExCompressumCreativeTab() {
        super(ExCompressum.MOD_ID);
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ModItems.compressedHammerIron);
    }

    @Override
    public void displayAllRelevantItems(NonNullList<ItemStack> list) {
        super.displayAllRelevantItems(list);
        list.addAll(additionalItems);
    }

    public void addAdditionalItem(ItemStack itemStack) {
        additionalItems.add(itemStack);
    }
}
