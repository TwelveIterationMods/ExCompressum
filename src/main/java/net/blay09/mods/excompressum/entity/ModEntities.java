package net.blay09.mods.excompressum.entity;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = ExCompressum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static EntityType<AngryChickenEntity> angryChicken;

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        final IForgeRegistry<EntityType<?>> registry = event.getRegistry();
        registry.register(angryChicken = registerEntity(EntityType.Builder.create(AngryChickenEntity::new, EntityClassification.MONSTER).size(0.4f, 0.7f), "angry_chicken"));
        GlobalEntityTypeAttributes.put(angryChicken, AngryChickenEntity.createEntityAttributes().create());
    }

    @SuppressWarnings("unchecked")
    private static <T extends Entity> EntityType<T> registerEntity(EntityType.Builder<T> builder, String name) {
        return (EntityType<T>) builder.build(name).setRegistryName(name);
    }
}
