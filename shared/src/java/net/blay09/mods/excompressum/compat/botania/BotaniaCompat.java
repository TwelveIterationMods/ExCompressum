package net.blay09.mods.excompressum.compat.botania;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
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

    public static boolean requestManaExactForTool(ItemStack stack, Player player, int mana, boolean remove) {
        return isBotaniaLoaded() && bindings.requestManaExactForTool(stack, player, mana, remove);
    }

    public static Block createOrechidBlock() {
        if (isBotaniaLoaded()) {
            return bindings.createOrechidBlock();
        } else {
            BlockBehaviour.Properties properties = BlockBehaviour.Properties.copy(Blocks.POPPY);
            return new Block(properties);
        }
    }

    @Nullable
    public static BlockEntity createOrechidTileEntity() {
        if (isBotaniaLoaded()) {
            return bindings.createOrechidTileEntity();
        } else {
            return null;
        }
    }

    @Nullable
    public static BlockEntity createManaSieveTileEntity() {
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
            return new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noDrops());
        }
    }

    public static ParticleOptions getManaParticle() {
        if (isBotaniaLoaded() && !failedToGetManaParticle && internalBindings != null) {
            try {
                return internalBindings.createWispParticle();
            } catch (Throwable ignored) {
                failedToGetManaParticle = true;
            }
        }

        return ParticleTypes.HAPPY_VILLAGER;
    }

    public static Tier getManaSteelItemTier() {
        return isBotaniaLoaded() ? bindings.getManaSteelItemTier() : Tiers.IRON;
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
