package net.blay09.mods.excompressum.container;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.tile.AutoCompressorTileEntity;
import net.blay09.mods.excompressum.tile.AutoHammerTileEntity;
import net.blay09.mods.excompressum.tile.AutoSieveTileEntityBase;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = ExCompressum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModContainers {
    public static ContainerType<AutoCompressorContainer> autoCompressor;
    public static ContainerType<AutoHammerContainer> autoHammer;
    public static ContainerType<AutoSieveContainer> autoSieve;
    public static ContainerType<AutoSieveContainer> manaSieve;

    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
        final IForgeRegistry<ContainerType<?>> registry = event.getRegistry();
        registry.register(autoCompressor = register("auto_compressor", ((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            return new AutoCompressorContainer(windowId, inv, (AutoCompressorTileEntity) Objects.requireNonNull(tileEntity));
        })));

        registry.register(autoHammer = register("auto_hammer", ((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            return new AutoHammerContainer(windowId, inv, (AutoHammerTileEntity) Objects.requireNonNull(tileEntity));
        })));

        registry.register(autoSieve = register("auto_sieve", ((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            return new AutoSieveContainer(autoSieve, windowId, inv, (AutoSieveTileEntityBase) Objects.requireNonNull(tileEntity));
        })));

        registry.register(manaSieve = register("mana_sieve", ((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            return new AutoSieveContainer(autoSieve, windowId, inv, (AutoSieveTileEntityBase) Objects.requireNonNull(tileEntity));
        })));
    }

    @SuppressWarnings("unchecked")
    private static <T extends Container> ContainerType<T> register(String name, IContainerFactory<T> containerFactory) {
        return (ContainerType<T>) new ContainerType<>(containerFactory).setRegistryName(name);
    }
}
