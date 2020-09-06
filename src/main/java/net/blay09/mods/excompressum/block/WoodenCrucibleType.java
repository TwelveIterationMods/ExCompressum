package net.blay09.mods.excompressum.block;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum WoodenCrucibleType implements IStringSerializable {
    OAK,
    SPRUCE,
    BIRCH,
    JUNGLE,
    ACACIA,
    DARK_OAK;

    public static WoodenCrucibleType[] values = values();

    @Override
    public String getString() {
        return name().toLowerCase(Locale.ENGLISH);
    }
}
