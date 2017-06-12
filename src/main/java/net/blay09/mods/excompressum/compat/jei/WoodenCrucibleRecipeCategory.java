package net.blay09.mods.excompressum.compat.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IFocus;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRegistryEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nonnull;
import java.util.List;

public class WoodenCrucibleRecipeCategory extends BlankRecipeCategory<WoodenCrucibleRecipe> {

	public static final String UID = "excompressum:woodenCrucible";
	private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/jei_wooden_crucible.png");

	private final IDrawableStatic background;
	private final IDrawableStatic slotHighlight;
	private boolean hasHighlight;
	private int highlightX;
	private int highlightY;

	public WoodenCrucibleRecipeCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(texture, 0, 0, 166, 130);
		this.slotHighlight = guiHelper.createDrawable(texture, 166, 0, 18, 18);
	}

	@Nonnull
	@Override
	public String getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return I18n.format("jei." + UID);
	}

	@Nonnull
	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void drawExtras(@Nonnull Minecraft minecraft) {
		if(hasHighlight) {
			slotHighlight.draw(minecraft, highlightX, highlightY);
		}
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, final WoodenCrucibleRecipe recipeWrapper, final IIngredients ingredients) {
		ItemStack fluidItem = ItemStack.EMPTY;
		if(FluidRegistry.isUniversalBucketEnabled()) {
			fluidItem = new ItemStack(Items.BUCKET);
			IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(fluidItem);
			if(fluidHandler != null) {
				fluidHandler.fill(ingredients.getOutputs(FluidStack.class).get(0).get(0), true);
				fluidItem = fluidHandler.getContainer();
			}
		} else {
			//TODO: Figure out a way to make a filled bucket without using the universal bucket
			//return;
			//fluidItem = FluidContainerRegistry.fillFluidContainer(ingredients.getOutputs(FluidStack.class).get(0), FluidContainerRegistry.EMPTY_BUCKET);
		}
		recipeLayout.getItemStacks().init(0, false, 74, 9);
		recipeLayout.getItemStacks().set(0, fluidItem);

		IFocus<?> focus = recipeLayout.getFocus();
		boolean hasFocus = focus != null && focus.getMode() == IFocus.Mode.INPUT;
		hasHighlight = false;
		final List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
		final int INPUT_SLOTS = 1;
		int slotNumber = 0;
		for (List<ItemStack> input : inputs) {
			final int slotX = 2 + (slotNumber % 9 * 18);
			final int slotY = 36 + (slotNumber / 9 * 18);
			recipeLayout.getItemStacks().init(INPUT_SLOTS + slotNumber, true, slotX, slotY);
			recipeLayout.getItemStacks().set(INPUT_SLOTS + slotNumber, input);
			if(focus != null) {
				Object focusValue = focus.getValue();
				if (hasFocus && focusValue instanceof ItemStack) {
					ItemStack focusStack = (ItemStack) focus.getValue();
					for (ItemStack inputVariant : input) {
						if (focusStack.getItem() == inputVariant.getItem() && focusStack.getItemDamage() == inputVariant.getItemDamage()) {
							hasHighlight = true;
							highlightX = slotX;
							highlightY = slotY;
							break;
						}
					}
				}
			}
			slotNumber++;
		}
		recipeLayout.getItemStacks().addTooltipCallback(new ITooltipCallback<ItemStack>() {
			@Override
			public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip) {
				if(input) {
					WoodenCrucibleRegistryEntry entry = recipeWrapper.getEntryAt(slotIndex - INPUT_SLOTS);
					tooltip.add(recipeWrapper.getFluid().getLocalizedName(ingredients.getOutputs(FluidStack.class).get(0).get(0)));
					tooltip.add(" * " + entry.getAmount() + " mB");
				}
			}
		});
	}
}
