package net.blay09.mods.excompressum.tile;

public enum EnvironmentalCondition {
    CanSpawn("excompressum.tooltip.baitCanSpawn"),
    NearbyBait("excompressum.tooltip.baitNearbyBait"),
    WrongEnv("excompressum.tooltip.baitWrongEnv"),
    NearbyAnimal("excompressum.tooltip.baitNearbyAnimal"),
    NoWater("excompressum.tooltip.baitNoWater");

    public final String langKey;

    EnvironmentalCondition(String langKey) {
        this.langKey = langKey;
    }
}
