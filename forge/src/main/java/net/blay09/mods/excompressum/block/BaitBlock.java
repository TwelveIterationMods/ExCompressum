package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.block.entity.ModBlockEntities;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.block.entity.BaitBlockEntity;
import net.blay09.mods.excompressum.block.entity.EnvironmentalCondition;
import net.blay09.mods.excompressum.utils.Messages;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BaitBlock extends BaseEntityBlock {

    public static final String nameSuffix = "_bait";

    private static final VoxelShape BOUNDING_BOX = Shapes.box(0.1, 0, 0.1, 0.9, 0.1, 0.9);

    private final BaitType baitType;

    public BaitBlock(BaitType baitType) {
        super(Properties.of(Material.CAKE).strength(0.1f));
        this.baitType = baitType;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        return BOUNDING_BOX;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BaitBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof BaitBlockEntity bait) {
            EnvironmentalCondition environmentStatus = bait.checkSpawnConditions(true);
            if (!level.isClientSide) {
                TranslatableComponent chatComponent = new TranslatableComponent(environmentStatus.langKey);
                chatComponent.withStyle(environmentStatus != EnvironmentalCondition.CanSpawn ? ChatFormatting.RED : ChatFormatting.GREEN);
                player.sendMessage(chatComponent, Util.NIL_UUID);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (placer instanceof Player) {
            if (level.getBlockEntity(pos) instanceof BaitBlockEntity bait) {
                EnvironmentalCondition environmentStatus = bait.checkSpawnConditions(true);
                if (!level.isClientSide) {
                    TranslatableComponent chatComponent = new TranslatableComponent(environmentStatus.langKey);
                    chatComponent.withStyle(environmentStatus != EnvironmentalCondition.CanSpawn ? ChatFormatting.RED : ChatFormatting.GREEN);
                    placer.sendMessage(chatComponent, Util.NIL_UUID);
                }
            }
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random rand) {
        if (!ExCompressumConfig.getActive().client.disableParticles) {
            if (level.getBlockEntity(pos) instanceof BaitBlockEntity bait && bait.checkSpawnConditions(false) == EnvironmentalCondition.CanSpawn) {
                if (rand.nextFloat() <= 0.2f) {
                    level.addParticle(ParticleTypes.SMOKE, pos.getX() + rand.nextFloat(), pos.getY() + rand.nextFloat() * 0.5f, pos.getZ() + rand.nextFloat(), 0.0, 0.0, 0.0);
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag flagIn) {
        if (baitType == BaitType.SQUID) {
            tooltip.add(Messages.lang("tooltip.baitPlaceInWater"));
        }
    }

    public BaitType getBaitType() {
        return baitType;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide ? createTickerHelper(type, ModBlockEntities.bait.get(), BaitBlockEntity::serverTick) : null;
    }
}
