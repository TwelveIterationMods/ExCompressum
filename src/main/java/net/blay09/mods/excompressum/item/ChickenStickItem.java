package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.List;

public class ChickenStickItem extends ToolItem {

    public static final String name = "chicken_stick";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public ChickenStickItem(Item.Properties properties) {
        super(0f, 0f, ChickenStickTier.INSTANCE, new HashSet<>(), properties.isImmuneToFire());
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        tryPlayChickenSound(attacker.world, new BlockPos(attacker.getPosX(), attacker.getPosY(), attacker.getPosZ()));
        return super.hitEntity(itemStack, attacker, target);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        tryPlayChickenSound(world, new BlockPos(player.getPosX(), player.getPosY(), player.getPosZ()));
        player.swingArm(hand);
        return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    public boolean canHarvestBlock(BlockState state) {
        return ExRegistries.getChickenStickRegistry().isHammerable(state);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if ((ExRegistries.getChickenStickRegistry().isHammerable(state))) {
            if (isAngry(stack)) {
                return efficiency * 1.5f;
            }
            return efficiency;
        }
        return 0.8f;
    }

    public void tryPlayChickenSound(IWorld world, BlockPos pos) {
        if (world.getRandom().nextFloat() <= ExCompressumConfig.COMMON.chickenStickSoundChance.get()) {
            ResourceLocation location = null;
            final List<? extends String> chickenStickSounds = ExCompressumConfig.COMMON.chickenStickSounds.get();
            if (!chickenStickSounds.isEmpty()) {
                location = new ResourceLocation(chickenStickSounds.get(world.getRandom().nextInt(chickenStickSounds.size())));
            }
            if (location != null) {
                SoundEvent soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(location);
                if (soundEvent != null) {
                    world.playSound(null, pos, soundEvent, SoundCategory.PLAYERS, 1f, world.getRandom().nextFloat() * 0.1f + 0.9f);
                } else {
                    ExCompressum.logger.warn("Chicken Stick tried to play a sound that does not exist: {}", location);
                }
            }
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return isAngry(stack);
    }

    public boolean isAngry(ItemStack itemStack) {
        return itemStack.getTag() != null && itemStack.getTag().getBoolean("IsAngry");
    }

}
