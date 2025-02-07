package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.tag.ModBlockTags;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;

public class CompressedHammerItem extends DiggerItem {

    public CompressedHammerItem(Tier tier, Item.Properties properties) {
        super(6f, -3.2f, tier, ModBlockTags.MINEABLE_WITH_HAMMER, properties);
    }

}
