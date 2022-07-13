package net.blay09.mods.excompressum.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Random;

public class AngryChickenEntity extends PathfinderMob {

    private final ServerBossEvent bossInfo = (ServerBossEvent) (new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);

    public AngryChickenEntity(EntityType<? extends AngryChickenEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createEntityAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE, 2)
                .add(Attributes.ATTACK_KNOCKBACK, 1)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0)
                .add(Attributes.MAX_HEALTH, 50)
                .add(Attributes.MOVEMENT_SPEED, 0.45);
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);
        bossInfo.setName(getDisplayName());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(4, new AngryChickenAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8f));
        this.goalSelector.addGoal(5, new LeapAtTargetGoal(this, 0.4f));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        bossInfo.setProgress(getHealth() / getMaxHealth());
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.CHICKEN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.CHICKEN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CHICKEN_HURT;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        if (hasCustomName()) {
            bossInfo.setName(getDisplayName());
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        bossInfo.removePlayer(player);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        super.playStepSound(pos, state);
        playSound(SoundEvents.CHICKEN_STEP, 0.15f, 1.0f);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();

        if (deathTime == 18) {
            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
            if (lightningBolt != null) {
                lightningBolt.setVisualOnly(true);
                lightningBolt.moveTo(Vec3.atBottomCenterOf(blockPosition()));
                level.addFreshEntity(lightningBolt);
            }

            Random random = new Random();
            for (int i = 0; i < 3; i++) {
                int offX = random.nextInt(3) - 2;
                int offZ = random.nextInt(3) - 2;
                for (int y = 0; y <= 1; y++) {
                    BlockPos pos = blockPosition().offset(offX, y, offZ);
                    BlockState state = level.getBlockState(pos);
                    if (state.isAir() || state.getMaterial().isReplaceable()) {
                        BlockState fireState = BaseFireBlock.getState(level, pos);
                        level.setBlock(pos, fireState, 11);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void knockback(double strength, double ratioX, double ratioZ) {
        super.knockback(strength * 2f, ratioX, ratioZ);
    }

    @Override
    public void tick() {
        super.tick();

        if (tickCount < 25) {
            this.refreshDimensions();
        }
    }


    @Override
    public float getScale() {
        float growProgress = easeInOutCubic(Math.min(1f, (tickCount - 5) / 20f));
        float maxSize = 1.15f;
        return 1f + maxSize * growProgress;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
        return size.height * 0.92f;
    }

    @Override
    protected int getExperienceReward(Player player) {
        return 25;
    }

    private static float easeInOutCubic(float x) {
        return x < 0.5 ? 4 * x * x * x : (float) (1 - Math.pow(-2 * x + 2, 3) / 2);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.isProjectile()) {
            Entity arrow = source.getDirectEntity();
            Entity attacker = source.getEntity();
            if (!level.isClientSide && arrow != null && !(attacker instanceof AngryChickenEntity)) {
                ((ServerLevel) level).sendParticles(ParticleTypes.CLOUD, getX(), getY() + 1f, getZ(), 50, 0.25f, 0.25f, 0.25f, 0.2f);
                if (arrow instanceof Arrow && attacker instanceof LivingEntity) {
                    Arrow reflectedArrow = new Arrow(level, this);
                    reflectedArrow.copyPosition(arrow);
                    level.addFreshEntity(reflectedArrow);
                    double dirX = attacker.getX() - arrow.getX();
                    double dirY = (attacker.getY() + 1.8f) - arrow.getY();
                    double dirZ = attacker.getZ() - arrow.getZ();
                    reflectedArrow.shoot(dirX, dirY, dirZ, 1f, 10f);
                    arrow.remove(RemovalReason.DISCARDED);
                }
            }
            return false;
        }

        return super.hurt(source, amount);
    }
}
