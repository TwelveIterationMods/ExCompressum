package net.blay09.mods.excompressum.item;

import com.google.common.collect.Sets;
import cpw.mods.fml.common.registry.GameRegistry;
import exnihilo.blocks.ores.BlockOre;
import exnihilo.items.hammers.IHammer;
import exnihilo.items.ores.ItemOre;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ModItems;
import net.blay09.mods.excompressum.registry.CompressedRecipeRegistry;
import net.blay09.mods.excompressum.registry.data.CompressedRecipe;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemOreSmasher extends ItemTool implements IHammer {

	private static final String[] ORE_BLOCKS = new String[] {
			"oreGravel",
			"oreNetherGravel",
			"oreSand",
			//"oreDust" // We don't want to place oreDust because it can't be broken further.
	};

	private static final String[] ORE_ITEMS = new String[] {
			"oreBroken",
			"oreNetherBroken",
			"oreCrushed",
//			"orePowdered"
	};

    public ItemOreSmasher() {
        super(0f, ToolMaterial.EMERALD, Sets.newHashSet());
        setUnlocalizedName(ExCompressum.MOD_ID + ":ore_smasher");
        setTextureName(ExCompressum.MOD_ID + ":ore_smasher");
        setCreativeTab(ExCompressum.creativeTab);
    }

	@Override
	public boolean canHarvestBlock(Block block, ItemStack itemStack) {
		return isOreBlock(new ItemStack(block));
	}

	@Override
	public float getDigSpeed(ItemStack item, Block block, int meta) {
		if (isOreBlock(new ItemStack(block))) {
			return efficiencyOnProperMaterial;
		}
		return 0.8f;
	}

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        for(int i = 0; i < entityPlayer.inventory.mainInventory.length; i++) {
            ItemStack inventoryStack = entityPlayer.inventory.mainInventory[i];
            if(inventoryStack != null) {
                if(isOreItem(inventoryStack)) {
					CompressedRecipe recipe = CompressedRecipeRegistry.getRecipe(inventoryStack);
					if(recipe != null && recipe.getResultStack().stackSize == 1) {
						if(inventoryStack.stackSize >= recipe.getSourceStack().stackSize) {
							ItemStack resultStack = recipe.getResultStack().copy();
							resultStack.getItem().onItemUse(resultStack, entityPlayer, world, x, y, z, side, hitX, hitY, hitZ);
							if(resultStack.stackSize <= 0) {
								inventoryStack.stackSize -= recipe.getSourceStack().stackSize;
								if (inventoryStack.stackSize <= 0) {
									entityPlayer.inventory.mainInventory[i] = null;
								}
								entityPlayer.swingItem();
								break;
							}
						}
					}
                }
				if (isOreBlock(inventoryStack)) {
					inventoryStack.getItem().onItemUse(inventoryStack, entityPlayer, world, x, y, z, side, hitX, hitY, hitZ);
					if (inventoryStack.stackSize <= 0) {
						entityPlayer.inventory.mainInventory[i] = null;
					}
					entityPlayer.swingItem();
					break;
				}
            }
        }
        return false;
    }

	private boolean isOreItem(ItemStack itemStack) {
		Item item = itemStack.getItem();
		if(item instanceof ItemOre && !item.getUnlocalizedName().endsWith("_powdered")) {
			return true;
		}
		int[] oreIDs = OreDictionary.getOreIDs(itemStack);
		for (int oreID : oreIDs) {
			String oreName = OreDictionary.getOreName(oreID);
			for(String dictName : ORE_ITEMS) {
				if(oreName.startsWith(dictName)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isOreBlock(ItemStack itemStack) {
		Block block = Block.getBlockFromItem(itemStack.getItem());
		if(block instanceof BlockOre && !((BlockOre) block).getName().endsWith("dust")) {
			return true;
		}
		int[] oreIDs = OreDictionary.getOreIDs(itemStack);
		for (int oreID : oreIDs) {
			String oreName = OreDictionary.getOreName(oreID);
			for(String dictName : ORE_BLOCKS) {
				if(oreName.startsWith(dictName)) {
					return true;
				}
			}
		}
		return false;
	}

    @Override
    public boolean isHammer(ItemStack itemStack) {
        return true;
    }

	public static void registerRecipes(Configuration config) {
		if(config.getBoolean("Ore Smasher", "items", true, "If set to false, the recipe for the ore smasher will be disabled.")) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.oreSmasher), " CD", " SC", "S  ", 'C', Blocks.crafting_table, 'D', Items.diamond, 'S', "stickWood"));
		}
	}
}
