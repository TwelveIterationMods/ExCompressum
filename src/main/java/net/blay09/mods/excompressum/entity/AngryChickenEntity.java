package net.blay09.mods.excompressum.entity;

import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public class AngryChickenEntity extends CreatureEntity {

    private final ServerBossInfo bossInfo = (ServerBossInfo) (new ServerBossInfo(getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);

    public AngryChickenEntity(EntityType<? extends AngryChickenEntity> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute createEntityAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 2)
                .createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 1)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0)
                .createMutableAttribute(Attributes.MAX_HEALTH, 50)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.45);
    }

    @Override
    public void setCustomName(@Nullable ITextComponent name) {
        super.setCustomName(name);
        bossInfo.setName(getDisplayName());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(4, new AngryChickenAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(5, new LeapAtTargetGoal(this, 0.4f));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        bossInfo.setPercent(getHealth() / getMaxHealth());
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_CHICKEN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENTITY_CHICKEN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CHICKEN_HURT;
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);

        if (hasCustomName()) {
            bossInfo.setName(getDisplayName());
        }
    }

    @Override
    public void addTrackingPlayer(ServerPlayerEntity player) {
        super.addTrackingPlayer(player);
        bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(ServerPlayerEntity player) {
        super.removeTrackingPlayer(player);
        bossInfo.removePlayer(player);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        super.playStepSound(pos, blockIn);
        playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15f, 1.0f);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
    }

    @Override
    protected void onDeathUpdate() {
        super.onDeathUpdate();

        if (deathTime == 18) {
            LightningBoltEntity lightningBolt = EntityType.LIGHTNING_BOLT.create(world);
            if (lightningBolt != null) {
                lightningBolt.setEffectOnly(true);
                lightningBolt.moveForced(Vector3d.copyCenteredHorizontally(getPosition()));
                world.addEntity(lightningBolt);
            }

            Random random = new Random();
            for (int i = 0; i < 3; i++) {
                int offX = random.nextInt(3) - 2;
                int offZ = random.nextInt(3) - 2;
                for (int y = 0; y <= 1; y++) {
                    BlockPos pos = getPosition().add(offX, y, offZ);
                    BlockState state = world.getBlockState(pos);
                    if (state.isAir() || state.getMaterial().isReplaceable()) {
                        BlockState fireState = AbstractFireBlock.getFireForPlacement(world, pos);
                        world.setBlockState(pos, fireState, 11);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void applyKnockback(float strength, double ratioX, double ratioZ) {
        super.applyKnockback(strength * 2f, ratioX, ratioZ);
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (ticksExisted < 25) {
            this.recalculateSize();
        }
    }


    @Override
    public float getRenderScale() {
        float growProgress = easeInOutCubic(Math.min(1f, (ticksExisted - 5) / 20f));
        float maxSize = 1.15f;
        return 1f + maxSize * growProgress;
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize size) {
        return size.height * 0.92f;
    }

    @Override
    protected int getExperiencePoints(PlayerEntity player) {
        return 25;
    }

    private static float easeInOutCubic(float x) {
        return x < 0.5 ? 4 * x * x * x : (float) (1 - Math.pow(-2 * x + 2, 3) / 2);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.isProjectile()) {
            Entity arrow = source.getImmediateSource();
            Entity attacker = source.getTrueSource();
            if (!world.isRemote && arrow != null && !(attacker instanceof AngryChickenEntity)) {
                ((ServerWorld) world).spawnParticle(ParticleTypes.CLOUD, getPosX(), getPosY() + 1f, getPosZ(), 50, 0.25f, 0.25f, 0.25f, 0.2f);
                if (arrow instanceof ArrowEntity && attacker instanceof LivingEntity) {
                    ArrowEntity reflectedArrow = new ArrowEntity(world, this);
                    reflectedArrow.copyLocationAndAnglesFrom(arrow);
                    world.addEntity(reflectedArrow);
                    double dirX = attacker.getPosX() - arrow.getPosX();
                    double dirY = (attacker.getPosY() + 1.8f) - arrow.getPosY();
                    double dirZ = attacker.getPosZ() - arrow.getPosZ();
                    reflectedArrow.shoot(dirX, dirY, dirZ, 1f, 10f);
                    arrow.remove();
                }
            }
            return false;
        }

        return super.attackEntityFrom(source, amount);
    }
}
