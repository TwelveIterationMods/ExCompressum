package net.blay09.mods.excompressum.compat.botania;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

interface BotaniaBindings {

    IItemTier getManaSteelItemTier();

    boolean requestManaExactForTool(ItemStack stack, PlayerEntity player, int mana, boolean remove);

    Block createOrechidBlock();

    @Nullable
    TileEntity createOrechidTileEntity();

    @Nullable
    TileEntity createManaSieveTileEntity();

    Block createManaSieveBlock();

    Item createManaHammerItem(Item.Properties properties);
}
