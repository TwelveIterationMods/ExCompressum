package net.blay09.mods.excompressum.tile;

public enum EnvironmentalCondition {
    CanSpawn("info.excompressum:baitCanSpawn"),
    NearbyBait("info.excompressum:baitNearbyBait"),
    WrongEnv("info.excompressum:baitWrongEnv"),
    NearbyAnimal("info.excompressum:baitNearbyAnimal"),
    NoWater("info.excompressum:baitNoWater");

    public final String langKey;

    EnvironmentalCondition(String langKey) {
        this.langKey = langKey;
    }
}
