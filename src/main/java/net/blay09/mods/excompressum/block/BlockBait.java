package net.blay09.mods.excompressum.block;

import com.google.common.collect.Lists;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.config.BaitConfig;
import net.blay09.mods.excompressum.config.ModConfig;
import net.blay09.mods.excompressum.registry.ExNihiloProvider;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.tile.TileBait;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockBait extends BlockContainer {

	public static final String name = "bait";
	public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

	public enum Type implements IStringSerializable {
		WOLF(new ItemStack(Items.BEEF), new ItemStack(Items.BONE), EntityWolf::new, () -> BaitConfig.baitWolfChance),
		OCELOT(new ItemStack(Items.GUNPOWDER), new ItemStack(Items.FISH), EntityOcelot::new, () -> BaitConfig.baitOcelotChance),
		COW(new ItemStack(Items.WHEAT), new ItemStack(Items.WHEAT), EntityCow::new, () -> BaitConfig.baitCowChance),
		PIG(new ItemStack(Items.CARROT), new ItemStack(Items.CARROT), EntityPig::new, () -> BaitConfig.baitPigChance),
		CHICKEN(new ItemStack(Items.WHEAT_SEEDS), new ItemStack(Items.WHEAT_SEEDS), EntityChicken::new, () -> BaitConfig.baitChickenChance),
		SHEEP(new ItemStack(Items.WHEAT_SEEDS), new ItemStack(Items.WHEAT), EntitySheep::new, () -> BaitConfig.baitSheepChance),
		SQUID(new ItemStack(Items.FISH), new ItemStack(Items.FISH), EntitySquid::new, () -> BaitConfig.baitSquidChance),
		RABBIT(new ItemStack(Items.CARROT), new ItemStack(Items.MELON_SEEDS), EntityRabbit::new, () -> BaitConfig.baitRabbitChance),
		HORSE(new ItemStack(Items.GOLDEN_APPLE), new ItemStack(Items.GOLDEN_APPLE), EntityHorse::new, () -> BaitConfig.baitHorseChance),
		DONKEY(new ItemStack(Items.GOLDEN_CARROT), new ItemStack(Items.GOLDEN_CARROT), EntityDonkey::new, () -> BaitConfig.baitDonkeyChance);
		// TODO add missing animals

		public static Type[] values = values();

		private ItemStack displayItemFirst;
		private ItemStack displayItemSecond;
		private Function<World, EntityLiving> entityFactory;
		private Supplier<Float> chanceSupplier;
		private List<TileBait.BaitBlockCondition> environmentConditions;

		Type(ItemStack displayItemFirst, ItemStack displayItemSecond, Function<World, EntityLiving> entityFactory, Supplier<Float> chanceSupplier) {
			this.displayItemFirst = displayItemFirst;
			this.displayItemSecond = displayItemSecond;
			this.entityFactory = entityFactory;
			this.chanceSupplier = chanceSupplier;
		}

		@Nullable
		public static Type fromId(int id) {
			return id >= 0 && id < values.length ? values[id] : null;
		}

		@Override
		public String getName() {
			return name().toLowerCase(Locale.ENGLISH);
		}

		public EntityLiving createEntity(World world) {
			return entityFactory.apply(world);
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

		public Collection<TileBait.BaitBlockCondition> getEnvironmentConditions() {
			if (environmentConditions == null) {
				if (this == OCELOT) {
					environmentConditions = Lists.newArrayList(
							new TileBait.BaitBlockCondition(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE), false),
							new TileBait.BaitBlockCondition(Blocks.VINE.getDefaultState(), true),
							new TileBait.BaitBlockCondition(Blocks.WATERLILY.getDefaultState(), false),
							new TileBait.BaitBlockCondition(Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.JUNGLE), false)
					);
				} else if (this == SQUID) {
					environmentConditions = Lists.newArrayList(
							new TileBait.BaitBlockCondition(Blocks.WATER.getDefaultState(), true),
							new TileBait.BaitBlockCondition(Blocks.FLOWING_WATER.getDefaultState(), true)
					);
				} else {
					BlockPlanks.EnumType[] acceptedTrees = new BlockPlanks.EnumType[]{BlockPlanks.EnumType.OAK, BlockPlanks.EnumType.BIRCH, BlockPlanks.EnumType.SPRUCE, BlockPlanks.EnumType.ACACIA, BlockPlanks.EnumType.DARK_OAK};
					environmentConditions = Lists.newArrayList(
							new TileBait.BaitBlockCondition(Blocks.GRASS.getDefaultState(), false),
							new TileBait.BaitBlockCondition(Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS), false),
							new TileBait.BaitBlockCondition(Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.FERN), false)
					);
					for (BlockPlanks.EnumType treeType : acceptedTrees) {
						environmentConditions.add(new TileBait.BaitBlockCondition(Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, treeType), false));
						if (BlockOldLog.VARIANT.getAllowedValues().contains(treeType)) {
							environmentConditions.add(new TileBait.BaitBlockCondition(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, treeType), false));
						}
						if (BlockNewLog.VARIANT.getAllowedValues().contains(treeType)) {
							environmentConditions.add(new TileBait.BaitBlockCondition(Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, treeType), false));
						}
					}
				}
			}
			return environmentConditions;
		}
	}

	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0, 0, 0, 1, 0.1, 1);
	public static final PropertyEnum<Type> VARIANT = PropertyEnum.create("variant", Type.class);

	public BlockBait() {
		super(Material.GROUND);
		setHardness(0.1f);
		setCreativeTab(ExCompressum.creativeTab);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT);
	}

	@Override
	@SuppressWarnings("deprecation")
	public IBlockState getStateFromMeta(int meta) {
		if (meta < 0 || meta >= Type.values.length) {
			return getDefaultState();
		}
		return getDefaultState().withProperty(VARIANT, Type.values[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(VARIANT).ordinal();
	}

	@Override
	protected ItemStack getSilkTouchDrop(IBlockState state) {
		return new ItemStack(this, 1, state.getValue(VARIANT).ordinal());
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(VARIANT).ordinal();
	}

	@Override
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BOUNDING_BOX;
	}

	@Nullable
	@Override
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return null;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
		for (int i = 0; i < Type.values.length; i++) {
			items.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileBait();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileBait tileEntity = (TileBait) world.getTileEntity(pos);
		if (tileEntity != null) {
			TileBait.EnvironmentalCondition environmentStatus = tileEntity.checkSpawnConditions(true);
			if (!world.isRemote) {
				ITextComponent chatComponent = new TextComponentTranslation(environmentStatus.langKey);
				chatComponent.getStyle().setColor(environmentStatus != TileBait.EnvironmentalCondition.CanSpawn ? TextFormatting.RED : TextFormatting.GREEN);
				player.sendMessage(chatComponent);
			}
		}
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if (placer instanceof EntityPlayer) {
			TileBait tileEntity = (TileBait) world.getTileEntity(pos);
			if (tileEntity != null) {
				TileBait.EnvironmentalCondition environmentStatus = tileEntity.checkSpawnConditions(true);
				if (!world.isRemote) {
					ITextComponent chatComponent = new TextComponentTranslation(environmentStatus.langKey);
					chatComponent.getStyle().setColor(environmentStatus != TileBait.EnvironmentalCondition.CanSpawn ? TextFormatting.RED : TextFormatting.GREEN);
					placer.sendMessage(chatComponent);
				}
			}
		}
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (!ModConfig.client.disableParticles) {
			TileBait tileEntity = (TileBait) world.getTileEntity(pos);
			if (tileEntity != null && tileEntity.checkSpawnConditions(false) == TileBait.EnvironmentalCondition.CanSpawn) {
				if (rand.nextFloat() <= 0.2f) {
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + rand.nextFloat(), pos.getY() + rand.nextFloat() * 0.5f, pos.getZ() + rand.nextFloat(), 0.0, 0.0, 0.0);
				}
			}
		}
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		BlockBait.Type type = BlockBait.Type.fromId(stack.getItemDamage());
		if (type == BlockBait.Type.SQUID) {
			tooltip.add(I18n.format("info.excompressum:baitPlaceInWater"));
		}
	}

}
