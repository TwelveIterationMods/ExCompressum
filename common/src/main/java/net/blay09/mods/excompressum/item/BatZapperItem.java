package net.blay09.mods.excompressum.item;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.energy.BalmEnergyStorageProvider;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.common.CommonCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class BatZapperItem extends Item {

    public BatZapperItem(Item.Properties properties) {
        super(properties.durability(ToolMaterial.STONE.durability()));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() != null && context.getPlayer().getAbilities().instabuild) {
            final var blockEntity = context.getLevel().getBlockEntity(context.getClickedPos());
            if (blockEntity != null) {
                if (blockEntity instanceof BalmEnergyStorageProvider energyStorageProvider) {
                    final var energyStorage = energyStorageProvider.getEnergyStorage();
                    if (energyStorage != null) {
                        energyStorage.setEnergy(energyStorage.getCapacity());
                    }
                } else {
                    final var energyStorage = Balm.getCapabilities().getCapability(blockEntity, CommonCapabilities.ENERGY_STORAGE);
                    if (energyStorage != null) {
                        energyStorage.setEnergy(energyStorage.getCapacity());
                    }
                }
            }
        }

        return zapBatter(context.getLevel(), context.getPlayer(), context.getItemInHand(), context.getClickedPos(), context.getHand());
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        return zapBatter(level, player, player.getItemInHand(hand), player.blockPosition(), hand);
    }

    private InteractionResult zapBatter(Level level, Player player, ItemStack itemStack, BlockPos pos, InteractionHand hand) {
        level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.PLAYERS, 1f, level.random.nextFloat() * 0.1f + 0.9f);
        player.swing(hand);

        if (!level.isClientSide) {
            final int range = 5;
            for (Bat entity : level.getEntitiesOfClass(Bat.class,
                    new AABB(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + range, pos.getZ() + range))) {
                entity.hurt(level.damageSources().playerAttack(player), Float.MAX_VALUE);
                ((ServerLevel) level).sendParticles(ParticleTypes.CRIMSON_SPORE, entity.getX(), entity.getY(), entity.getZ(), 50, 0.1f, 0.1f, 0.1f, 0.1f);
            }
        }

        itemStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
        return InteractionResult.SUCCESS;
    }

}
