package net.blay09.mods.excompressum.menu;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.entity.AutoCompressorBlockEntity;
import net.blay09.mods.excompressum.block.entity.AutoHammerBlockEntity;
import net.blay09.mods.excompressum.block.entity.AbstractAutoSieveBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Objects;

public class ModMenus {
    public static DeferredObject<MenuType<AutoCompressorMenu>> autoCompressor;
    public static DeferredObject<MenuType<AutoHammerMenu>> autoHammer;
    public static DeferredObject<MenuType<AutoSieveMenu>> autoSieve;

    public static void initialize(BalmMenus menus) {
        autoCompressor = menus.registerMenu(id("auto_compressor"), ((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            BlockEntity blockEntity = inv.player.level().getBlockEntity(pos);
            return new AutoCompressorMenu(windowId, inv, (AutoCompressorBlockEntity) Objects.requireNonNull(blockEntity));
        }));

        autoHammer = menus.registerMenu(id("auto_hammer"), ((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            BlockEntity blockEntity = inv.player.level().getBlockEntity(pos);
            return new AutoHammerMenu(windowId, inv, (AutoHammerBlockEntity) Objects.requireNonNull(blockEntity));
        }));

        autoSieve = menus.registerMenu(id("auto_sieve"), ((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            BlockEntity blockEntity = inv.player.level().getBlockEntity(pos);
            return new AutoSieveMenu(autoSieve.get(), windowId, inv, (AbstractAutoSieveBlockEntity) Objects.requireNonNull(blockEntity));
        }));
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(ExCompressum.MOD_ID, path);
    }
}
