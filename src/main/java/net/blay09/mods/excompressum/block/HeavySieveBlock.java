package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.SieveModelBounds;
import net.blay09.mods.excompressum.config.ModConfig;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.tile.HeavySieveTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;

import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.ItemHandlerHelper;

public class HeavySieveBlock extends ContainerBlock {

	public static final String name = "heavy_sieve";
	public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

	public static final SieveModelBounds SIEVE_BOUNDS = new SieveModelBounds(0.5625f, 0.0625f, 0.88f, 0.5f);

	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0, 0, 0, 1, 0.75f, 1);
	public static final BooleanProperty WITH_MESH = BooleanProperty.create("with_mesh");

	public HeavySieveBlock(HeavySieveType type) {
		super(Material.WOOD);
		setCreativeTab(ExCompressum.itemGroup);
		setHardness(2f);
	}

	@Override
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
		return BOUNDING_BOX;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(WITH_MESH);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new HeavySieveTileEntity();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, BlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		HeavySieveTileEntity tileEntity = (HeavySieveTileEntity) world.getTileEntity(pos);
		if (tileEntity != null) {
			ItemStack heldItem = player.getHeldItem(hand);
			if (!heldItem.isEmpty()) {
				SieveMeshRegistryEntry sieveMesh = SieveMeshRegistry.getEntry(heldItem);
				if (sieveMesh != null && tileEntity.getMeshStack().isEmpty()) {
					tileEntity.setMeshStack(player.capabilities.isCreativeMode ? ItemHandlerHelper.copyStackWithSize(heldItem, 1) : heldItem.splitStack(1));
					return true;
				}

				if (tileEntity.addSiftable(player, heldItem)) {
					world.playSound(null, pos, SoundEvents.BLOCK_GRAVEL_STEP, SoundCategory.BLOCKS, 0.5f, 1f);
					return true;
				}
			} else {
				if(!world.isRemote && player.isSneaking()) {
					ItemStack meshStack = tileEntity.getMeshStack();
					if(!meshStack.isEmpty()) {
						if (player.inventory.addItemStackToInventory(meshStack)) {
							world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, meshStack));
						}
						tileEntity.setMeshStack(ItemStack.EMPTY);
					}
				}
			}

			if (ModConfig.automation.allowHeavySieveAutomation || !(player instanceof FakePlayer)) {
				if (tileEntity.processContents(player)) {
					world.playSound(null, pos, SoundEvents.BLOCK_SAND_STEP, SoundCategory.BLOCKS, 0.3f, 0.6f);
				}
			}
		}
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, BlockState state) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity instanceof HeavySieveTileEntity) {
			HeavySieveTileEntity tileHeavySieve = (HeavySieveTileEntity) tileEntity;
			if(!tileHeavySieve.getMeshStack().isEmpty()) {
				world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, tileHeavySieve.getMeshStack()));
			}
		}
		super.breakBlock(world, pos, state);
	}

}
