package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.utils.Messages;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class UniversalHammerHeadItem extends Item {

    public static final String name = "universal_hammer_head";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public UniversalHammerHeadItem(Item.Properties properties) {
        //noinspection ConstantConditions - TConstruct is not yet released and this modifier does not work yet - so hide it!
        super(properties.stacksTo(1).tab(null));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Messages.styledLang("tooltip.universal_hammer_head", ChatFormatting.DARK_AQUA));
        tooltip.add(Messages.styledLang("tooltip.best_with_blasting", ChatFormatting.DARK_AQUA));
    }

}
