package net.blay09.mods.excompressum.compat.botania;

import net.blay09.mods.excompressum.tile.ManaSieveTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;

public class BotaniaCompat {

    private static boolean failedToGetManaParticle;

    public static boolean requestManaExactForTool(ItemStack stack, PlayerEntity player, int mana, boolean remove) {
        return isBotaniaLoaded() && BotaniaBindings.requestManaExactForTool(stack, player, mana, remove);
    }

    public static Block createOrechidBlock() {
        AbstractBlock.Properties properties = AbstractBlock.Properties.from(Blocks.POPPY);
        if (isBotaniaLoaded()) {
            return BotaniaBindings.createOrechidBlock();
        } else {
            return new Block(properties);
        }
    }

    @Nullable
    public static EvolvedOrechidTileEntity createOrechidTileEntity() {
        if (isBotaniaLoaded()) {
            return BotaniaBindings.createOrechidTileEntity();
        } else {
            return null;
        }
    }

    @Nullable
    public static ManaSieveTileEntity createManaSieveTileEntity() {
        if (isBotaniaLoaded()) {
            return BotaniaBindings.createManaSieveTileEntity();
        } else {
            return null;
        }
    }

    public static Block createManaSieveBlock() {
        if (isBotaniaLoaded()) {
            return BotaniaBindings.createManaSieveBlock();
        } else {
            return new Block(AbstractBlock.Properties.from(Blocks.IRON_BLOCK));
        }
    }

    public static IParticleData getManaParticle() {
        if (isBotaniaLoaded() && !failedToGetManaParticle) {
            try {
                return BotaniaInternalBindings.createWispParticle();
            } catch (Throwable ignored) {
                failedToGetManaParticle = true;
            }
        }

        return ParticleTypes.FLASH;
    }

    public static IItemTier getManaSteelItemTier() {
        return isBotaniaLoaded() ? BotaniaBindings.getManaSteelItemTier() : ItemTier.IRON;
    }

    private static boolean isBotaniaLoaded() {
        return ModList.get().isLoaded("botania");
    }

    public static Item createManaHammerItem(Item.Properties properties) {
        if (isBotaniaLoaded()) {
            return BotaniaBindings.createManaHammerItem(properties);
        } else {
            return new Item(properties);
        }
    }
}
