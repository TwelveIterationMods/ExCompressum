package net.blay09.mods.excompressum;

import com.google.common.collect.Lists;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ExCompressumCreativeTab extends CreativeTabs {

    private final List<ItemStack> additionalItems = Lists.newArrayList();

    public ExCompressumCreativeTab() {
        super(ExCompressum.MOD_ID);
    }

    @Override
    public Item getTabIconItem() {
        return ModItems.compressedHammerIron;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void displayAllReleventItems(List list) {
        super.displayAllReleventItems(list);
        list.addAll(additionalItems);
    }

    public void addAdditionalItem(ItemStack itemStack) {
        additionalItems.add(itemStack);
    }
}
