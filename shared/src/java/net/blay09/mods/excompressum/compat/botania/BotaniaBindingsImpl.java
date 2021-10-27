package net.blay09.mods.excompressum.compat.botania;

import net.blay09.mods.excompressum.block.ManaSieveBlock;
import net.blay09.mods.excompressum.item.ManaHammerItem;
import net.blay09.mods.excompressum.block.entity.ManaSieveBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.fml.ModList;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.ManaItemHandler;

import javax.annotation.Nullable;

class BotaniaBindingsImpl implements BotaniaBindings {

    @Override
    public Tier getManaSteelItemTier() {
        return BotaniaAPI.instance().getManasteelItemTier();
    }

    @Override
    public boolean requestManaExactForTool(ItemStack stack, Player player, int mana, boolean remove) {
        return ManaItemHandler.instance().requestManaExactForTool(stack, player, mana, remove);
    }

    @Override
    public Block createOrechidBlock() {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.copy(Blocks.POPPY);
        if (isBotaniaLoaded()) {
            return new EvolvedOrechidBlock(properties);
        } else {
            return new Block(properties);
        }
    }

    @Override
    @Nullable
    public BlockEntity createOrechidTileEntity() {
        if (isBotaniaLoaded()) {
            return new EvolvedOrechidBlockEntity();
        } else {
            return null;
        }
    }

    @Override
    @Nullable
    public BlockEntity createManaSieveTileEntity() {
        if (isBotaniaLoaded()) {
            return new ManaSieveBlockEntity();
        } else {
            return null;
        }
    }

    @Override
    public Block createManaSieveBlock() {
        if (isBotaniaLoaded()) {
            return new ManaSieveBlock();
        } else {
            return new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK));
        }
    }

    @Override
    public Item createManaHammerItem(Item.Properties properties) {
        return new ManaHammerItem(properties);
    }

    private static boolean isBotaniaLoaded() {
        return ModList.get().isLoaded("botania");
    }

}
