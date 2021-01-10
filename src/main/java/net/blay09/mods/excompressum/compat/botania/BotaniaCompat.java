package net.blay09.mods.excompressum.compat.botania;

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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;

public class BotaniaCompat {

    public static final String MOD_ID = "botania";
    public static ResourceLocation RED_STRING_RELAY = new ResourceLocation("botania", "red_string_relay");

    private static BotaniaBindings bindings;
    private static BotaniaInternalBindings internalBindings;
    private static boolean failedToGetManaParticle;

    static {
        if (isBotaniaLoaded()) {
            try {
                bindings = (BotaniaBindings) Class.forName("net.blay09.mods.excompressum.compat.botania.BotaniaBindingsImpl").newInstance();
                internalBindings = (BotaniaInternalBindings) Class.forName("net.blay09.mods.excompressum.compat.botania.BotaniaInternalBindingsImpl").newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean requestManaExactForTool(ItemStack stack, PlayerEntity player, int mana, boolean remove) {
        return isBotaniaLoaded() && bindings.requestManaExactForTool(stack, player, mana, remove);
    }

    public static Block createOrechidBlock() {
        if (isBotaniaLoaded()) {
            return bindings.createOrechidBlock();
        } else {
            AbstractBlock.Properties properties = AbstractBlock.Properties.from(Blocks.POPPY);
            return new Block(properties);
        }
    }

    @Nullable
    public static TileEntity createOrechidTileEntity() {
        if (isBotaniaLoaded()) {
            return bindings.createOrechidTileEntity();
        } else {
            return null;
        }
    }

    @Nullable
    public static TileEntity createManaSieveTileEntity() {
        if (isBotaniaLoaded()) {
            return bindings.createManaSieveTileEntity();
        } else {
            return null;
        }
    }

    public static Block createManaSieveBlock() {
        if (isBotaniaLoaded()) {
            return bindings.createManaSieveBlock();
        } else {
            return new Block(AbstractBlock.Properties.from(Blocks.IRON_BLOCK).noDrops());
        }
    }

    public static IParticleData getManaParticle() {
        if (isBotaniaLoaded() && !failedToGetManaParticle && internalBindings != null) {
            try {
                return internalBindings.createWispParticle();
            } catch (Throwable ignored) {
                failedToGetManaParticle = true;
            }
        }

        return ParticleTypes.HAPPY_VILLAGER;
    }

    public static IItemTier getManaSteelItemTier() {
        return isBotaniaLoaded() ? bindings.getManaSteelItemTier() : ItemTier.IRON;
    }

    private static boolean isBotaniaLoaded() {
        return ModList.get().isLoaded("botania");
    }

    public static Item createManaHammerItem(Item.Properties properties) {
        if (isBotaniaLoaded()) {
            return bindings.createManaHammerItem(properties);
        } else {
            return new Item(properties);
        }
    }
}
