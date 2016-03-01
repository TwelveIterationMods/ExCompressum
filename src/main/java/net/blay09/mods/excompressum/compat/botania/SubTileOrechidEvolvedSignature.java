package net.blay09.mods.excompressum.compat.botania;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.signature.SubTileSignature;

import java.util.List;

public class SubTileOrechidEvolvedSignature extends SubTileSignature {
    @Override
    public void registerIcons(IIconRegister register) {
    }

    @Override
    public IIcon getIconForStack(ItemStack stack) {
        return BotaniaAPI.internalHandler.getSubTileIconForName("orechid");
    }

    @Override
    public String getUnlocalizedNameForStack(ItemStack stack) {
        return "tile.botania:flower.excompressum.orechidEvolved";
    }

    @Override
    public String getUnlocalizedLoreTextForStack(ItemStack stack) {
        return "tile.botania:flower.excompressum.orechidEvolved.reference";
    }

    @Override
    public void addTooltip(ItemStack stack, EntityPlayer player, List<String> tooltip) {
        tooltip.add(EnumChatFormatting.BLUE + StatCollector.translateToLocal("botania.flowerType.functional"));
    }
}
