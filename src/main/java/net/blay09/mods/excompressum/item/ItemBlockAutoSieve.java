package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.registry.AutoSieveSkinRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;

import java.util.List;

public class ItemBlockAutoSieve extends ItemBlock {

    public ItemBlockAutoSieve(Block block) {
        super(block);
    }

    private ItemStack lastHoverStack;
    private String currentRandomName;

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean flag) {
        if(itemStack.stackTagCompound != null && itemStack.stackTagCompound.hasKey("CustomSkin")) {
            list.add("\u00a77" + I18n.format("tooltip.excompressum:auto_sieve", NBTUtil.func_152459_a(itemStack.stackTagCompound.getCompoundTag("CustomSkin")).getName()));
        } else {
            if(currentRandomName == null) {
                currentRandomName = AutoSieveSkinRegistry.getRandomSkin();
            }
            list.add("\u00a77" + I18n.format("tooltip.excompressum:auto_sieve", currentRandomName));
        }
        if(lastHoverStack != itemStack) {
            currentRandomName = AutoSieveSkinRegistry.getRandomSkin();
            lastHoverStack = itemStack;
        }
    }
}
