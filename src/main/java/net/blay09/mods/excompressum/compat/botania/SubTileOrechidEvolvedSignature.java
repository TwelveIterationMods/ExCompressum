package net.blay09.mods.excompressum.compat.botania;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.subtile.signature.SubTileSignature;

import java.util.List;

public class SubTileOrechidEvolvedSignature extends SubTileSignature {
    @Override
    public String getUnlocalizedNameForStack(ItemStack stack) {
        return "tile.botania:flower.excompressum.orechidEvolved";
    }

    @Override
    public String getUnlocalizedLoreTextForStack(ItemStack stack) {
        return "tile.botania:flower.excompressum.orechidEvolved.reference";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addTooltip(ItemStack stack, EntityPlayer player, List<String> tooltip) {
        tooltip.add(TextFormatting.BLUE + I18n.format("botania.flowerType.functional"));
    }
}
