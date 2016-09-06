package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipeRegistry;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashSet;

public class ItemOreSmasher extends ItemTool {

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

	private static final String[] ORE_ITEMS = new String[] {
			"exnihiloomnia:ore_broken",
			"exnihiloomnia:ore_broken_nether",
			"exnihiloomnia:ore_broken_ender",
			"exnihiloomnia:ore_crushed",
			"exnihiloomnia:ore_powder"
	};

	private static final String[] ORE_ITEMS_OREDICT = new String[] {
			"oreBroken",
			"oreNetherBroken",
			"oreCrushed"
	};

    public ItemOreSmasher() {
        super(0f, 0f, ToolMaterial.DIAMOND, new HashSet<Block>());
		setRegistryName("ore_smasher");
        setUnlocalizedName(getRegistryName().toString());
        setCreativeTab(ExCompressum.creativeTab);
    }

	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
		return isOreBlock(new ItemStack(state.getBlock()));
	}

	/*@Override // TODO so uh... what happened to getDigSpeed?
	public float getDigSpeed(ItemStack item, Block block, int meta) {
		if (isOreBlock(new ItemStack(block))) {
			return efficiencyOnProperMaterial;
		}
		return 0.8f;
	}*/

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!world.checkNoEntityCollision(new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1))) {
			return EnumActionResult.FAIL;
		}
        for(int i = 0; i < player.inventory.mainInventory.length; i++) {
            ItemStack inventoryStack = player.inventory.mainInventory[i];
            if(inventoryStack != null) {
                if(isOreItem(inventoryStack)) {
					CompressedRecipe recipe = CompressedRecipeRegistry.getRecipe(inventoryStack);
					if(recipe != null && recipe.getResultStack().stackSize == 1) {
						if(inventoryStack.stackSize >= recipe.getSourceStack().stackSize) {
							IBlockState oldState = world.getBlockState(pos);
							ItemStack resultStack = recipe.getResultStack().copy();
							resultStack.getItem().onItemUse(resultStack, player, world, pos, hand, facing, hitX, hitY, hitZ);
							world.notifyBlockUpdate(pos, oldState, world.getBlockState(pos), 3);
							if(resultStack.stackSize <= 0) {
								inventoryStack.stackSize -= recipe.getSourceStack().stackSize;
								if (inventoryStack.stackSize <= 0) {
									player.inventory.mainInventory[i] = null;
								}
								player.swingArm(hand);
								return EnumActionResult.SUCCESS;
							}
						}
					}
                }
				if (isOreBlock(inventoryStack)) {
					IBlockState oldState = world.getBlockState(pos);
					inventoryStack.getItem().onItemUse(inventoryStack, player, world, pos, hand, facing, hitX, hitY, hitZ);
					world.notifyBlockUpdate(pos, oldState, world.getBlockState(pos), 3);
					if (inventoryStack.stackSize <= 0) {
						player.inventory.mainInventory[i] = null;
					}
					player.swingArm(hand);
					return EnumActionResult.SUCCESS;
				}
            }
        }
        return EnumActionResult.FAIL;
    }

	private boolean isOreItem(ItemStack itemStack) {
		String registryName = itemStack.getItem().getRegistryName().toString();
		if (ArrayUtils.contains(ORE_ITEMS, registryName)) {
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
		String registryName = itemStack.getItem().getRegistryName().toString();
		if (ArrayUtils.contains(ORE_BLOCKS, registryName)) {
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
