package net.blay09.mods.excompressum.compat.botania;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import vazkii.botania.api.subtile.signature.SubTileSignature;

import java.util.List;

public class SubTileBrokenComprillaSignature extends SubTileSignature {

    public static IIcon icon;

    @Override
    public void registerIcons(IIconRegister register) {
        icon = register.registerIcon(ExCompressum.MOD_ID + ":broken_comprilla");
    }

    @Override
    public IIcon getIconForStack(ItemStack stack) {
        return icon;
    }

    @Override
    public String getUnlocalizedNameForStack(ItemStack stack) {
        return "tile.botania:flower.excompressum.brokenComprilla";
    }

    @Override
    public String getUnlocalizedLoreTextForStack(ItemStack stack) {
        return "tile.botania:flower.excompressum.brokenComprilla.reference";
    }

    @Override
    public void addTooltip(ItemStack stack, EntityPlayer player, List<String> tooltip) {
        tooltip.add(EnumChatFormatting.BLUE + StatCollector.translateToLocal("botania.flowerType.functional"));
    }
}
