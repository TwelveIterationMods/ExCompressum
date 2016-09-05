package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public class ItemCompressedCrook extends Item { // TODO ItemCrook

    public ItemCompressedCrook() {
        super(ToolMaterial.WOOD);
        setRegistryName("compressed_crook");
        setUnlocalizedName(getRegistryName().toString());
        setMaxDamage((int) (ToolMaterial.WOOD.getMaxUses() * 2 * ExCompressum.compressedCrookDurabilityMultiplier));
        setCreativeTab(ExCompressum.creativeTab);
        efficiencyOnProperMaterial *= ExCompressum.compressedCrookSpeedMultiplier;
    }

    public static void registerRecipes(Configuration config) {
        if(config.getBoolean("Compressed Crook", "items", true, "If set to false, the recipe for the compressed crook will be disabled.")) {
            Item itemCrook = GameRegistry.findItem("exnihilo", "crook");
            if(itemCrook != null) {
                GameRegistry.addRecipe(new ItemStack(ModItems.compressedCrook), "## ", " # ", " # ", '#', itemCrook);
            }
        }
    }
}
