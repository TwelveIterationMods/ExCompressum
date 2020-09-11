package net.blay09.mods.excompressum.item;

import com.google.common.collect.Lists;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipe;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipeRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.ToolItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class ItemOreSmasher extends ToolItem {

    private static final List<String> ORE_BLOCKS = Lists.newArrayList();
    private static final List<String> ORE_ITEMS = Lists.newArrayList();
    private static final List<String> ORE_BLOCKS_OREDICT = Lists.newArrayList("oreGravel", "oreNetherGravel", "oreSand");
    private static final List<String> ORE_ITEMS_OREDICT = Lists.newArrayList("oreBroken", "oreNetherBroken", "oreCrushed");

    public static final String name = "ore_smasher";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public ItemOreSmasher(Properties properties) {
        super(0f, 0f, ItemTier.DIAMOND, new HashSet<>(), properties);
    }

    @Override
    public boolean canHarvestBlock(BlockState state, ItemStack stack) {
        return isOreBlock(new ItemStack(state.getBlock()));
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (isOreBlock(new ItemStack(state.getBlock()))) {
            return efficiency;
        }

        return 0.8f;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        final PlayerEntity player = context.getPlayer();
        if (player == null) {
            return ActionResultType.FAIL;
        }

        final World world = context.getWorld();
        final BlockPos pos = context.getPos();
        if (!world.checkNoEntityCollision(new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1))) {
            return ActionResultType.FAIL;
        }

        for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
            ItemStack inventoryStack = player.inventory.mainInventory.get(i);
            if (!inventoryStack.isEmpty()) {
                if (isOreItem(inventoryStack)) {
                    CompressedRecipe recipe = CompressedRecipeRegistry.getRecipe(inventoryStack);
                    if (recipe != null && recipe.getResultStack().getCount() == 1) {
                        if (inventoryStack.getCount() >= recipe.getCount()) {
                            BlockState oldState = world.getBlockState(pos);
                            ItemStack resultStack = recipe.getResultStack().copy();
                            resultStack.getItem().onItemUse(new ItemUseContext(player, context.getHand(), new BlockRayTraceResult(context.getHitVec(), context.getFace(), context.getPos(), context.isInside())));
                            world.notifyBlockUpdate(pos, oldState, world.getBlockState(pos), 3);
                            if (resultStack.isEmpty()) {
                                inventoryStack.shrink(recipe.getCount());
                                if (inventoryStack.isEmpty()) {
                                    player.inventory.mainInventory.remove(i);
                                }
                                player.swingArm(context.getHand());
                                return ActionResultType.SUCCESS;
                            }
                        }
                    }
                }

                if (isOreBlock(inventoryStack)) {
                    BlockState oldState = world.getBlockState(pos);
                    inventoryStack.getItem().onItemUse(new ItemUseContext(player, context.getHand(), new BlockRayTraceResult(context.getHitVec(), context.getFace(), context.getPos(), context.isInside())));
                    world.notifyBlockUpdate(pos, oldState, world.getBlockState(pos), 3);
                    if (inventoryStack.isEmpty()) {
                        player.inventory.mainInventory.remove(i);
                    }
                    player.swingArm(context.getHand());
                    return ActionResultType.SUCCESS;
                }
            }
        }

        return ActionResultType.FAIL;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World world, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (!world.isRemote && canHarvestBlock(state, itemStack) && ExRegistro.isHammerable(state)) {
            world.removeBlock(pos, false);
            Collection<ItemStack> rewards = ExRegistro.rollHammerRewards(state, toolMaterial.getHarvestLevel(), EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemStack), world.rand);
            for (ItemStack rewardStack : rewards) {
                world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, rewardStack));
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
