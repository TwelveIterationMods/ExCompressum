package net.blay09.mods.excompressum.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityAngryChicken extends EntityMob {

	public EntityAngryChicken(World world) {
		super(world);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0, false));
		tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0));
		tasks.addTask(7, new EntityAIWander(this, 1.0));
		tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
		tasks.addTask(8, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
		setSize(0.3f, 0.7f);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.8);
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0);
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEFINED;
	}

	@Override
	protected String getLivingSound() {
		return "mob.chicken.say";
	}

	@Override
	protected String getHurtSound() {
		return "mob.chicken.hurt";
	}

	@Override
	protected String getDeathSound() {
		return "mob.chicken.hurt";
	}

	@Override
	protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_) {
		playSound("mob.chicken.step", 0.15f, 1.0f);
	}

	@Override
	protected void dropRareDrop(int i) {
	}

	@Override
	protected Item getDropItem() {
		return Items.feather;
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int looting) {
		int featherCount = rand.nextInt(3) + rand.nextInt(1 + looting);
		for (int i = 0; i < featherCount; i++) {
			dropItem(Items.feather, 1);
		}

		if (isBurning()) {
			dropItem(Items.cooked_chicken, 1);
		} else {
			dropItem(Items.chicken, 1);
		}
	}

	@Override
	protected void addRandomArmor() {
	}

}
