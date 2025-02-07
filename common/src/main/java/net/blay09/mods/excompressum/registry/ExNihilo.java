package net.blay09.mods.excompressum.registry;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.utils.StupidUtils;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.Collections;

public abstract class ExNihilo {

    private static ExNihiloProvider instance;

    public static void setInstance(ExNihiloProvider instance) {
        ExNihilo.instance = instance;
    }

    public static ExNihiloProvider getInstance() {
        if (instance == null) {
            ExCompressum.logger.warn("No Ex Nihilo mod installed - many things will be disabled.");
            instance = new NihilisticNihiloProvider();
        }
        return instance;
    }

    public static boolean isHammerable(Level level, ItemStack itemStack) {
        BlockState state = StupidUtils.getStateFromItemStack(itemStack);
        return state != null && getInstance().isHammerable(level, state);
    }

    public static boolean isSiftableWithMesh(Level level, BlockState sieveState, ItemStack itemStack, @Nullable SieveMeshRegistryEntry sieveMesh) {
        BlockState state = StupidUtils.getStateFromItemStack(itemStack);
        return state != null && getInstance().isSiftableWithMesh(level, sieveState, state, sieveMesh);
    }

    public static Collection<ItemStack> rollSieveRewards(Level level, BlockState sieveState, ItemStack itemStack, SieveMeshRegistryEntry sieveMesh, float luck, RandomSource rand) {
        BlockState state = StupidUtils.getStateFromItemStack(itemStack);
        if (state != null) {
            return getInstance().rollSieveRewards(level, sieveState, state, sieveMesh, luck, rand);
        }
        return Collections.emptyList();
    }

    public static boolean hasNihiloMod() {
        return !(getInstance() instanceof NihilisticNihiloProvider);
    }

}
