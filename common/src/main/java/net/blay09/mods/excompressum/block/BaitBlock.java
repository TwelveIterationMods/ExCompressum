package net.blay09.mods.excompressum.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.excompressum.block.entity.ModBlockEntities;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.block.entity.BaitBlockEntity;
import net.blay09.mods.excompressum.block.entity.EnvironmentalConditionResult;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class BaitBlock extends BaseEntityBlock {

    public static final MapCodec<BaitBlock> CODEC = RecordCodecBuilder.mapCodec((it) -> it.group(BaitType.CODEC.fieldOf("type").forGetter(
                    BaitBlock::getBaitType),
            propertiesCodec()).apply(it, BaitBlock::new));

    private static final VoxelShape BOUNDING_BOX = Shapes.box(0.1, 0, 0.1, 0.9, 0.1, 0.9);

    private final BaitType baitType;

    public BaitBlock(BaitType baitType, Properties properties) {
        super(properties.strength(0.1f));
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
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult blockHitResult) {
        if (!(level.getBlockEntity(pos) instanceof BaitBlockEntity bait)) {
            return InteractionResult.FAIL;
        }

        final var environmentStatus = bait.checkSpawnConditions(true);
        if (!level.isClientSide()) {
            final var chatComponent = Component.translatable(environmentStatus.langKey, environmentStatus.params);
            chatComponent.withStyle(environmentStatus != EnvironmentalConditionResult.CanSpawn ? ChatFormatting.RED : ChatFormatting.GREEN);
            player.sendSystemMessage(chatComponent);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (placer instanceof Player player && level.getBlockEntity(pos) instanceof BaitBlockEntity bait) {
            final var environmentStatus = bait.checkSpawnConditions(true);
            if (!level.isClientSide()) {
                final var chatComponent = Component.translatable(environmentStatus.langKey, environmentStatus.params);
                chatComponent.withStyle(environmentStatus != EnvironmentalConditionResult.CanSpawn ? ChatFormatting.RED : ChatFormatting.GREEN);
                player.sendSystemMessage(chatComponent);
            }
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
        if (!ExCompressumConfig.getActive().client.disableParticles) {
            if (level.getBlockEntity(pos) instanceof BaitBlockEntity bait && bait.checkSpawnConditions(false) == EnvironmentalConditionResult.CanSpawn) {
                if (rand.nextFloat() <= 0.2f) {
                    level.addParticle(ParticleTypes.SMOKE,
                            pos.getX() + rand.nextFloat(),
                            pos.getY() + rand.nextFloat() * 0.5f,
                            pos.getZ() + rand.nextFloat(),
                            0.0,
                            0.0,
                            0.0);
                }
            }
        }
    }

    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flagIn) {
        if (baitType == BaitType.SQUID) {
            tooltip.accept(Component.translatable("tooltip.excompressum.baitPlaceInWater"));
        }
    }

    public BaitType getBaitType() {
        return baitType;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide() ? createTickerHelper(type, ModBlockEntities.bait.value(), BaitBlockEntity::serverTick) : null;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
