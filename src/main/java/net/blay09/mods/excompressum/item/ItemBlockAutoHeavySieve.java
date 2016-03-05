package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.registry.AutoSieveSkinRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;

import java.util.List;

public class ItemBlockAutoHeavySieve extends ItemBlock {

    private static final int RANDOM_NAME_CHANGE = 40;

    public ItemBlockAutoHeavySieve(Block block) {
        super(block);
    }

    private int ticksSinceTooltip;
    private String currentRandomName;

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean flag) {
        if(itemStack.stackTagCompound != null && itemStack.stackTagCompound.hasKey("CustomSkin")) {
            list.add("\u00a77" + I18n.format("tooltip.excompressum:auto_heavy_sieve", NBTUtil.func_152459_a(itemStack.stackTagCompound.getCompoundTag("CustomSkin")).getName()));
        } else {
            if(currentRandomName == null) {
                currentRandomName = AutoSieveSkinRegistry.getRandomSkin();
            }
            list.add("\u00a77" + I18n.format("tooltip.excompressum:auto_heavy_sieve", currentRandomName));
        }
        ticksSinceTooltip++;
        if(ticksSinceTooltip >= RANDOM_NAME_CHANGE) {
            currentRandomName = AutoSieveSkinRegistry.getRandomSkin();
            ticksSinceTooltip = 0;
        }
    }
}
