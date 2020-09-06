package net.blay09.mods.excompressum.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class AngryChickenEntity extends MobEntity {

    public AngryChickenEntity(EntityType<? extends AngryChickenEntity> type, World world) {
        super(type, world);

        // TODO size? etc. copy from chicken/zombie
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4f));
        /*this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1f, true));
        this.goalSelector.addGoal(6, new MoveTowardsRestrictionGoal(this, 6f));
        this.goalSelector.addGoal(7, new RandomWalkingGoal(this, 1f));*/
    }

    /*@Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25.0);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.45);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0);
    }*/

    /*@Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEFINED;
    }
*/
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
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        super.playStepSound(pos, blockIn);
        playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15f, 1.0f);
    }

// TODO loot table	protected Item getDropItem() {  chickenstick }

	/* TODO loot table
	protected void dropFewItems(boolean wasRecentlyHit, int looting) {
		int featherCount = rand.nextInt(3) + rand.nextInt(1 + looting);
		for (int i = 0; i < featherCount; i++) {
			dropItem(Items.FEATHER, 1);
		}

		if (isBurning()) {
			dropItem(Items.COOKED_CHICKEN, 1);
		} else {
			dropItem(Items.CHICKEN, 1);
		}

		EntityItem chickenStick = dropItem(ModItems.chickenStick, 1);
		if(chickenStick != null) {
			NBTTagCompound tagCompound = new NBTTagCompound();
			tagCompound.setBoolean("IsAngry", true);
			chickenStick.getItem().setTagCompound(tagCompound);
		}
	}*/

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
    }

}
