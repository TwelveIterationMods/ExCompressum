package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.config.ModConfig;
import net.blay09.mods.excompressum.tile.BaitTileEntity;
import net.blay09.mods.excompressum.tile.EnvironmentalCondition;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BaitBlock extends ContainerBlock {

	public static final String name = "bait";
	public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0, 0, 0, 1, 0.1, 1);
	public static final EnumProperty<BaitType> VARIANT = EnumProperty.create("variant", BaitType.class);

	public BaitBlock() {
		super(Material.GROUND);
		setHardness(0.1f);
		setCreativeTab(ExCompressum.itemGroup);
	}

	@Override
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
		return BOUNDING_BOX;
	}

	@Nullable
	@Override
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getCollisionBoundingBox(BlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new BaitTileEntity();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, BlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		BaitTileEntity tileEntity = (BaitTileEntity) world.getTileEntity(pos);
		if (tileEntity != null) {
			EnvironmentalCondition environmentStatus = tileEntity.checkSpawnConditions(true);
			if (!world.isRemote) {
				ITextComponent chatComponent = new TextComponentTranslation(environmentStatus.langKey);
				chatComponent.getStyle().setColor(environmentStatus != EnvironmentalCondition.CanSpawn ? TextFormatting.RED : TextFormatting.GREEN);
				player.sendMessage(chatComponent);
			}
		}
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, EntityLivingBase placer, ItemStack stack) {
		if (placer instanceof EntityPlayer) {
			BaitTileEntity tileEntity = (BaitTileEntity) world.getTileEntity(pos);
			if (tileEntity != null) {
				EnvironmentalCondition environmentStatus = tileEntity.checkSpawnConditions(true);
				if (!world.isRemote) {
					ITextComponent chatComponent = new TextComponentTranslation(environmentStatus.langKey);
					chatComponent.getStyle().setColor(environmentStatus != EnvironmentalCondition.CanSpawn ? TextFormatting.RED : TextFormatting.GREEN);
					placer.sendMessage(chatComponent);
				}
			}
		}
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rand) {
		if (!ModConfig.client.disableParticles) {
			BaitTileEntity tileEntity = (BaitTileEntity) world.getTileEntity(pos);
			if (tileEntity != null && tileEntity.checkSpawnConditions(false) == EnvironmentalCondition.CanSpawn) {
				if (rand.nextFloat() <= 0.2f) {
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + rand.nextFloat(), pos.getY() + rand.nextFloat() * 0.5f, pos.getZ() + rand.nextFloat(), 0.0, 0.0, 0.0);
				}
			}
		}
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		BaitType type = BaitType.fromId(stack.getItemDamage());
		if (type == BaitType.SQUID) {
			tooltip.add(I18n.format("info.excompressum:baitPlaceInWater"));
		}
	}

	public BaitType getBaitType() {
		return baitType;
	}
}
