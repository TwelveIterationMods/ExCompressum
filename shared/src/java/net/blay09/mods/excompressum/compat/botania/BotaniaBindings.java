package net.blay09.mods.excompressum.compat.botania;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;

interface BotaniaBindings {

    Tier getManaSteelItemTier();

    boolean requestManaExactForTool(ItemStack stack, Player player, int mana, boolean remove);

    Block createOrechidBlock();

    @Nullable
    BlockEntity createOrechidTileEntity();

    @Nullable
    BlockEntity createManaSieveTileEntity();

    Block createManaSieveBlock();

    Item createManaHammerItem(Item.Properties properties);
}
