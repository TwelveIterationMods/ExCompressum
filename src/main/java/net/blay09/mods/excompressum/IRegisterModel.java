package net.blay09.mods.excompressum;

import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IRegisterModel { // literally
	@SideOnly(Side.CLIENT)
	void registerModel(Item item);
}
