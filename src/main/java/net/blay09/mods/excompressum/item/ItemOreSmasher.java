package net.blay09.mods.excompressum.item;

import com.google.common.collect.Lists;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipe;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipeRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class ItemOreSmasher extends ItemTool {

    private static final List<String> ORE_BLOCKS = Lists.newArrayList();
    private static final List<String> ORE_ITEMS = Lists.newArrayList();
    private static final List<String> ORE_BLOCKS_OREDICT = Lists.newArrayList("oreGravel", "oreNetherGravel", "oreSand");
    private static final List<String> ORE_ITEMS_OREDICT = Lists.newArrayList("oreBroken", "oreNetherBroken", "oreCrushed");

    public static final String name = "ore_smasher";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public ItemOreSmasher() {
        super(0f, 0f, ToolMaterial.DIAMOND, new HashSet<>());
        setUnlocalizedName(registryName.toString());
        setCreativeTab(ExCompressum.creativeTab);
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
        return isOreBlock(new ItemStack(state.getBlock()));
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        if (isOreBlock(new ItemStack(state.getBlock()))) {
            return efficiency;
        }

        return 0.8f;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.checkNoEntityCollision(new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1))) {
            return EnumActionResult.FAIL;
        }

        for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
            ItemStack inventoryStack = player.inventory.mainInventory.get(i);
            if (!inventoryStack.isEmpty()) {
                if (isOreItem(inventoryStack)) {
                    CompressedRecipe recipe = CompressedRecipeRegistry.getRecipe(inventoryStack);
                    if (recipe != null && recipe.getResultStack().getCount() == 1) {
                        if (inventoryStack.getCount() >= recipe.getCount()) {
                            IBlockState oldState = world.getBlockState(pos);
                            ItemStack resultStack = recipe.getResultStack().copy();
                            resultStack.getItem().onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
                            world.notifyBlockUpdate(pos, oldState, world.getBlockState(pos), 3);
                            if (resultStack.isEmpty()) {
                                inventoryStack.shrink(recipe.getCount());
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
        if (!world.isRemote && canHarvestBlock(state, itemStack) && ExRegistro.isHammerable(state)) {
            world.setBlockToAir(pos);
            Collection<ItemStack> rewards = ExRegistro.rollHammerRewards(state, toolMaterial.getHarvestLevel(), EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemStack), world.rand);
            for (ItemStack rewardStack : rewards) {
                world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, rewardStack));
            }
        }

        return super.onBlockDestroyed(itemStack, world, state, pos, entityLiving);
    }

    private boolean isOreItem(ItemStack itemStack) {
        ResourceLocation registryName = itemStack.getItem().getRegistryName();
        if (registryName == null) {
            return false;
        }

        if (ORE_ITEMS.contains(registryName.toString())) {
            return true;
        }

        return matchesOreDict(itemStack, ORE_ITEMS_OREDICT);
    }

    private boolean isOreBlock(ItemStack itemStack) {
        ResourceLocation registryName = itemStack.getItem().getRegistryName();
        if (registryName == null || itemStack.isEmpty()) {
            return false;
        }

        if (ORE_BLOCKS.contains(registryName.toString())) {
            return true;
        }

        return matchesOreDict(itemStack, ORE_BLOCKS_OREDICT);

    }

    private boolean matchesOreDict(ItemStack itemStack, List<String> oreDictEntries) {
        int[] oreIDs = OreDictionary.getOreIDs(itemStack);
        for (int oreID : oreIDs) {
            String oreName = OreDictionary.getOreName(oreID);
            for (String dictName : oreDictEntries) {
                if (oreName.startsWith(dictName)) {
                    return true;
                }
            }
        }

        return false;
    }

}
