package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.ExNihilo;

import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;

public class CompressedHammerItem extends DiggerItem implements ICompressedHammer {

    public CompressedHammerItem(Tier tier, Item.Properties properties) {
        super(6f, -3.2f, tier, new HashSet<>(), properties);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        RecipeManager recipeManager = ExCompressum.proxy.getRecipeManager(null);
        ItemStack itemStack = StupidUtils.getItemStackFromState(state);
        return ExRegistries.getCompressedHammerRegistry().isHammerable(recipeManager, itemStack) || ExNihilo.getInstance().isHammerable(state);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        RecipeManager recipeManager = ExCompressum.proxy.getRecipeManager(null);
        ItemStack itemStack = StupidUtils.getItemStackFromState(state);
        if ((ExRegistries.getCompressedHammerRegistry().isHammerable(recipeManager, itemStack) || ExNihilo.getInstance().isHammerable(state)) && state.getBlock().getHarvestLevel(state) <= getTier().getLevel()) {
            return speed * 0.75f;
        }
        return 0.8f;
    }

    @Override
    public boolean canHammer(ItemStack itemStack, Level level, BlockState state, Player player) {
        return true;
    }

    @Override
    public int getHammerLevel(ItemStack itemStack, Level level, BlockState state, Player player) {
        return getTier().getLevel();
    }
}
