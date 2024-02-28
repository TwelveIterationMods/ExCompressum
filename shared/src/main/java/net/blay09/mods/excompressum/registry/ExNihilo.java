package net.blay09.mods.excompressum.registry;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.utils.StupidUtils;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

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

    public static boolean isNihiloItem(ItemStack itemStack, ExNihiloProvider.NihiloItems type) {
        ItemStack nihiloStack = instance.getNihiloItem(type);
        return !nihiloStack.isEmpty() && itemStack.getItem() == nihiloStack.getItem();
    }

    public static boolean isHammerable(ItemStack itemStack) {
        BlockState state = StupidUtils.getStateFromItemStack(itemStack);
        return state != null && instance.isHammerable(state);
    }

    public static boolean isSiftableWithMesh(BlockState sieveState, ItemStack itemStack, @Nullable SieveMeshRegistryEntry sieveMesh) {
        BlockState state = StupidUtils.getStateFromItemStack(itemStack);
        return state != null && instance.isSiftableWithMesh(sieveState, state, sieveMesh);
    }

    public static Collection<ItemStack> rollSieveRewards(BlockState sieveState, ItemStack itemStack, SieveMeshRegistryEntry sieveMesh, float luck, RandomSource rand) {
        BlockState state = StupidUtils.getStateFromItemStack(itemStack);
        if (state != null) {
            return instance.rollSieveRewards(sieveState, state, sieveMesh, luck, rand);
        }
        return Collections.emptyList();
    }

    public static boolean hasNihiloMod() {
        return !(instance instanceof NihilisticNihiloProvider);
    }

}
