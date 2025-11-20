package net.blay09.mods.excompressum.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.core.component.BalmDataComponentTypeRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.util.Unit;

public class ModComponents {

    public static Holder<DataComponentType<Integer>> energy;
    public static Holder<DataComponentType<Unit>> angry;

    public static void initialize(BalmDataComponentTypeRegistrar components) {
        energy = components.register("energy", Codec.INT).asHolder();
        angry = components.register("angry", MapCodec.unitCodec(Unit.INSTANCE)).asHolder();
    }
}
