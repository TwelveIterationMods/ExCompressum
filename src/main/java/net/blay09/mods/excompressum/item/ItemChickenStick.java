package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;

public class ItemChickenStick extends ToolItem {

    public static final String name = "chicken_stick";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public ItemChickenStick(Item.Properties properties) {
        super(0f, 0f, ChickenStickTier.INSTANCE, new HashSet<>(), properties);
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        playChickenSound(attacker.world, new BlockPos(attacker.getPosX(), attacker.getPosY(), attacker.getPosZ()));
        return super.hitEntity(itemStack, attacker, target);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        playChickenSound(world, new BlockPos(player.getPosX(), player.getPosY(), player.getPosZ()));
        player.swingArm(hand);
        return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    public boolean canHarvestBlock(BlockState state) {
        return ChickenStickRegistry.isHammerable(state);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if ((ChickenStickRegistry.isHammerable(state))) {
            if (isAngry(stack)) {
                return efficiency * 1.5f;
            }
            return efficiency;
        }
        return 0.8f;
    }

    private void playChickenSound(World world, BlockPos pos) {
        if (world.rand.nextFloat() <= ExCompressumConfig.tools.chickenStickSoundChance) {
            ResourceLocation location = null;
            if (ExCompressumConfig.tools.chickenStickSounds.length > 0) {
                location = new ResourceLocation(ExCompressumConfig.tools.chickenStickSounds[world.rand.nextInt(ExCompressumConfig.tools.chickenStickSounds.length)]);
            }
            if (location != null) {
                SoundEvent soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(location);
                if (soundEvent != null) {
                    world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, soundEvent, SoundCategory.PLAYERS, 1f, world.rand.nextFloat() * 0.1f + 0.9f, false);
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
