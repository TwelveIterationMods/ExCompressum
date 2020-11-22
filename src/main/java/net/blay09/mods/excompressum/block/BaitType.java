package net.blay09.mods.excompressum.block;

import com.google.common.collect.Lists;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.tile.BaitBlockStateCondition;
import net.blay09.mods.excompressum.tile.BaitBlockTagCondition;
import net.blay09.mods.excompressum.tile.BaitEnvironmentCondition;
import net.blay09.mods.excompressum.tile.BaitFluidCondition;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public enum BaitType implements IStringSerializable {
    WOLF(new ItemStack(Items.BEEF), new ItemStack(Items.BONE), EntityType.WOLF, ExCompressumConfig.COMMON.wolfBaitChance::get, 14144467, 13545366),
    OCELOT(new ItemStack(Items.GUNPOWDER), new ItemStack(Items.COD), EntityType.OCELOT, ExCompressumConfig.COMMON.ocelotBaitChance::get, 15720061, 5653556),
    COW(new ItemStack(Items.WHEAT), new ItemStack(Items.WHEAT), EntityType.COW, ExCompressumConfig.COMMON.cowBaitChance::get, 4470310, 10592673),
    PIG(new ItemStack(Items.CARROT), new ItemStack(Items.CARROT), EntityType.PIG, ExCompressumConfig.COMMON.pigBaitChance::get, 15771042, 14377823),
    CHICKEN(new ItemStack(Items.WHEAT_SEEDS), new ItemStack(Items.WHEAT_SEEDS), EntityType.CHICKEN, ExCompressumConfig.COMMON.chickenBaitChance::get,  10592673, 16711680),
    SHEEP(new ItemStack(Items.WHEAT_SEEDS), new ItemStack(Items.WHEAT), EntityType.SHEEP, ExCompressumConfig.COMMON.sheepBaitChance::get,  15198183, 16758197),
    SQUID(new ItemStack(Items.COD), new ItemStack(Items.COD), EntityType.SQUID, ExCompressumConfig.COMMON.squidBaitChance::get,  2243405, 7375001),
    RABBIT(new ItemStack(Items.CARROT), new ItemStack(Items.MELON_SEEDS), EntityType.RABBIT, ExCompressumConfig.COMMON.rabbitBaitChance::get,  10051392, 7555121),
    HORSE(new ItemStack(Items.GOLDEN_APPLE), new ItemStack(Items.GOLDEN_APPLE), EntityType.HORSE, ExCompressumConfig.COMMON.horseBaitChance::get,  6842447, 15066584),
    DONKEY(new ItemStack(Items.GOLDEN_CARROT), new ItemStack(Items.GOLDEN_CARROT), EntityType.DONKEY, ExCompressumConfig.COMMON.donkeyBaitChance::get,  5457209, 8811878),
    POLAR_BEAR(new ItemStack(Items.SNOWBALL), new ItemStack(Items.COD), EntityType.POLAR_BEAR, ExCompressumConfig.COMMON.polarBearBaitChance::get,  15921906, 9803152),
    LLAMA(new ItemStack(Items.WHEAT), new ItemStack(Items.SUGAR), EntityType.LLAMA, ExCompressumConfig.COMMON.llamaBaitChance::get,  15377456, 4547222),
    PARROT(new ItemStack(Items.RED_DYE), new ItemStack(Items.GREEN_DYE), EntityType.PARROT, ExCompressumConfig.COMMON.parrotBaitChance::get,  894731, 16711680),
    CAT(new ItemStack(Items.LEAD), new ItemStack(Items.COD), EntityType.CAT, ExCompressumConfig.COMMON.catBaitChance::get,  15714446, 9794134),
    FOX(new ItemStack(Items.RABBIT), new ItemStack(Items.SWEET_BERRIES), EntityType.FOX, ExCompressumConfig.COMMON.foxBaitChance::get,  14005919, 13396256),
    TURTLE(new ItemStack(Items.SEAGRASS), new ItemStack(Items.SEA_PICKLE), EntityType.TURTLE, ExCompressumConfig.COMMON.turtleBaitChance::get,  15198183, 44975),
    MOOSHROOM(new ItemStack(Items.RED_MUSHROOM), new ItemStack(Items.WHEAT), EntityType.MOOSHROOM, ExCompressumConfig.COMMON.mooshroomBaitChance::get,  10489616, 12040119);

    private final ItemStack displayItemFirst;
    private final ItemStack displayItemSecond;
    private final EntityType<?> entityType;
    private final Supplier<Double> chanceSupplier;
    private final int primaryColor;
    private final int secondaryColor;
    private List<BaitEnvironmentCondition> environmentConditions;

    BaitType(ItemStack displayItemFirst, ItemStack displayItemSecond, EntityType<?> entityType, Supplier<Double> chanceSupplier, int primaryColor, int secondaryColor) {
        this.displayItemFirst = displayItemFirst;
        this.displayItemSecond = displayItemSecond;
        this.entityType = entityType;
        this.chanceSupplier = chanceSupplier;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
    }

    @Override
    public String getString() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @Nullable
    public Entity createEntity(World world) {
        return entityType.create(world);
    }

    public ItemStack getDisplayItemFirst() {
        if (this == SHEEP) {
            ItemStack grassSeeds = ExNihilo.getInstance().getNihiloItem(ExNihiloProvider.NihiloItems.SEEDS_GRASS);
            if (!grassSeeds.isEmpty()) {
                return grassSeeds;
            }
        }
        return displayItemFirst;
    }

    public ItemStack getDisplayItemSecond() {
        return displayItemSecond;
    }

    public float getChance() {
        return chanceSupplier.get().floatValue();
    }

    public Collection<BaitEnvironmentCondition> getEnvironmentConditions() {
        if (environmentConditions == null) {
            if (this == OCELOT || this == PARROT) {
                environmentConditions = Lists.newArrayList(
                        new BaitBlockTagCondition(new ResourceLocation("minecraft", "logs")),
                        new BaitBlockStateCondition(Blocks.VINE.getDefaultState()),
                        new BaitBlockStateCondition(Blocks.LILY_PAD.getDefaultState()),
                        new BaitBlockTagCondition(new ResourceLocation("minecraft", "saplings"))
                );
            } else if (this == SQUID) {
                environmentConditions = Lists.newArrayList(
                        new BaitFluidCondition(Fluids.WATER),
                        new BaitFluidCondition(Fluids.FLOWING_WATER)
                );
            } else if (this == POLAR_BEAR) {
                environmentConditions = Lists.newArrayList(
                        new BaitFluidCondition(Fluids.WATER),
                        new BaitBlockStateCondition(Blocks.SNOW.getDefaultState()),
                        new BaitBlockStateCondition(Blocks.SNOW_BLOCK.getDefaultState())
                );
            } else {
                environmentConditions = Lists.newArrayList(
                        new BaitBlockStateCondition(Blocks.GRASS.getDefaultState()),
                        new BaitBlockStateCondition(Blocks.TALL_GRASS.getDefaultState()),
                        new BaitBlockStateCondition(Blocks.FERN.getDefaultState()),
                        new BaitBlockStateCondition(Blocks.LARGE_FERN.getDefaultState()),
                        new BaitBlockTagCondition(new ResourceLocation("minecraft", "saplings")),
                        new BaitBlockTagCondition(new ResourceLocation("minecraft", "logs"))
                );
            }
        }

        return environmentConditions;
    }

    public int getItemColor(int tintIndex) {

        return tintIndex == 0 ? primaryColor : secondaryColor;
    }
}
