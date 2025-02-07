package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.ExCompressumAPI;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Collection;

public class OreSmasherItem extends DiggerItem {

    public static final String name = "ore_smasher";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public OreSmasherItem(Item.Properties properties) {
        super(0f, 0f, Tiers.DIAMOND, BlockTags.MINEABLE_WITH_SHOVEL, properties);
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState state) {
        return ExCompressumAPI.getExNihilo().isHammerableOre(new ItemStack(state.getBlock()));
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (ExCompressumAPI.getExNihilo().isHammerableOre(new ItemStack(state.getBlock()))) {
            return speed;
        }

        return 0.8f;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        final Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.FAIL;
        }

        final Level level = context.getLevel();
        final BlockPos pos = context.getClickedPos();
        /* if (!world.checkNoEntityCollision(new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1))) {
            return ActionResultType.FAIL;
        }*/

        for (int i = 0; i < player.getInventory().items.size(); i++) {
            ItemStack inventoryStack = player.getInventory().items.get(i);
            if (!inventoryStack.isEmpty()) {
                if (ExCompressumAPI.getExNihilo().isCompressableOre(inventoryStack)) {
                    CompressedRecipe recipe = ExRegistries.getCompressedRecipeRegistry().getRecipe(inventoryStack);
                    if (recipe != null && recipe.getResultStack().getCount() == 1) {
                        if (inventoryStack.getCount() >= recipe.getCount()) {
                            BlockState oldState = level.getBlockState(pos);
                            ItemStack resultStack = recipe.getResultStack().copy();
                            resultStack.getItem().useOn(new UseOnContext(player, context.getHand(), new BlockHitResult(context.getClickLocation(), context.getClickedFace(), context.getClickedPos(), context.isInside())));
                            level.sendBlockUpdated(pos, oldState, level.getBlockState(pos), 3);
                            if (resultStack.isEmpty()) {
                                inventoryStack.shrink(recipe.getCount());
                                if (inventoryStack.isEmpty()) {
                                    player.getInventory().items.remove(i);
                                }
                                player.swing(context.getHand());
                                return InteractionResult.SUCCESS;
                            }
                        }
                    }
                }

                if (ExCompressumAPI.getExNihilo().isHammerableOre(inventoryStack)) {
                    BlockState oldState = level.getBlockState(pos);
                    inventoryStack.getItem().useOn(new UseOnContext(player, context.getHand(), new BlockHitResult(context.getClickLocation(), context.getClickedFace(), context.getClickedPos(), context.isInside())));
                    level.sendBlockUpdated(pos, oldState, level.getBlockState(pos), 3);
                    if (inventoryStack.isEmpty()) {
                        player.getInventory().items.remove(i);
                    }
                    player.swing(context.getHand());
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.FAIL;
    }

    @Override
    public boolean mineBlock(ItemStack itemStack, Level level, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (!level.isClientSide && isCorrectToolForDrops(state) && ExNihilo.getInstance().isHammerable(level, state)) {
            level.removeBlock(pos, false);
            Collection<ItemStack> rewards = ExNihilo.getInstance().rollHammerRewards(level, state, itemStack, level.random);
            for (ItemStack rewardStack : rewards) {
                level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, rewardStack));
            }
        }

        return super.mineBlock(itemStack, level, state, pos, entityLiving);
    }

}
