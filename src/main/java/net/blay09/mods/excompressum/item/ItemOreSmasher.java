package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipeRegistry;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Collection;
import java.util.HashSet;

public class ItemOreSmasher extends ItemCompressumTool {

	// CLEANUP this probably shouldn't be hardcoded here, and go into the OmniaAddon instead
	private static final String[] ORE_BLOCKS = new String[] {
			"exnihiloomnia:ore_gravel",
			"exnihiloomnia:ore_gravel_ender",
			"exnihiloomnia:ore_gravel_nether",
			"exnihiloomnia:ore_sand"
	};

	private static final String[] ORE_BLOCKS_OREDICT = new String[] {
			"oreGravel",
			"oreNetherGravel",
			"oreSand"
	};

	// CLEANUP this probably shouldn't be hardcoded here, and go into the OmniaAddon instead
	private static final String[] ORE_ITEMS = new String[] {
			"exnihiloomnia:ore_broken",
			"exnihiloomnia:ore_broken_nether",
			"exnihiloomnia:ore_broken_ender",
			"exnihiloomnia:ore_crushed"
	};

	private static final String[] ORE_ITEMS_OREDICT = new String[] {
			"oreBroken",
			"oreNetherBroken",
			"oreCrushed"
	};

    public ItemOreSmasher() {
        super(0f, 0f, ToolMaterial.DIAMOND, new HashSet<Block>());
		setRegistryName("ore_smasher");
        setUnlocalizedName(getRegistryNameString());
        setCreativeTab(ExCompressum.creativeTab);
    }

	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
		return isOreBlock(new ItemStack(state.getBlock()));
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state) {
		if (isOreBlock(new ItemStack(state.getBlock()))) {
			return efficiencyOnProperMaterial; // Note: Omnia's ore blocks break instantly at the moment. Not sure if intended, but it's not an issue on our side.
		}
		return 0.8f;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!world.checkNoEntityCollision(new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1))) {
			return EnumActionResult.FAIL;
		}
        for(int i = 0; i < player.inventory.mainInventory.size(); i++) {
            ItemStack inventoryStack = player.inventory.mainInventory.get(i);
            if(!inventoryStack.isEmpty()) {
                if(isOreItem(inventoryStack)) {
					CompressedRecipe recipe = CompressedRecipeRegistry.getRecipe(inventoryStack);
					if(recipe != null && recipe.getResultStack().getCount() == 1) {
						if(inventoryStack.getCount() >= recipe.getSourceStack().getCount()) {
							IBlockState oldState = world.getBlockState(pos);
							ItemStack resultStack = recipe.getResultStack().copy();
							resultStack.getItem().onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
							world.notifyBlockUpdate(pos, oldState, world.getBlockState(pos), 3);
							if(resultStack.isEmpty()) {
								inventoryStack.shrink(recipe.getSourceStack().getCount());
								if (inventoryStack.isEmpty()) {
									player.inventory.mainInventory.remove(i);
								}
								player.swingArm(hand);
								return EnumActionResult.SUCCESS;
							}
						}
					}
                }
				if (isOreBlock(inventoryStack)) {
					IBlockState oldState = world.getBlockState(pos);
					inventoryStack.getItem().onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
					world.notifyBlockUpdate(pos, oldState, world.getBlockState(pos), 3);
					if (inventoryStack.isEmpty()) {
						player.inventory.mainInventory.remove(i);
					}
					player.swingArm(hand);
					return EnumActionResult.SUCCESS;
				}
            }
        }
        return EnumActionResult.FAIL;
    }

	@Override
	public boolean onBlockDestroyed(ItemStack itemStack, World world, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
		if(!world.isRemote && canHarvestBlock(state, itemStack) && ExRegistro.isHammerable(state)) {
			world.setBlockToAir(pos);
			Collection<ItemStack> rewards = ExRegistro.rollHammerRewards(state, toolMaterial.getHarvestLevel(), EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemStack), world.rand);
			for(ItemStack rewardStack : rewards) {
				world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, rewardStack));
			}
		}
		return super.onBlockDestroyed(itemStack, world, state, pos, entityLiving);
	}

	private boolean isOreItem(ItemStack itemStack) {
		ResourceLocation registryName = itemStack.getItem().getRegistryName();
		if(registryName == null) {
			return false;
		}
		if (ArrayUtils.contains(ORE_ITEMS, registryName.toString())) {
			return true;
		}
		int[] oreIDs = OreDictionary.getOreIDs(itemStack);
		for (int oreID : oreIDs) {
			String oreName = OreDictionary.getOreName(oreID);
			for(String dictName : ORE_ITEMS_OREDICT) {
				if(oreName.startsWith(dictName)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isOreBlock(ItemStack itemStack) {
		ResourceLocation registryName = itemStack.getItem().getRegistryName();
		if(registryName == null) {
			return false;
		}
		if (ArrayUtils.contains(ORE_BLOCKS, registryName.toString())) {
			return true;
		}
		int[] oreIDs = OreDictionary.getOreIDs(itemStack);
		for (int oreID : oreIDs) {
			String oreName = OreDictionary.getOreName(oreID);
			for(String dictName : ORE_BLOCKS_OREDICT) {
				if(oreName.startsWith(dictName)) {
					return true;
				}
			}
		}
		return false;
	}

}
