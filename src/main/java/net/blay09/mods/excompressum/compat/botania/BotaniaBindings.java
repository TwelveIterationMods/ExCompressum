package net.blay09.mods.excompressum.compat.botania;

import net.blay09.mods.excompressum.tile.ModTileEntities;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;

public class BotaniaBindings {

    public static ResourceLocation RED_STRING_RELAY = new ResourceLocation("botania", "red_string_relay");

    public static IItemTier manaSteelItemTier = ItemTier.DIAMOND; // TODO botania compat

    public static boolean requestManaExactForTool(ItemStack stack, PlayerEntity attacker, int i, boolean b) {
        return true; // TODO botania compat
    }

    public static Block createOrechidBlock() {
        AbstractBlock.Properties properties = AbstractBlock.Properties.from(Blocks.POPPY);
        if (ModList.get().isLoaded("botania")) {
            return new EvolvedOrechidBlock(properties);
        } else {
            return new Block(properties);
        }
    }

    @Nullable
    public static EvolvedOrechidTileEntity createOrechidTileEntity() {
        AbstractBlock.Properties properties = AbstractBlock.Properties.from(Blocks.POPPY);
        if (ModList.get().isLoaded("botania")) {
            return new EvolvedOrechidTileEntity();
        } else {
            return null;
        }
    }
}
