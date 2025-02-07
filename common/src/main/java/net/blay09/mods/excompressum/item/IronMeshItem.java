package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class IronMeshItem extends Item {

    public static final String name = "iron_mesh";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public IronMeshItem(Item.Properties properties) {
        super(properties.durability(256));
    }

    @Override
    public int getEnchantmentValue() {
        return 30;
    }

}
