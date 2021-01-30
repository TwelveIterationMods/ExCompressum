package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.ExCompressumAPI;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.ToolItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.HashSet;

public class OreSmasherItem extends ToolItem {

    public static final String name = "ore_smasher";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public OreSmasherItem(Properties properties) {
        //noinspection ConstantConditions - hide ore smasher for now, there's no useful function for it in current ex nihilo
        super(0f, 0f, ItemTier.DIAMOND, new HashSet<>(), properties.group(null));
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, BlockState state) {
        return ExCompressumAPI.getExNihilo().isHammerableOre(new ItemStack(state.getBlock()));
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (ExCompressumAPI.getExNihilo().isHammerableOre(new ItemStack(state.getBlock()))) {
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
        /* if (!world.checkNoEntityCollision(new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1))) {
            return ActionResultType.FAIL;
        }*/

        for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
            ItemStack inventoryStack = player.inventory.mainInventory.get(i);
            if (!inventoryStack.isEmpty()) {
                if (ExCompressumAPI.getExNihilo().isCompressableOre(inventoryStack)) {
                    CompressedRecipe recipe = ExRegistries.getCompressedRecipeRegistry().getRecipe(inventoryStack);
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

                if (ExCompressumAPI.getExNihilo().isHammerableOre(inventoryStack)) {
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
        if (!world.isRemote && canHarvestBlock(itemStack, state) && ExNihilo.getInstance().isHammerable(state)) {
            world.removeBlock(pos, false);
            Collection<ItemStack> rewards = ExNihilo.getInstance().rollHammerRewards(state, itemStack, world.rand);
            for (ItemStack rewardStack : rewards) {
                world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, rewardStack));
            }
        }

        return super.onBlockDestroyed(itemStack, world, state, pos, entityLiving);
    }

}
