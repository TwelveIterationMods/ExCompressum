package net.blay09.mods.excompressum.client.render;


import net.minecraft.core.Direction;

public class RenderUtils {

    /**
     * @deprecated use facing.getHorizontalAngle() instead
     */
    @Deprecated
    public static float getRotationAngle(Direction facing) {
        return switch (facing) {
            case NORTH -> 0;
            case EAST -> -90;
            case SOUTH -> 180;
            case WEST -> 90;
            default -> -90;
        };
    }
}
