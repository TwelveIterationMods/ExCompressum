package net.blay09.mods.excompressum.menu;

import net.blay09.mods.balm.world.BalmMenuFactory;
import net.blay09.mods.balm.world.inventory.BalmMenuTypeRegistrar;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.entity.AutoCompressorBlockEntity;
import net.blay09.mods.excompressum.block.entity.AutoHammerBlockEntity;
import net.blay09.mods.excompressum.block.entity.AbstractAutoSieveBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

import java.util.Objects;

public class ModMenus {
    public static Holder<MenuType<AutoCompressorMenu>> autoCompressor;
    public static Holder<MenuType<AutoHammerMenu>> autoHammer;
    public static Holder<MenuType<AutoSieveMenu>> autoSieve;

    public static void initialize(BalmMenuTypeRegistrar menus) {
        autoCompressor = menus.register("auto_compressor", new BalmMenuFactory<AutoCompressorMenu, BlockPos>() {
                    @Override
                    public AutoCompressorMenu create(int windowId, Inventory inventory, BlockPos pos) {
                        final var blockEntity = inventory.player.level().getBlockEntity(pos);
                        return new AutoCompressorMenu(windowId, inventory, (AutoCompressorBlockEntity) Objects.requireNonNull(blockEntity));
                    }

                    @Override
                    public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getStreamCodec() {
                        return BlockPos.STREAM_CODEC.cast();
                    }
                }
        ).asHolder();

        autoHammer = menus.register("auto_hammer", new BalmMenuFactory<AutoHammerMenu, BlockPos>() {
                    @Override
                    public AutoHammerMenu create(int windowId, Inventory inventory, BlockPos pos) {
                        final var blockEntity = inventory.player.level().getBlockEntity(pos);
                        return new AutoHammerMenu(windowId, inventory, (AutoHammerBlockEntity) Objects.requireNonNull(blockEntity));
                    }

                    @Override
                    public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getStreamCodec() {
                        return BlockPos.STREAM_CODEC.cast();
                    }
                }
        ).asHolder();

        autoSieve = menus.register("auto_sieve", new BalmMenuFactory<AutoSieveMenu, BlockPos>() {
                    @Override
                    public AutoSieveMenu create(int windowId, Inventory inventory, BlockPos pos) {
                        final var blockEntity = inventory.player.level().getBlockEntity(pos);
                        return new AutoSieveMenu(autoSieve.value(), windowId, inventory, (AbstractAutoSieveBlockEntity) Objects.requireNonNull(blockEntity));
                    }

                    @Override
                    public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getStreamCodec() {
                        return BlockPos.STREAM_CODEC.cast();
                    }
                }
        ).asHolder();
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(ExCompressum.MOD_ID, path);
    }
}
