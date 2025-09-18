package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.tag.ModBlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.state.BlockState;

public class CompressedCrookItem extends Item {

    public CompressedCrookItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity entity, InteractionHand hand) {
        pushEntity(itemStack, player, entity, hand);
        return InteractionResult.SUCCESS;
    }

    public static void pushEntity(ItemStack itemStack, Player player, Entity entity, InteractionHand hand) {
        if (!player.level().isClientSide()) {
            double distance = Math.sqrt(Math.pow(player.getX() - entity.getX(), 2) + Math.pow(player.getZ() - entity.getZ(), 2));
            double scalarX = (player.getX() - entity.getX()) / distance;
            double scalarZ = (player.getZ() - entity.getZ()) / distance;
            double strength = 2.0;
            double velX = 0.0 - scalarX * strength;
            double velY = player.getY() < entity.getY() ? 0.5 : 0.0;
            double velZ = 0.0 - scalarZ * strength;
            entity.push(velX, velY, velZ);
        }
        itemStack.hurtAndBreak(1, player, hand.asEquipmentSlot());
    }

    @Override
    public float getDestroySpeed(ItemStack itemStack, BlockState state) {
        final var speed = ExCompressumConfig.getActive().tools.compressedCrookSpeedMultiplier;
        return state.is(ModBlockTags.MINEABLE_WITH_CROOK) ? (float) (ToolMaterial.WOOD.speed() * speed) : 0f;
    }

}
