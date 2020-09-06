package net.blay09.mods.excompressum.block;

import net.minecraft.util.IStringSerializable;

import javax.annotation.Nullable;
import java.util.Locale;

public enum CompressedBlockType implements IStringSerializable {
    DUST,
    COBBLESTONE,
    GRAVEL,
    SAND,
    DIRT,
    FLINT,
    NETHER_GRAVEL,
    ENDER_GRAVEL,
    SOUL_SAND,
    NETHERRACK,
    END_STONE;

    public static final CompressedBlockType[] values = values();

    @Override
    public String getString() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @Nullable
    public static CompressedBlockType fromId(int id) {
        return id >= 0 && id < values.length ? values[id] : null;
    }
}
