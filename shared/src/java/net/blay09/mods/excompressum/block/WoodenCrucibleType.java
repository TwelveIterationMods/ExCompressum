package net.blay09.mods.excompressum.block;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum WoodenCrucibleType implements StringRepresentable {
    OAK,
    SPRUCE,
    BIRCH,
    JUNGLE,
    ACACIA,
    DARK_OAK;

    public static WoodenCrucibleType[] values = values();

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ENGLISH);
    }
}
