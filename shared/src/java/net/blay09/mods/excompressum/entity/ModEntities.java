package net.blay09.mods.excompressum.entity;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.entity.BalmEntities;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
    public static DeferredObject<EntityType<AngryChickenEntity>> angryChicken;

    public static void initialize(BalmEntities entities) {
        angryChicken = entities.registerEntity(id("angry_chicken"), EntityType.Builder.of(AngryChickenEntity::new, MobCategory.MONSTER).sized(0.4f, 0.7f), AngryChickenEntity.createEntityAttributes());
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(ExCompressum.MOD_ID, path);
    }

}
