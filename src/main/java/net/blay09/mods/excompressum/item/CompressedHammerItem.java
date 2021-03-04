package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.ExNihilo;

import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.ToolItem;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.world.World;

import java.util.HashSet;

public class CompressedHammerItem extends ToolItem implements ICompressedHammer {

    public static final String namePrefix = "compressed_hammer_";

    public CompressedHammerItem(ItemTier tier, Properties properties) {
        super(6f, -3.2f, tier, new HashSet<>(), properties);
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, BlockState state) {
        RecipeManager recipeManager = ExCompressum.proxy.getRecipeManager(null);
        ItemStack itemStack = StupidUtils.getItemStackFromState(state);
        return ExRegistries.getCompressedHammerRegistry().isHammerable(recipeManager, itemStack) || ExNihilo.getInstance().isHammerable(state);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        RecipeManager recipeManager = ExCompressum.proxy.getRecipeManager(null);
        ItemStack itemStack = StupidUtils.getItemStackFromState(state);
        if ((ExRegistries.getCompressedHammerRegistry().isHammerable(recipeManager, itemStack) || ExNihilo.getInstance().isHammerable(state)) && state.getBlock().getHarvestLevel(state) <= getTier().getHarvestLevel()) {
            return efficiency * 0.75f;
        }
        return 0.8f;
    }

    @Override
    public boolean canHammer(ItemStack itemStack, World world, BlockState state, PlayerEntity entityPlayer) {
        return true;
    }

    @Override
    public int getHammerLevel(ItemStack itemStack, World world, BlockState state, PlayerEntity entityPlayer) {
        return getTier().getHarvestLevel();
    }
}
