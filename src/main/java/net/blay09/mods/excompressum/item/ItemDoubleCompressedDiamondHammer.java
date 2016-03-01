package net.blay09.mods.excompressum.item;

import cpw.mods.fml.common.registry.GameRegistry;
import exnihilo.registries.HammerRegistry;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.CompressedHammerRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import java.util.List;

public class ItemDoubleCompressedDiamondHammer extends Item {

    public ItemDoubleCompressedDiamondHammer() {
        setCreativeTab(ExCompressum.creativeTab);
        setTextureName(ExCompressum.MOD_ID + ":double_compressed_diamond_hammer");
        setUnlocalizedName(ExCompressum.MOD_ID + ":double_compressed_diamond_hammer");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean flag) {
        super.addInformation(itemStack, entityPlayer, list, flag);
        list.add("\u00a73" + I18n.format("tooltip.excompressum:double_compressed_diamond_hammer"));
    }

    public static void registerRecipes() {
        GameRegistry.addRecipe(new ItemStack(ExCompressum.doubleCompressedDiamondHammer), "##", "##", '#', ExCompressum.compressedHammerDiamond);
    }
}
