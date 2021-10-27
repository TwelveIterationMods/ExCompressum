package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class WoodChippingItem extends Item {

    public static final String name = "wood_chippings";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public WoodChippingItem(Item.Properties properties) {
        super(properties);
    }
}
