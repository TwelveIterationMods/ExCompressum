package net.blay09.mods.excompressum.compat.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import exnihilo.data.ModData;
import exnihilo.registries.BarrelRecipeRegistry;
import exnihilo.registries.helpers.FluidItemCombo;
import exnihilo.utils.ItemInfo;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeHandlerBarrelProcess extends TemplateRecipeHandler {

    public class CachedBarrelProcess extends CachedRecipe {

        private final List<PositionedStack> input = Lists.newArrayList();
        private final PositionedStack output;

        public CachedBarrelProcess(ItemStack barrelFluid, ItemStack inputStack, ItemStack outputStack) {
            PositionedStack positionedStack = new PositionedStack(barrelFluid, 19, 9);
            positionedStack.setMaxSize(1);
            input.add(positionedStack);

            positionedStack = new PositionedStack(inputStack, 71, 21);
            positionedStack.setMaxSize(1);
            input.add(positionedStack);

            output = new PositionedStack(outputStack, 131, 21);
            output.setMaxSize(1);
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return input;
        }

        @Override
        public PositionedStack getResult() {
            return output;
        }

        @Override
        public List<PositionedStack> getOtherStacks() {
            return Collections.emptyList();
        }

    }

    @Override
    public void drawBackground(int recipeIndex) {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 166, 58);
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(100, 22, 16, 13), "excompressum.barrelProcess"));
    }

    @Override
    public int recipiesPerPage() {
        return 2;
    }

    @Override
    public String getGuiTexture() {
        return "excompressum:textures/gui/neiBarrelProcess.png";
    }

    @Override
    public String getRecipeName() {
        return "Barrel Recipe";
    }

    @Override
    public void loadCraftingRecipes(String outputID, Object... wat) {
        if (!outputID.equals("excompressum.barrelProcess")) {
            super.loadCraftingRecipes(outputID, wat);
            return;
        }

        if(ModData.ALLOW_BARREL_RECIPE_SLIME) {
            addCached(new ItemStack(Items.water_bucket), new ItemStack(Items.milk_bucket), new ItemStack(Items.slime_ball, 4));
        }
        Item witchWaterBucket = GameRegistry.findItem("exnihilo", "bucket_witchwater");
        if(ModData.ALLOW_BARREL_RECIPE_SOULSAND) {
            addCached(new ItemStack(Items.water_bucket), new ItemStack(Items.mushroom_stew), new ItemStack(witchWaterBucket, 1));
            addCached(new ItemStack(Items.water_bucket), GameRegistry.findItemStack("exnihilo", "spores", 1), new ItemStack(witchWaterBucket, 1));
        }
        try {
            Map<FluidItemCombo, ItemInfo> recipes = ReflectionHelper.getPrivateValue(BarrelRecipeRegistry.class, null, "recipes");
            for(Map.Entry<FluidItemCombo, ItemInfo> entry : recipes.entrySet()) {
                Fluid inputFluid = entry.getKey().getInputFluid();
                ItemStack fluidItem;
                if(inputFluid == FluidRegistry.WATER) {
                    fluidItem = new ItemStack(Items.water_bucket);
                } else if(inputFluid == FluidRegistry.LAVA) {
                    fluidItem = new ItemStack(Items.lava_bucket);
                } else if(inputFluid == FluidRegistry.getFluid("witchwater")) {
                    fluidItem = new ItemStack(witchWaterBucket);
                } else {
                    fluidItem = new ItemStack(entry.getKey().getInputFluid().getBlock());
                }
                ItemStack inputItem = entry.getKey().getInputItem().getStack();
                addCached(fluidItem, inputItem, entry.getValue().getStack());
            }
        } catch (ReflectionHelper.UnableToAccessFieldException ignored) {}
    }

    @Override
    public void loadCraftingRecipes(ItemStack itemStack) {
        if(ModData.ALLOW_BARREL_RECIPE_SLIME && itemStack.getItem() == Items.slime_ball) {
            addCached(new ItemStack(Items.water_bucket), new ItemStack(Items.milk_bucket), new ItemStack(Items.slime_ball, 4));
            return;
        }
        Item witchWaterBucket = GameRegistry.findItem("exnihilo", "bucket_witchwater");
        if(ModData.ALLOW_BARREL_RECIPE_SOULSAND && itemStack.getItem() == witchWaterBucket) {
            addCached(new ItemStack(Items.water_bucket), new ItemStack(Items.mushroom_stew), new ItemStack(witchWaterBucket, 1));
            addCached(new ItemStack(Items.water_bucket), GameRegistry.findItemStack("exnihilo", "spores", 1), new ItemStack(witchWaterBucket, 1));
            return;
        }
        try {
            Map<FluidItemCombo, ItemInfo> recipes = ReflectionHelper.getPrivateValue(BarrelRecipeRegistry.class, null, "recipes");
            for(Map.Entry<FluidItemCombo, ItemInfo> entry : recipes.entrySet()) {
                if(entry.getValue().getItem() != itemStack.getItem() || entry.getValue().getMeta() != itemStack.getItemDamage()) {
                    continue;
                }
                Fluid inputFluid = entry.getKey().getInputFluid();
                ItemStack fluidItem;
                if(inputFluid == FluidRegistry.WATER) {
                    fluidItem = new ItemStack(Items.water_bucket);
                } else if(inputFluid == FluidRegistry.LAVA) {
                    fluidItem = new ItemStack(Items.lava_bucket);
                } else if(inputFluid == FluidRegistry.getFluid("witchwater")) {
                    fluidItem = new ItemStack(witchWaterBucket);
                } else {
                    fluidItem = new ItemStack(entry.getKey().getInputFluid().getBlock());
                }
                ItemStack inputItem = entry.getKey().getInputItem().getStack();
                addCached(fluidItem, inputItem, entry.getValue().getStack());
            }
        } catch (ReflectionHelper.UnableToAccessFieldException ignored) {}
    }

    @Override
    public void loadUsageRecipes(ItemStack itemStack) {
        if(ModData.ALLOW_BARREL_RECIPE_SLIME && itemStack.getItem() == Items.milk_bucket) {
            addCached(new ItemStack(Items.water_bucket), itemStack, new ItemStack(Items.slime_ball, 4));
            return;
        }
        Item witchWaterBucket = GameRegistry.findItem("exnihilo", "bucket_witchwater");
        Item spores = GameRegistry.findItem("exnihilo", "spores");
        if(ModData.ALLOW_BARREL_RECIPE_SOULSAND && (itemStack.getItem() == Items.mushroom_stew || itemStack.getItem() == spores)) {
            addCached(new ItemStack(Items.water_bucket), itemStack, new ItemStack(witchWaterBucket, 1));
            return;
        }
        try {
            Map<FluidItemCombo, ItemInfo> recipes = ReflectionHelper.getPrivateValue(BarrelRecipeRegistry.class, null, "recipes");
            for(Map.Entry<FluidItemCombo, ItemInfo> entry : recipes.entrySet()) {
                if(entry.getKey().getInputItem().getItem() != itemStack.getItem() || entry.getKey().getInputItem().getMeta() != itemStack.getItemDamage()) {
                    continue;
                }
                Fluid inputFluid = entry.getKey().getInputFluid();
                ItemStack fluidItem;
                if(inputFluid == FluidRegistry.WATER) {
                    fluidItem = new ItemStack(Items.water_bucket);
                } else if(inputFluid == FluidRegistry.LAVA) {
                    fluidItem = new ItemStack(Items.lava_bucket);
                } else if(inputFluid == FluidRegistry.getFluid("witchwater")) {
                    fluidItem = new ItemStack(witchWaterBucket);
                } else {
                    fluidItem = new ItemStack(entry.getKey().getInputFluid().getBlock());
                }
                ItemStack inputItem = entry.getKey().getInputItem().getStack();
                addCached(fluidItem, inputItem, entry.getValue().getStack());
            }
        } catch (ReflectionHelper.UnableToAccessFieldException ignored) {}
    }

    private void addCached(ItemStack barrelFluid, ItemStack inputStack, ItemStack outputStack) {
        arecipes.add(new CachedBarrelProcess(barrelFluid, inputStack, outputStack));
    }

}
