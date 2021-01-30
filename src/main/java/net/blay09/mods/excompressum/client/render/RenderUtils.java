package net.blay09.mods.excompressum.client.render;


import net.minecraft.util.Direction;

public class RenderUtils {

    /**
     * @deprecated use facing.getHorizontalAngle() instead
     */
    @Deprecated
    public static float getRotationAngle(Direction facing) {
        switch (facing) {
            case NORTH:
                return 0;
            case EAST:
                return -90;
            case SOUTH:
                return 180;
            case WEST:
                return 90;
            default:
                return -90;
        }
    }
}
