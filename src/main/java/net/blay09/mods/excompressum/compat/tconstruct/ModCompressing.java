package net.blay09.mods.excompressum.compat.tconstruct;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipe;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipeRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.world.BlockEvent;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;

public class ModCompressing extends ModifierTrait {

	private final Multiset<CompressedRecipe> tmpInputItems = HashMultiset.create();

	public ModCompressing() {
		super(Compat.TCONSTRUCT_TRAIT_COMPRESSING, 0xFF0000);
	}

	@Override
	public boolean canApplyTogether(Enchantment enchantment) {
		return enchantment != Enchantments.SILK_TOUCH;
	}

	@Override
	public void blockHarvestDrops(ItemStack tool, BlockEvent.HarvestDropsEvent event) {
		tmpInputItems.clear();
		for (ItemStack itemStack : event.getDrops()) {
			if (!itemStack.isEmpty()) {
				CompressedRecipe compressedRecipe = CompressedRecipeRegistry.getRecipe(itemStack);
				if (compressedRecipe != null) {
					tmpInputItems.add(compressedRecipe, itemStack.getCount());
				}
			}
		}
		NonNullList<ItemStack> newDrops = NonNullList.create();
		for (CompressedRecipe compressedRecipe : tmpInputItems.elementSet()) {
			Ingredient ingredient = compressedRecipe.getIngredient();
			if (tmpInputItems.count(compressedRecipe) >= compressedRecipe.getCount()) {
				int count = compressedRecipe.getCount();
				for (ItemStack itemStack : event.getDrops()) {
					if (!itemStack.isEmpty() && ingredient.apply(itemStack)) {
						int consumed = Math.min(itemStack.getCount(), count);
						count = Math.max(0, count - consumed);
						itemStack.shrink(consumed);
					}
				}
				if (count <= 0) {
					newDrops.add(compressedRecipe.getResultStack().copy());
				}
				break;
			}
		}
		event.getDrops().removeIf(ItemStack::isEmpty);
		event.getDrops().addAll(newDrops);
	}

}
