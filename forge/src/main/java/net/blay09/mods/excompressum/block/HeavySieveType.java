package net.blay09.mods.excompressum.block;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum HeavySieveType implements StringRepresentable {
    OAK,
    SPRUCE,
    BIRCH,
    JUNGLE,
    ACACIA,
    DARK_OAK;

    public static HeavySieveType[] values = values();

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ENGLISH);
    }
}
