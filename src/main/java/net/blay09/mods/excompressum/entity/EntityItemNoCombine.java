package net.blay09.mods.excompressum.entity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityItemNoCombine extends EntityItem {
	public EntityItemNoCombine(World world, double x, double y, double z, ItemStack itemStack) {
		super(world, x, y, z, itemStack);
	}

	// TODO combineItems is private now ... AT on searchForOtherItemsNearby and override that
}
