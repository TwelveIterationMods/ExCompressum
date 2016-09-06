package net.blay09.mods.excompressum.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Deprecated
public class ItemAndMetadata {

    public final Item item;
    public final int metadata;

    public ItemAndMetadata(Item item, int metadata) {
        this.item = item;
        this.metadata = metadata;
    }

    public ItemAndMetadata(Block block, int metadata) {
        this.item = Item.getItemFromBlock(block);
        this.metadata = metadata;
    }

    public ItemAndMetadata(ItemStack itemStack) {
        this.item = itemStack.getItem();
        this.metadata = itemStack.getItemDamage();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemAndMetadata that = (ItemAndMetadata) o;
        return metadata == that.metadata && (item != null ? item.equals(that.item) : that.item == null);
    }

    @Override
    public int hashCode() {
        int result = item != null ? item.hashCode() : 0;
        result = 31 * result + metadata;
        return result;
    }

    public ItemStack createStack(int stackSize) {
        return new ItemStack(item, stackSize, metadata);
    }
}
