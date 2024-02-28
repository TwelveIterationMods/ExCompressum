package net.blay09.mods.excompressum.block.entity;

public class EnvironmentalConditionResult {
    public static final EnvironmentalConditionResult CanSpawn = new EnvironmentalConditionResult("tooltip.excompressum.baitCanSpawn");
    public static final EnvironmentalConditionResult NearbyBait = new EnvironmentalConditionResult("tooltip.excompressum.baitNearbyBait");
    public static final EnvironmentalConditionResult NearbyAnimal = new EnvironmentalConditionResult("tooltip.excompressum.baitNearbyAnimal");
    public static final EnvironmentalConditionResult NoWater = new EnvironmentalConditionResult("tooltip.excompressum.baitNoWater");

    public static EnvironmentalConditionResult wrongEnv(String listOfBlocks) {
        return new EnvironmentalConditionResult("tooltip.excompressum.baitWrongEnv", listOfBlocks);
    }

    public final String langKey;
    public final Object[] params;

    EnvironmentalConditionResult(String langKey, Object... params) {
        this.langKey = langKey;
        this.params = params;
    }
}
