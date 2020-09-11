package net.blay09.mods.excompressum.block;

import com.google.common.collect.Lists;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.config.ModConfig;
import net.blay09.mods.excompressum.registry.ExRegistro;
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
    WOLF(new ItemStack(Items.BEEF), new ItemStack(Items.BONE), EntityType.WOLF, () -> ModConfig.baits.wolfChance),
    OCELOT(new ItemStack(Items.GUNPOWDER), new ItemStack(Items.COD), EntityType.OCELOT, () -> ModConfig.baits.ocelotChance),
    COW(new ItemStack(Items.WHEAT), new ItemStack(Items.WHEAT), EntityType.COW, () -> ModConfig.baits.cowChance),
    PIG(new ItemStack(Items.CARROT), new ItemStack(Items.CARROT), EntityType.PIG, () -> ModConfig.baits.pigChance),
    CHICKEN(new ItemStack(Items.WHEAT_SEEDS), new ItemStack(Items.WHEAT_SEEDS), EntityType.CHICKEN, () -> ModConfig.baits.chickenChance),
    SHEEP(new ItemStack(Items.WHEAT_SEEDS), new ItemStack(Items.WHEAT), EntityType.SHEEP, () -> ModConfig.baits.sheepChance),
    SQUID(new ItemStack(Items.COD), new ItemStack(Items.COD), EntityType.SQUID, () -> ModConfig.baits.squidChance),
    RABBIT(new ItemStack(Items.CARROT), new ItemStack(Items.MELON_SEEDS), EntityType.RABBIT, () -> ModConfig.baits.rabbitChance),
    HORSE(new ItemStack(Items.GOLDEN_APPLE), new ItemStack(Items.GOLDEN_APPLE), EntityType.HORSE, () -> ModConfig.baits.horseChance),
    DONKEY(new ItemStack(Items.GOLDEN_CARROT), new ItemStack(Items.GOLDEN_CARROT), EntityType.DONKEY, () -> ModConfig.baits.donkeyChance),
    POLAR_BEAR(new ItemStack(Items.SNOWBALL), new ItemStack(Items.COD), EntityType.POLAR_BEAR, () -> ModConfig.baits.polarBearChance),
    LLAMA(new ItemStack(Items.WHEAT), new ItemStack(Items.SUGAR), EntityType.LLAMA, () -> ModConfig.baits.llamaChance),
    PARROT(new ItemStack(Items.RED_DYE), new ItemStack(Items.GREEN_DYE), EntityType.PARROT, () -> ModConfig.baits.parrotChance);

    private final ItemStack displayItemFirst;
    private final ItemStack displayItemSecond;
    private final EntityType<?> entityType;
    private final Supplier<Float> chanceSupplier;
    private List<BaitEnvironmentCondition> environmentConditions;

    BaitType(ItemStack displayItemFirst, ItemStack displayItemSecond, EntityType<?> entityType, Supplier<Float> chanceSupplier) {
        this.displayItemFirst = displayItemFirst;
        this.displayItemSecond = displayItemSecond;
        this.entityType = entityType;
        this.chanceSupplier = chanceSupplier;
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
            ItemStack grassSeeds = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.SEEDS_GRASS);
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
        return chanceSupplier.get();
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
}
