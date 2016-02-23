package net.blay09.mods.excompressum.item;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exnihilo.items.ItemCrook;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public class ItemCompressedCrook extends ItemCrook {

    public ItemCompressedCrook() {
        super(ToolMaterial.WOOD);
        setTextureName(ExCompressum.MOD_ID + ":compressed_crook");
        setMaxDamage((int) (ToolMaterial.WOOD.getMaxUses() * ExCompressum.compressedCrookDurabilityMultiplier));
        efficiencyOnProperMaterial *= ExCompressum.compressedCrookSpeedMultiplier;
    }

    @Override
    public String getUnlocalizedName() {
        return "item." + ExCompressum.MOD_ID + ":compressed_crook";
    }

    @Override
    public String getUnlocalizedName(ItemStack item) {
        return "item." + ExCompressum.MOD_ID + ":compressed_crook";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        this.itemIcon = register.registerIcon(iconString);
    }

    public static void registerRecipes(Configuration config) {
        if(config.getBoolean("Compressed Crook", "general", true, "If set to false, the recipe for the compressed crook will be disabled.")) {
            Item itemCrook = GameRegistry.findItem("exnihilo", "crook");
            if(itemCrook != null) {
                GameRegistry.addRecipe(new ItemStack(ExCompressum.compressedCrook), "## ", " # ", " # ", '#', itemCrook);
            }
        }
    }
}
