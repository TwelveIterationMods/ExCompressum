package net.blay09.mods.excompressum.compat.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeCategory;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRegistryEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

public class WoodenCrucibleRecipeCategory implements IRecipeCategory<WoodenCrucibleRecipe> {

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
	public void drawAnimations(@Nonnull Minecraft minecraft) {

	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull final WoodenCrucibleRecipe recipeWrapper) {
		ItemStack fluidItem;
		if(FluidRegistry.isUniversalBucketEnabled()) {
			fluidItem = new ItemStack(Items.BUCKET);
			IFluidHandler fluidHandler = fluidItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
			fluidHandler.fill(recipeWrapper.getFluidOutputs().get(0), true);
		} else {
			fluidItem = FluidContainerRegistry.fillFluidContainer(recipeWrapper.getFluidOutputs().get(0), FluidContainerRegistry.EMPTY_BUCKET);
		}
		recipeLayout.getItemStacks().init(0, false, 74, 9);
		recipeLayout.getItemStacks().set(0, fluidItem);

		IFocus<ItemStack> focus = recipeLayout.getItemStacks().getFocus();
		boolean hasFocus = focus.getMode() == IFocus.Mode.INPUT;
		hasHighlight = false;
		final List inputs = recipeWrapper.getInputs();
		final int INPUT_SLOTS = 1;
		int slotNumber = 0;
		for(int i = 0; i < inputs.size(); i++) {
			final int slotX = 2 + (slotNumber % 9 * 18);
			final int slotY = 36 + (slotNumber / 9 * 18);
			recipeLayout.getItemStacks().init(INPUT_SLOTS + slotNumber, true, slotX, slotY);
			Object input = inputs.get(i);
			if(input instanceof Collection) {
				//noinspection unchecked /// Thanks Java, you're the best <3
				recipeLayout.getItemStacks().set(INPUT_SLOTS + slotNumber, (Collection<ItemStack>) input);
			} else if (input instanceof ItemStack) {
				ItemStack outputItemStack = (ItemStack) input;
				recipeLayout.getItemStacks().set(INPUT_SLOTS + slotNumber, outputItemStack);
				ItemStack focusStack = focus.getValue();
				if(hasFocus && focusStack != null
						&& focusStack.getItem() == outputItemStack.getItem()
						&& focusStack.getItemDamage() == outputItemStack.getItemDamage()) {
					hasHighlight = true;
					highlightX = slotX;
					highlightY = slotY;
				}
			}
			slotNumber++;
		}
		recipeLayout.getItemStacks().addTooltipCallback(new ITooltipCallback<ItemStack>() {
			@Override
			public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip) {
				if(input) {
					WoodenCrucibleRegistryEntry entry = recipeWrapper.getEntryAt(slotIndex - INPUT_SLOTS);
					tooltip.add(recipeWrapper.getFluid().getLocalizedName(recipeWrapper.getFluidOutputs().get(0)));
					tooltip.add(" * " + entry.getAmount() + " mB");
				}
			}
		});
	}
}
