package net.blay09.mods.excompressum.block;

import com.google.common.collect.Lists;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.block.entity.BaitBlockStateCondition;
import net.blay09.mods.excompressum.block.entity.BaitBlockTagCondition;
import net.blay09.mods.excompressum.block.entity.BaitEnvironmentCondition;
import net.blay09.mods.excompressum.block.entity.BaitFluidCondition;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

import org.jspecify.annotations.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public enum BaitType implements StringRepresentable {
    WOLF(new ItemStackTemplate(Items.BEEF), new ItemStackTemplate(Items.BONE), EntityType.WOLF, () -> ExCompressumConfig.getActive().baits.wolfBaitChance, 14144467, 13545366),
    OCELOT(new ItemStackTemplate(Items.GUNPOWDER), new ItemStackTemplate(Items.COD), EntityType.OCELOT, () -> ExCompressumConfig.getActive().baits.ocelotBaitChance, 15720061, 5653556),
    COW(new ItemStackTemplate(Items.WHEAT), new ItemStackTemplate(Items.WHEAT), EntityType.COW, () -> ExCompressumConfig.getActive().baits.cowBaitChance, 4470310, 10592673),
    PIG(new ItemStackTemplate(Items.CARROT), new ItemStackTemplate(Items.CARROT), EntityType.PIG, () -> ExCompressumConfig.getActive().baits.pigBaitChance, 15771042, 14377823),
    CHICKEN(new ItemStackTemplate(Items.WHEAT_SEEDS), new ItemStackTemplate(Items.WHEAT_SEEDS), EntityType.CHICKEN, () -> ExCompressumConfig.getActive().baits.chickenBaitChance,  10592673, 16711680),
    SHEEP(new ItemStackTemplate(Items.WHEAT_SEEDS), new ItemStackTemplate(Items.WHEAT), EntityType.SHEEP, () -> ExCompressumConfig.getActive().baits.sheepBaitChance,  15198183, 16758197),
    SQUID(new ItemStackTemplate(Items.COD), new ItemStackTemplate(Items.COD), EntityType.SQUID, () -> ExCompressumConfig.getActive().baits.squidBaitChance,  2243405, 7375001),
    RABBIT(new ItemStackTemplate(Items.CARROT), new ItemStackTemplate(Items.MELON_SEEDS), EntityType.RABBIT, () -> ExCompressumConfig.getActive().baits.rabbitBaitChance,  10051392, 7555121),
    HORSE(new ItemStackTemplate(Items.GOLDEN_APPLE), new ItemStackTemplate(Items.GOLDEN_APPLE), EntityType.HORSE, () -> ExCompressumConfig.getActive().baits.horseBaitChance,  6842447, 15066584),
    DONKEY(new ItemStackTemplate(Items.GOLDEN_CARROT), new ItemStackTemplate(Items.GOLDEN_CARROT), EntityType.DONKEY, () -> ExCompressumConfig.getActive().baits.donkeyBaitChance,  5457209, 8811878),
    POLAR_BEAR(new ItemStackTemplate(Items.SNOWBALL), new ItemStackTemplate(Items.COD), EntityType.POLAR_BEAR, () -> ExCompressumConfig.getActive().baits.polarBearBaitChance,  15921906, 9803152),
    LLAMA(new ItemStackTemplate(Items.WHEAT), new ItemStackTemplate(Items.SUGAR), EntityType.LLAMA, () -> ExCompressumConfig.getActive().baits.llamaBaitChance,  15377456, 4547222),
    PARROT(new ItemStackTemplate(Items.RED_DYE), new ItemStackTemplate(Items.GREEN_DYE), EntityType.PARROT, () -> ExCompressumConfig.getActive().baits.parrotBaitChance,  894731, 16711680),
    CAT(new ItemStackTemplate(Items.LEAD), new ItemStackTemplate(Items.COD), EntityType.CAT, () -> ExCompressumConfig.getActive().baits.catBaitChance,  15714446, 9794134),
    FOX(new ItemStackTemplate(Items.RABBIT), new ItemStackTemplate(Items.SWEET_BERRIES), EntityType.FOX, () -> ExCompressumConfig.getActive().baits.foxBaitChance,  14005919, 13396256),
    TURTLE(new ItemStackTemplate(Items.SEAGRASS), new ItemStackTemplate(Items.SEA_PICKLE), EntityType.TURTLE, () -> ExCompressumConfig.getActive().baits.turtleBaitChance,  15198183, 44975),
    MOOSHROOM(new ItemStackTemplate(Items.RED_MUSHROOM), new ItemStackTemplate(Items.WHEAT), EntityType.MOOSHROOM, () -> ExCompressumConfig.getActive().baits.mooshroomBaitChance,  10489616, 12040119);

    public static final StringRepresentable.EnumCodec<BaitType> CODEC = StringRepresentable.fromEnum(BaitType::values);

    private final ItemStackTemplate displayItemFirst;
    private final ItemStackTemplate displayItemSecond;
    private final EntityType<?> entityType;
    private final Supplier<Double> chanceSupplier;
    private final int primaryColor;
    private final int secondaryColor;
    private @Nullable List<BaitEnvironmentCondition> environmentConditions;

    BaitType(ItemStackTemplate displayItemFirst, ItemStackTemplate displayItemSecond, EntityType<?> entityType, Supplier<Double> chanceSupplier, int primaryColor, int secondaryColor) {
        this.displayItemFirst = displayItemFirst;
        this.displayItemSecond = displayItemSecond;
        this.entityType = entityType;
        this.chanceSupplier = chanceSupplier;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @Nullable
    public Entity createEntity(Level level) {
        return entityType.create(level, EntitySpawnReason.NATURAL);
    }

    public ItemStackTemplate getDisplayItemFirst() {
        return displayItemFirst;
    }

    public ItemStackTemplate getDisplayItemSecond() {
        return displayItemSecond;
    }

    public float getChance() {
        return chanceSupplier.get().floatValue();
    }

    public Collection<BaitEnvironmentCondition> getEnvironmentConditions() {
        if (environmentConditions == null) {
            if (this == OCELOT || this == PARROT) {
                environmentConditions = Lists.newArrayList(
                        new BaitBlockTagCondition(BlockTags.LOGS, Component.translatable("tooltip.excompressum.bait.logs")),
                        new BaitBlockStateCondition(Blocks.VINE.defaultBlockState()),
                        new BaitBlockStateCondition(Blocks.LILY_PAD.defaultBlockState()),
                        new BaitBlockTagCondition(BlockTags.SAPLINGS, Component.translatable("tooltip.excompressum.bait.saplings"))
                );
            } else if (this == SQUID) {
                environmentConditions = Lists.newArrayList(
                        new BaitFluidCondition(Fluids.WATER),
                        new BaitFluidCondition(Fluids.FLOWING_WATER)
                );
            } else if (this == POLAR_BEAR) {
                environmentConditions = Lists.newArrayList(
                        new BaitFluidCondition(Fluids.WATER),
                        new BaitBlockStateCondition(Blocks.SNOW.defaultBlockState()),
                        new BaitBlockStateCondition(Blocks.SNOW_BLOCK.defaultBlockState())
                );
            } else {
                environmentConditions = Lists.newArrayList(
                        new BaitBlockStateCondition(Blocks.GRASS_BLOCK.defaultBlockState()),
                        new BaitBlockStateCondition(Blocks.TALL_GRASS.defaultBlockState()),
                        new BaitBlockStateCondition(Blocks.FERN.defaultBlockState()),
                        new BaitBlockStateCondition(Blocks.LARGE_FERN.defaultBlockState()),
                        new BaitBlockTagCondition(BlockTags.LOGS, Component.translatable("tooltip.excompressum.bait.logs")),
                        new BaitBlockTagCondition(BlockTags.SAPLINGS, Component.translatable("tooltip.excompressum.bait.saplings"))
                );
            }
        }

        return environmentConditions;
    }

    public int getItemColor(int tintIndex) {
        return tintIndex == 0 ? primaryColor : secondaryColor;
    }

    @Override
    public String toString() {
        return getSerializedName();
    }
}
