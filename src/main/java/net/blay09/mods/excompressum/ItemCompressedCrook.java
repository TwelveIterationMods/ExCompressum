package net.blay09.mods.excompressum;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exnihilo.items.ItemCrook;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;

public class ItemCompressedCrook extends ItemCrook {

    protected ItemCompressedCrook() {
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
}
