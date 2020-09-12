package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class UncompressedCoalItem extends Item {

    public static final String name = "uncompressed_coal";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public UncompressedCoalItem(Properties properties) {
        super(properties);
    }

    /* TODO public int getItemBurnTime(ItemStack itemStack) {
        return 200;
    }*/

}
