package net.blay09.mods.excompressum.compat;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.IForgeRegistry;

public interface IAddon {
    default void preInit() {}
    default void init() {}
    default void postInit() {}

    default void registerModels() {}
    default void registerItems(IForgeRegistry<Item> registry) {}
    default void registerBlocks(IForgeRegistry<Block> registry) {}

	default void registriesComplete() {}
}
