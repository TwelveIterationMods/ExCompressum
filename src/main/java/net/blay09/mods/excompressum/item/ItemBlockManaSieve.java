package net.blay09.mods.excompressum.item;

import com.mojang.authlib.GameProfile;
import net.blay09.mods.excompressum.registry.AutoSieveSkinRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class ItemBlockManaSieve extends ItemBlock {

    public ItemBlockManaSieve(Block block) {
        super(block);
    }

    private ItemStack lastHoverStack;
    private String currentRandomName;

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean flag) {
        NBTTagCompound tagCompound = itemStack.getTagCompound();
        if(tagCompound != null && tagCompound.hasKey("CustomSkin")) {
            GameProfile customSkin = NBTUtil.readGameProfileFromNBT(tagCompound.getCompoundTag("CustomSkin"));
            if(customSkin != null) {
                list.add(TextFormatting.GRAY + I18n.format("tooltip.excompressum:mana_sieve", customSkin.getName()));
            }
        } else {
            if(currentRandomName == null) {
                currentRandomName = AutoSieveSkinRegistry.getRandomSkin();
            }
            list.add(TextFormatting.GRAY + I18n.format("tooltip.excompressum:mana_sieve", currentRandomName));
        }
        if(lastHoverStack != itemStack) {
            currentRandomName = AutoSieveSkinRegistry.getRandomSkin();
            lastHoverStack = itemStack;
        }
    }
}
