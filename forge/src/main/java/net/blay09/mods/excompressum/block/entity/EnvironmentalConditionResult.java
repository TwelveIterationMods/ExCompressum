package net.blay09.mods.excompressum.block.entity;

public class EnvironmentalConditionResult {
    public static final EnvironmentalConditionResult CanSpawn = new EnvironmentalConditionResult("excompressum.tooltip.baitCanSpawn");
    public static final EnvironmentalConditionResult NearbyBait = new EnvironmentalConditionResult("excompressum.tooltip.baitNearbyBait");
    public static final EnvironmentalConditionResult NearbyAnimal = new EnvironmentalConditionResult("excompressum.tooltip.baitNearbyAnimal");
    public static final EnvironmentalConditionResult NoWater = new EnvironmentalConditionResult("excompressum.tooltip.baitNoWater");

    public static EnvironmentalConditionResult wrongEnv(String listOfBlocks) {
        return new EnvironmentalConditionResult("excompressum.tooltip.baitWrongEnv", listOfBlocks);
    }

    public final String langKey;
    public final Object[] params;

    EnvironmentalConditionResult(String langKey, Object... params) {
        this.langKey = langKey;
        this.params = params;
    }
}
