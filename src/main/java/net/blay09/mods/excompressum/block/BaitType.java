package net.blay09.mods.excompressum.block;

import com.google.common.collect.Lists;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.config.ModConfig;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.tile.BaitBlockCondition;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.passive.horse.DonkeyEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public enum BaitType implements IStringSerializable {
    WOLF(new ItemStack(Items.BEEF), new ItemStack(Items.BONE), Entities::new, () -> ModConfig.baits.wolfChance),
    OCELOT(new ItemStack(Items.GUNPOWDER), new ItemStack(Items.COD), OcelotEntity::new, () -> ModConfig.baits.ocelotChance),
    COW(new ItemStack(Items.WHEAT), new ItemStack(Items.WHEAT), CowEntity::new, () -> ModConfig.baits.cowChance),
    PIG(new ItemStack(Items.CARROT), new ItemStack(Items.CARROT), PigEntity::new, () -> ModConfig.baits.pigChance),
    CHICKEN(new ItemStack(Items.WHEAT_SEEDS), new ItemStack(Items.WHEAT_SEEDS), ChickenEntity::new, () -> ModConfig.baits.chickenChance),
    SHEEP(new ItemStack(Items.WHEAT_SEEDS), new ItemStack(Items.WHEAT), SheepEntity::new, () -> ModConfig.baits.sheepChance),
    SQUID(new ItemStack(Items.COD), new ItemStack(Items.COD), SquidEntity::new, () -> ModConfig.baits.squidChance),
    RABBIT(new ItemStack(Items.CARROT), new ItemStack(Items.MELON_SEEDS), RabbitEntity::new, () -> ModConfig.baits.rabbitChance),
    HORSE(new ItemStack(Items.GOLDEN_APPLE), new ItemStack(Items.GOLDEN_APPLE), HorseEntity::new, () -> ModConfig.baits.horseChance),
    DONKEY(new ItemStack(Items.GOLDEN_CARROT), new ItemStack(Items.GOLDEN_CARROT), DonkeyEntity::new, () -> ModConfig.baits.donkeyChance),
    POLAR_BEAR(new ItemStack(Items.SNOWBALL), new ItemStack(Items.COD), PolarBearEntity::new, () -> ModConfig.baits.polarBearChance),
    LLAMA(new ItemStack(Items.WHEAT), new ItemStack(Items.SUGAR), LlamaEntity::new, () -> ModConfig.baits.llamaChance),
    PARROT(new ItemStack(Items.RED_DYE), new ItemStack(Items.GREEN_DYE), ParrotEntity::new, () -> ModConfig.baits.parrotChance);

    public static BaitType[] values = values();

    private ItemStack displayItemFirst;
    private ItemStack displayItemSecond;
    private EntityType<?> entityType;
    private Supplier<Float> chanceSupplier;
    private List<BaitBlockCondition> environmentConditions;

    BaitType(ItemStack displayItemFirst, ItemStack displayItemSecond, EntityType<?> entityType, Supplier<Float> chanceSupplier) {
        this.displayItemFirst = displayItemFirst;
        this.displayItemSecond = displayItemSecond;
        this.entityType = entityType;
        this.chanceSupplier = chanceSupplier;
    }

    @Nullable
    public static BaitType fromId(int id) {
        return id >= 0 && id < values.length ? values[id] : null;
    }

    @Override
    public String getString() {
        return name().toLowerCase(Locale.ENGLISH);
    }

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

    public Collection<BaitBlockCondition> getEnvironmentConditions() {
        if (environmentConditions == null) {
            if (this == OCELOT || this == PARROT) {
                environmentConditions = Lists.newArrayList(
                        new BaitBlockCondition(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE), false),
                        new BaitBlockCondition(Blocks.VINE.getDefaultState(), true),
                        new BaitBlockCondition(Blocks.WATERLILY.getDefaultState(), false),
                        new BaitBlockCondition(Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.JUNGLE), false)
                );
            } else if (this == SQUID) {
                environmentConditions = Lists.newArrayList(
                        new BaitBlockCondition(Blocks.WATER.getDefaultState(), true),
                        new BaitBlockCondition(Blocks.FLOWING_WATER.getDefaultState(), true)
                );
            } else if (this == POLAR_BEAR) {
                environmentConditions = Lists.newArrayList(
                        new BaitBlockCondition(Blocks.WATER.getDefaultState(), true),
                        new BaitBlockCondition(Blocks.FLOWING_WATER.getDefaultState(), true),
                        new BaitBlockCondition(Blocks.SNOW.getDefaultState(), true),
                        new BaitBlockCondition(Blocks.WATER.getDefaultState(), true),
                        new BaitBlockCondition(Blocks.SNOW_LAYER.getDefaultState(), true)
                );
            } else {
                BlockPlanks.EnumType[] acceptedTrees = new BlockPlanks.EnumType[]{BlockPlanks.EnumType.OAK, BlockPlanks.EnumType.BIRCH, BlockPlanks.EnumType.SPRUCE, BlockPlanks.EnumType.ACACIA, BlockPlanks.EnumType.DARK_OAK};
                environmentConditions = Lists.newArrayList(
                        new BaitBlockCondition(Blocks.GRASS.getDefaultState(), false),
                        new BaitBlockCondition(Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS), false),
                        new BaitBlockCondition(Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.FERN), false)
                );
                for (BlockPlanks.EnumType treeType : acceptedTrees) {
                    environmentConditions.add(new BaitBlockCondition(Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, treeType), false));
                    if (BlockOldLog.VARIANT.getAllowedValues().contains(treeType)) {
                        environmentConditions.add(new BaitBlockCondition(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, treeType), false));
                    }
                    if (BlockNewLog.VARIANT.getAllowedValues().contains(treeType)) {
                        environmentConditions.add(new BaitBlockCondition(Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, treeType), false));
                    }
                }
            }
        }
        return environmentConditions;
    }
}
