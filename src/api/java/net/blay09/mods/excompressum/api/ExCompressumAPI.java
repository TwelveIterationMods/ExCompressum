package net.blay09.mods.excompressum.api;

import net.blay09.mods.excompressum.api.compressedhammer.CompressedHammerReward;
import net.blay09.mods.excompressum.api.heavysieve.HeavySieveReward;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * To register stuff, subscribe to the appropriate {@link net.blay09.mods.excompressum.api.ReloadRegistryEvent}.
 */
public class ExCompressumAPI {
    private static InternalMethods internalMethods;

    /**
     * INTERNAL USE ONLY
     */
    public static void __setupAPI(InternalMethods internalMethods) {
        ExCompressumAPI.internalMethods = internalMethods;
    }

    public static ExNihiloProvider getExNihilo() {
        return internalMethods.getExNihilo();
    }

    public static void registerCompressedHammerEntry(BlockState state, List<CompressedHammerReward> rewards) {
        internalMethods.registerCompressedHammerEntry(state, rewards);
    }

    public static void registerHeavySieveEntry(BlockState state, List<HeavySieveReward> rewards) {
        internalMethods.registerHeavySieveEntry(state, rewards);
    }

    public static void registerWoodenCrucibleEntry(ItemStack itemStack, Fluid fluid, int amount) {
        internalMethods.registerWoodenCrucibleEntry(itemStack, fluid, amount);
    }
}
