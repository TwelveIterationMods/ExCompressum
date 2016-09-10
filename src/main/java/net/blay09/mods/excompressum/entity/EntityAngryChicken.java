package net.blay09.mods.excompressum.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;

// TODO they are being terrible
public class EntityAngryChicken extends EntityMob {

	public EntityAngryChicken(World world) {
		super(world);

		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(2, new EntityAIAttackMelee(this, 1.0, false));
		tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0));
		tasks.addTask(7, new EntityAIWander(this, 1.0));
		tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8f));
		tasks.addTask(8, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
		setSize(0.3f, 0.7f);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0);
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEFINED;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_CHICKEN_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound() {
		return SoundEvents.ENTITY_CHICKEN_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_CHICKEN_HURT;
	}

	@Override
	protected void playStepSound(BlockPos pos, Block block) {
		super.playStepSound(pos, block);
		playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15f, 1.0f);
	}

	@Override
	protected Item getDropItem() {
		return Items.FEATHER;
	}

	@Override
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
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
	}

}