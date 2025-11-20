package net.blay09.mods.excompressum.entity;

import net.blay09.mods.balm.world.entity.BalmEntityTypeRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
    public static Holder<EntityType<AngryChickenEntity>> angryChicken;

    public static void initialize(BalmEntityTypeRegistrar entities) {
        angryChicken = entities.register("angry_chicken", () -> EntityType.Builder.of(AngryChickenEntity::new, MobCategory.MONSTER).sized(0.4f, 0.7f))
                .withDefaultAttributes(AngryChickenEntity::createEntityAttributes)
                .asHolder();
    }
}
