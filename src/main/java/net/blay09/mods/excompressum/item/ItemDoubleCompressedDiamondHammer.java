package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDoubleCompressedDiamondHammer extends Item {

    public static final String name = "double_compressed_diamond_hammer";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public ItemDoubleCompressedDiamondHammer(Item.Properties properties) {
        super(properties.maxStackSize(1));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(TextFormatting.DARK_AQUA + I18n.format("tooltip.excompressum:double_compressed_diamond_hammer"));
        tooltip.add(TextFormatting.DARK_AQUA + I18n.format("tooltip.excompressum:best_with_blasting"));
    }

}
