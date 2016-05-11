package net.blay09.mods.excompressum.entity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityItemNoCombine extends EntityItem {
	public EntityItemNoCombine(World p_i1710_1_, double p_i1710_2_, double p_i1710_4_, double p_i1710_6_, ItemStack p_i1710_8_) {
		super(p_i1710_1_, p_i1710_2_, p_i1710_4_, p_i1710_6_, p_i1710_8_);
	}

	@Override
	public boolean combineItems(EntityItem p_70289_1_) {
		return false;
	}
}
