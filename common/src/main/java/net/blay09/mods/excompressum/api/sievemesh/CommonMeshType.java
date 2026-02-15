package net.blay09.mods.excompressum.api.sievemesh;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.IntFunction;

public enum CommonMeshType implements StringRepresentable {
    STRING,
    FLINT,
    COPPER,
    IRON,
    GOLD,
    DIAMOND,
    EMERALD,
    NETHERITE;

    private static final IntFunction<CommonMeshType> BY_ID = ByIdMap.continuous(CommonMeshType::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StringRepresentable.EnumCodec<CommonMeshType> CODEC = StringRepresentable.fromEnum(CommonMeshType::values);
    public static final StreamCodec<ByteBuf, CommonMeshType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, CommonMeshType::ordinal);
    public static final StreamCodec<ByteBuf, List<CommonMeshType>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new));

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    @Override
    public String toString() {
        return getSerializedName();
    }
}
