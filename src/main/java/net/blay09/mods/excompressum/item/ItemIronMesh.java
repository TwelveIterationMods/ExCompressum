package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemIronMesh extends Item {

    public static final String name = "iron_mesh";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public ItemIronMesh(Item.Properties properties) {
        super(properties.maxDamage(256).maxStackSize(1));
    }

    @Override
    public int getItemEnchantability() {
        return 30;
    }

}
