package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.List;

public class ChickenStickItem extends DiggerItem {

    public ChickenStickItem(Item.Properties properties) {
        super(0f, 0f, ChickenStickTier.INSTANCE, ModTags.MINEABLE_WITH_HAMMER, properties.fireResistant());
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        tryPlayChickenSound(attacker.level, new BlockPos(attacker.getX(), attacker.getY(), attacker.getZ()));
        return super.hurtEnemy(itemStack, attacker, target);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        tryPlayChickenSound(level, new BlockPos(player.getX(), player.getY(), player.getZ()));
        player.swing(hand);
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState state) {
        RecipeManager recipeManager = ExCompressum.proxy.getRecipeManager(null);
        return ExRegistries.getChickenStickRegistry().isHammerable(recipeManager, new ItemStack(state.getBlock()));
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        RecipeManager recipeManager = ExCompressum.proxy.getRecipeManager(null);
        if ((ExRegistries.getChickenStickRegistry().isHammerable(recipeManager, new ItemStack(state.getBlock())))) {
            if (isAngry(stack)) {
                return speed * 1.5f;
            }
            return speed;
        }
        return 0.8f;
    }

    public void tryPlayChickenSound(LevelAccessor level, BlockPos pos) {
        if (level.getRandom().nextFloat() <= ExCompressumConfig.getActive().tools.chickenStickSoundChance) {
            ResourceLocation location = null;
            final List<? extends String> chickenStickSounds = ExCompressumConfig.getActive().tools.chickenStickSounds;
            if (!chickenStickSounds.isEmpty()) {
                location = new ResourceLocation(chickenStickSounds.get(level.getRandom().nextInt(chickenStickSounds.size())));
            }
            if (location != null) {
                SoundEvent soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(location);
                if (soundEvent != null) {
                    level.playSound(null, pos, soundEvent, SoundSource.PLAYERS, 1f, level.getRandom().nextFloat() * 0.1f + 0.9f);
                } else {
                    ExCompressum.logger.warn("Chicken Stick tried to play a sound that does not exist: {}", location);
                }
            }
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return isAngry(stack);
    }

    public boolean isAngry(ItemStack itemStack) {
        return itemStack.getTag() != null && itemStack.getTag().getBoolean("IsAngry");
    }

}
