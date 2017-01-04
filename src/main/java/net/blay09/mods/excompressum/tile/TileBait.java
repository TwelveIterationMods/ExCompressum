package net.blay09.mods.excompressum.tile;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.blay09.mods.excompressum.config.BaitConfig;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.block.BlockBait;
import net.blay09.mods.excompressum.registry.ExNihiloProvider;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public class TileBait extends TileEntity implements ITickable {

	private static final int ENVIRONMENTAL_CHECK_INTERVAL = 20 * 10;
	private static final int MAX_BAITS_IN_AREA = 2;
	private static final int MIN_ENV_IN_AREA = 10;
	private static final int MAX_ANIMALS_IN_AREA = 2;
	private static final int SPAWN_CHECK_INTERVAL = 20;
	private static final int MIN_DISTANCE_NO_PLAYERS = 6;

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

	public static class BaitBlockCondition {
		private final IBlockState state;
		private final boolean isWildcard;

		public BaitBlockCondition(IBlockState state, boolean isWildcard) {
			this.state = state;
			this.isWildcard = isWildcard;
		}

		public IBlockState getState() {
			return state;
		}

		public boolean isWildcard() {
			return isWildcard;
		}
	}

	private static final Multimap<BlockBait.Type, BaitBlockCondition> envBlockMap = ArrayListMultimap.create();

	private ItemStack renderItemMain;
	private ItemStack renderItemSub;
	private EnvironmentalCondition environmentStatus;
	private int ticksSinceEnvironmentalCheck;
	private int ticksSinceSpawnCheck;

	@Override
	public void update() {
		if (renderItemMain == null) {
			renderItemMain = getBaitDisplayItem(getBlockMetadata(), 0);
		}
		if (renderItemSub == null) {
			renderItemSub = getBaitDisplayItem(getBlockMetadata(), 1);
		}

		ticksSinceEnvironmentalCheck++;

		ticksSinceSpawnCheck++;
		if (ticksSinceSpawnCheck >= SPAWN_CHECK_INTERVAL) {
			int metadata = getBlockMetadata();
			if (!worldObj.isRemote && worldObj.rand.nextFloat() <= getBaitChance(metadata)) {
				if (checkSpawnConditions(true) == EnvironmentalCondition.CanSpawn) {
					final float range = MIN_DISTANCE_NO_PLAYERS;
					if (worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + range, pos.getZ() + range)).isEmpty()) {
						EntityLiving entityLiving = getBaitEntityLiving(worldObj, metadata);
						if (entityLiving != null) {
							if (entityLiving instanceof EntityAgeable && worldObj.rand.nextFloat() <= BaitConfig.baitChildChance) {
								((EntityAgeable) entityLiving).setGrowingAge(-24000);
							}
							entityLiving.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
							worldObj.spawnEntityInWorld(entityLiving);
							((WorldServer) worldObj).spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, false, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1, 0, 0, 0, 0.0);
							worldObj.playSound(null, pos, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.BLOCKS, 1f, 1f);
						}
						worldObj.setBlockToAir(pos);
					}
				}
			}
			ticksSinceSpawnCheck = 0;
		}
	}

	@Nullable
	private static ItemStack getBaitDisplayItem(int metadata, int i) {
		BlockBait.Type type = BlockBait.Type.fromId(metadata);
		if(type != null) {
			switch (type) {
				case WOLF:
					return i == 0 ? new ItemStack(Items.BEEF) : new ItemStack(Items.BONE);
				case OCELOT:
					return i == 0 ? new ItemStack(Items.GUNPOWDER) : new ItemStack(Items.FISH);
				case COW:
					return new ItemStack(Items.WHEAT);
				case PIG:
					return new ItemStack(Items.CARROT);
				case CHICKEN:
					return new ItemStack(Items.WHEAT_SEEDS);
				case SHEEP:
					ItemStack grassSeeds = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.SEEDS_GRASS);
					if (grassSeeds != null) {
						return i == 0 ? grassSeeds : new ItemStack(Items.WHEAT);
					} else {
						return new ItemStack(Items.WHEAT);
					}
				case SQUID:
					return new ItemStack(Items.FISH);
				case RABBIT:
					return i == 0 ? new ItemStack(Items.CARROT) : new ItemStack(Items.MELON_SEEDS);
			}
		}
		return null;
	}

	@Nullable
	private static EntityLiving getBaitEntityLiving(World world, int metadata) {
		BlockBait.Type type = BlockBait.Type.fromId(metadata);
		if(type != null) {
			switch (type) {
				case WOLF:
					return new EntityWolf(world);
				case OCELOT:
					return new EntityOcelot(world);
				case COW:
					return new EntityCow(world);
				case PIG:
					return new EntityPig(world);
				case CHICKEN:
					return new EntityChicken(world);
				case SHEEP:
					return new EntitySheep(world);
				case SQUID:
					return new EntitySquid(world);
				case RABBIT:
					return new EntityRabbit(world);
			}
		}
		return null;
	}

	private float getBaitChance(int metadata) {
		BlockBait.Type type = BlockBait.Type.fromId(metadata);
		if(type != null) {
			switch (type) {
				case WOLF:
					return BaitConfig.baitWolfChance;
				case OCELOT:
					return BaitConfig.baitOcelotChance;
				case COW:
					return BaitConfig.baitCowChance;
				case PIG:
					return BaitConfig.baitPigChance;
				case CHICKEN:
					return BaitConfig.baitChickenChance;
				case SHEEP:
					return BaitConfig.baitSheepChance;
				case SQUID:
					return BaitConfig.baitSquidChance;
				case RABBIT:
					return BaitConfig.baitRabbitChance;
			}
		}
		return 0;
	}

	@Nullable
	public ItemStack getRenderItem(int i) {
		return i == 0 ? renderItemMain : renderItemSub;
	}

	// NOTE if this turns out to perform badly, distribute the check over several ticks
	public EnvironmentalCondition checkSpawnConditions(boolean checkNow) {
		if (checkNow || ticksSinceEnvironmentalCheck > ENVIRONMENTAL_CHECK_INTERVAL) {
			int metadata = getBlockMetadata();
			Collection<BaitBlockCondition> envBlocks = envBlockMap.get(BlockBait.Type.fromId(metadata));
			populateEnvBlocks();
			final int range = 5;
			final int rangeVertical = 3;
			int countBait = 0;
			int countEnvBlocks = 0;
			boolean foundWater = false;
			for (int x = pos.getX() - range; x < pos.getX() + range; x++) {
				for (int y = pos.getY() - rangeVertical; y < pos.getY() + rangeVertical; y++) {
					for (int z = pos.getZ() - range; z < pos.getZ() + range; z++) {
						BlockPos testPos = new BlockPos(x, y, z);
						IBlockState state = worldObj.getBlockState(testPos);
						if (state.getBlock() == ModBlocks.bait) {
							countBait++;
						} else if (state.getBlock() == Blocks.WATER || state.getBlock() == Blocks.FLOWING_WATER) {
							foundWater = true;
						}

						for (BaitBlockCondition envBlock : envBlocks) {
							if (state.getBlock() == envBlock.getState().getBlock()) {
								if (!envBlock.isWildcard()) {
									int meta = state.getBlock().getMetaFromState(state);
									int envMeta = envBlock.getState().getBlock().getMetaFromState(envBlock.getState());
									if (meta != envMeta) {
										continue;
									}
								}
								countEnvBlocks++;
							}
						}
					}
				}
			}
			if (!foundWater) {
				environmentStatus = EnvironmentalCondition.NoWater;
			} else if (countBait > MAX_BAITS_IN_AREA) {
				environmentStatus = EnvironmentalCondition.NearbyBait;
			} else if (countEnvBlocks < MIN_ENV_IN_AREA) {
				environmentStatus = EnvironmentalCondition.WrongEnv;
			} else if (worldObj.getEntitiesWithinAABB(EntityAnimal.class, new AxisAlignedBB(pos.getX() - range * 2, pos.getY() - rangeVertical, pos.getZ() - range * 2, pos.getX() + range * 2, pos.getY() + rangeVertical, pos.getZ() + range * 2)).size() > MAX_ANIMALS_IN_AREA) {
				environmentStatus = EnvironmentalCondition.NearbyAnimal;
			} else {
				environmentStatus = EnvironmentalCondition.CanSpawn;
			}
			ticksSinceEnvironmentalCheck = 0;
		}

		return environmentStatus;
	}

	private static void populateEnvBlocks() {
		if (envBlockMap.size() > 0) {
			return;
		}

		BlockBait.Type[] simpleTypes = new BlockBait.Type[]{BlockBait.Type.WOLF, BlockBait.Type.COW, BlockBait.Type.PIG, BlockBait.Type.CHICKEN, BlockBait.Type.SHEEP};
		BlockPlanks.EnumType[] acceptedTrees = new BlockPlanks.EnumType[]{BlockPlanks.EnumType.OAK, BlockPlanks.EnumType.BIRCH, BlockPlanks.EnumType.SPRUCE, BlockPlanks.EnumType.ACACIA, BlockPlanks.EnumType.DARK_OAK};
		for (BlockBait.Type type : simpleTypes) {
			envBlockMap.put(type, new BaitBlockCondition(Blocks.GRASS.getDefaultState(), false));

			for (BlockPlanks.EnumType treeType : acceptedTrees) {
				envBlockMap.put(type, new BaitBlockCondition(Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, treeType), false));
				if (BlockOldLog.VARIANT.getAllowedValues().contains(treeType)) {
					envBlockMap.put(type, new BaitBlockCondition(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, treeType), false));
				}
				if (BlockNewLog.VARIANT.getAllowedValues().contains(treeType)) {
					envBlockMap.put(type, new BaitBlockCondition(Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, treeType), false));
				}
			}

			envBlockMap.put(type, new BaitBlockCondition(Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS), false));
			envBlockMap.put(type, new BaitBlockCondition(Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.FERN), false));
		}

		envBlockMap.put(BlockBait.Type.OCELOT, new BaitBlockCondition(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE), false));
		envBlockMap.put(BlockBait.Type.OCELOT, new BaitBlockCondition(Blocks.VINE.getDefaultState(), true));
		envBlockMap.put(BlockBait.Type.OCELOT, new BaitBlockCondition(Blocks.WATERLILY.getDefaultState(), false));
		envBlockMap.put(BlockBait.Type.OCELOT, new BaitBlockCondition(Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.JUNGLE), false));

		envBlockMap.put(BlockBait.Type.SQUID, new BaitBlockCondition(Blocks.WATER.getDefaultState(), true));
		envBlockMap.put(BlockBait.Type.SQUID, new BaitBlockCondition(Blocks.FLOWING_WATER.getDefaultState(), true));
	}

}
