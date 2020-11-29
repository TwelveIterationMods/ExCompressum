package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.utils.Messages;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class UniversalHammerHead extends Item {

    public static final String name = "universal_hammer_head";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public UniversalHammerHead(Item.Properties properties) {
        super(properties.maxStackSize(1));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(Messages.styledLang("tooltip.universal_hammer_head", TextFormatting.DARK_AQUA));
        tooltip.add(Messages.styledLang("tooltip.best_with_blasting", TextFormatting.DARK_AQUA));
    }

}
