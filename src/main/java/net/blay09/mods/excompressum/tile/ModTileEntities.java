package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.*;
import net.blay09.mods.excompressum.compat.botania.BotaniaBindings;
import net.blay09.mods.excompressum.compat.botania.EvolvedOrechidTileEntity;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ExCompressum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModTileEntities {
    public static TileEntityType<AutoHammerTileEntity> autoHammer;
    public static TileEntityType<AutoCompressedHammerTileEntity> autoCompressedHammer;
    public static TileEntityType<AutoCompressorTileEntity> autoCompressor;
    public static TileEntityType<RationingAutoCompressorTileEntity> rationingAutoCompressor;
    public static TileEntityType<AutoSieveTileEntity> autoSieve;
    public static TileEntityType<ManaSieveTileEntity> manaSieve;
    public static TileEntityType<HeavySieveTileEntity> heavySieve;
    public static TileEntityType<AutoHeavySieveTileEntity> autoHeavySieve;
    public static TileEntityType<WoodenCrucibleTileEntity> woodenCrucible;
    public static TileEntityType<BaitTileEntity> bait;
    public static TileEntityType<EvolvedOrechidTileEntity> evolvedOrechid;

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        final IForgeRegistry<TileEntityType<?>> registry = event.getRegistry();
        registry.registerAll(
                autoHammer = build(AutoHammerTileEntity::new, AutoHammerBlock.registryName, ModBlocks.autoHammer),
                autoCompressedHammer = build(AutoCompressedHammerTileEntity::new, AutoCompressedHammerBlock.registryName, ModBlocks.autoCompressedHammer),
                autoCompressor = build(AutoCompressorTileEntity::new, AutoCompressorBlock.registryName, ModBlocks.autoCompressor),
                rationingAutoCompressor = build(RationingAutoCompressorTileEntity::new, RationingAutoCompressorBlock.registryName, ModBlocks.rationingAutoCompressor),
                autoSieve = build(AutoSieveTileEntity::new, AutoSieveBlock.registryName, ModBlocks.autoSieve),
                manaSieve = build(ManaSieveTileEntity::new, ManaSieveBlock.registryName, ModBlocks.manaSieve),
                heavySieve = build(HeavySieveTileEntity::new, new ResourceLocation(ExCompressum.MOD_ID, "heavy_sieve"), ModBlocks.heavySieves),
                autoHeavySieve = build(AutoHeavySieveTileEntity::new, AutoHeavySieveBlock.registryName, ModBlocks.autoHeavySieve),
                woodenCrucible = build(WoodenCrucibleTileEntity::new, new ResourceLocation(ExCompressum.MOD_ID, "wooden_crucible"), ModBlocks.woodenCrucibles),
                bait = build(BaitTileEntity::new, new ResourceLocation(ExCompressum.MOD_ID, "bait"), ModBlocks.baits),
                evolvedOrechid = build(BotaniaBindings::createOrechidTileEntity, new ResourceLocation(ExCompressum.MOD_ID, "evolved_orechid"), ModBlocks.evolvedOrechid)
        );
    }

    @SuppressWarnings("unchecked")
    private static <T extends TileEntity> TileEntityType<T> build(Supplier<T> factory, ResourceLocation registryName, Block... block) {
        //noinspection ConstantConditions
        return (TileEntityType<T>) TileEntityType.Builder.create(factory, block).build(null).setRegistryName(registryName);
    }

}
