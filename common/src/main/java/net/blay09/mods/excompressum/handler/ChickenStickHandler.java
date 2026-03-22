package net.blay09.mods.excompressum.handler;

import net.blay09.mods.balm.platform.event.callback.BlockCallback;
import net.blay09.mods.balm.platform.event.callback.PlayerCallback;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.entity.AngryChickenEntity;
import net.blay09.mods.excompressum.entity.ModEntities;
import net.blay09.mods.excompressum.item.ChickenStickItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ChickenStickHandler {

    public static void initialize() {
        PlayerCallback.Attack.Before.EVENT.register(ChickenStickHandler::onPlayerAttack);
        BlockCallback.Break.Before.EVENT.register(ChickenStickHandler::onBlockBreak);
    }

    public static boolean onPlayerAttack(Player player, Entity target) {
        if (!ExCompressumConfig.getActive().tools.allowChickenStickCreation) {
            return true;
        }

        if (target instanceof Chicken chicken && !chicken.isBaby()) {
            ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (!heldItem.isEmpty() && heldItem.getItem() == Items.STICK) {
                chicken.remove(Entity.RemovalReason.DISCARDED);

                Level level = chicken.level();
                if (!level.isClientSide()) {
                    if (!player.getAbilities().instabuild) {
                        heldItem.shrink(1);
                    }

                    if (heldItem.isEmpty()) {
                        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                    }

                    AngryChickenEntity angryChicken = ModEntities.angryChicken.value().create(level, EntitySpawnReason.CONVERSION);
                    angryChicken.snapTo(chicken.getX(), chicken.getY(), chicken.getZ(), chicken.getYRot(), chicken.getXRot());
                    angryChicken.absSnapTo(chicken.getX(), chicken.getY(), chicken.getZ(), chicken.getYRot(), chicken.getXRot());
                    angryChicken.setYHeadRot(chicken.yHeadRot);
                    level.addFreshEntity(angryChicken);
                    level.playSound(null, angryChicken.blockPosition(), SoundEvents.CHICKEN_HURT_BABY.value(), SoundSource.HOSTILE, 1f, 0.5f);
                    level.playSound(null, angryChicken.blockPosition(), SoundEvents.WITHER_SPAWN, SoundSource.HOSTILE, 1f, 0.5f);
                    ((ServerLevel) level).sendParticles(ParticleTypes.ANGRY_VILLAGER,
                            angryChicken.getX(),
                            angryChicken.getY(),
                            angryChicken.getZ(),
                            200,
                            0.25f,
                            0.1f,
                            0.25f,
                            1f);
                }
                return false;
            }
        }

        return true;
    }

    public static boolean onBlockBreak(LevelAccessor levelAccessor, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, @Nullable Player player) {
        final var heldItem = player.getMainHandItem();
        if (heldItem.getItem() instanceof ChickenStickItem chickenStickItem && levelAccessor instanceof Level level) {
            chickenStickItem.tryPlayChickenSound(levelAccessor, pos);

            if (levelAccessor.getRandom().nextFloat() <= ExCompressumConfig.getActive().tools.chickenStickSpawnChance) {
                final var chicken = new Chicken(EntityType.CHICKEN, level);
                chicken.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                levelAccessor.addFreshEntity(chicken);
            }
        }

        return true;
    }

}
