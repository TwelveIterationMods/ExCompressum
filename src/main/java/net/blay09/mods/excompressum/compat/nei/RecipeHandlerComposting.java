package net.blay09.mods.excompressum.compat.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import exnihilo.registries.CompostRegistry;
import exnihilo.registries.helpers.Compostable;
import exnihilo.registries.helpers.Smashable;
import exnihilo.utils.ItemInfo;
import net.blay09.mods.excompressum.registry.CompressedHammerRegistry;
import net.blay09.mods.excompressum.registry.data.ItemAndMetadata;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class RecipeHandlerComposting extends TemplateRecipeHandler {

    private static final int SLOTS_PER_PAGE = 45;

    public class CachedCompostRecipe extends CachedRecipe {

        private final List<PositionedStack> input = Lists.newArrayList();
        private final List<PositionedStack> outputs = Lists.newArrayList();
        private Point highlightPoint;

        public CachedCompostRecipe(List<ItemStack> resultStacks, ItemStack inputStack, ItemStack highlightStack) {
            PositionedStack positionedStack = new PositionedStack(inputStack != null ? inputStack : resultStacks, 74, 4);
            positionedStack.setMaxSize(1);
            input.add(positionedStack);

            int row = 0;
            int column = 0;
            for (ItemStack resultStack : resultStacks) {
                outputs.add(new PositionedStack(resultStack, 3 + 18 * column, 37 + 18 * row));
                if (highlightStack != null && NEIServerUtils.areStacksSameTypeCrafting(highlightStack, resultStack)) {
                    highlightPoint = new Point(2 + 18 * column, 36 + 18 * row);
                }
                column++;
                if (column > 8) {
                    column = 0;
                    row++;
                }
            }
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 20, input);
        }

        @Override
        public List<PositionedStack> getOtherStacks() {
            return outputs;
        }

        @Override
        public PositionedStack getResult() {
            return null;
        }

    }

    @Override
    public void drawBackground(int recipeIndex) {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 166, 130);

        Point highlightPoint = ((CachedCompostRecipe) arecipes.get(recipeIndex)).highlightPoint;
        if (highlightPoint != null) {
            GuiDraw.drawTexturedModalRect(highlightPoint.x, highlightPoint.y, 166, 0, 18, 18);
        }
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack itemStack, List<String> list, int recipeIdx) {
        CachedCompostRecipe recipe = (CachedCompostRecipe) arecipes.get(recipeIdx);
        ItemStack sourceStack = recipe.input.get(0).item;
        if (itemStack != null && itemStack != sourceStack) {
            Compostable compostable = CompostRegistry.getItem(itemStack.getItem(), itemStack.getItemDamage());
            if(compostable != null) {
                list.add("Amount: " + compostable.value);
            }
        }
        return list;
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(75, 22, 15, 13), "excompressum.composting"));
    }

    @Override
    public int recipiesPerPage() {
        return 1;
    }

    @Override
    public String getGuiTexture() {
        return "excompressum:textures/gui/neiComposting.png";
    }

    @Override
    public String getRecipeName() {
        return "Composting (Barrel)";
    }

    @Override
    public void loadCraftingRecipes(String outputID, Object... wat) {
        if (!outputID.equals("excompressum.neiComposting")) {
            super.loadCraftingRecipes(outputID, wat);
            return;
        }
        List<ItemStack> resultStacks = Lists.newArrayList();
        for(Map.Entry<ItemInfo, Compostable> entry : CompostRegistry.entries.entrySet()) {
            ItemStack itemStack = entry.getKey().getStack();
            itemStack.stackSize = (int) Math.ceil(1f / entry.getValue().value);
            resultStacks.add(itemStack);
        }
        addCached(resultStacks, new ItemStack(Blocks.dirt), null);
    }

    @Override
    public void loadCraftingRecipes(ItemStack itemStack) {
        if (itemStack.getItem() != Item.getItemFromBlock(Blocks.dirt)) {
            return;
        }
        List<ItemStack> resultStacks = Lists.newArrayList();
        for(Map.Entry<ItemInfo, Compostable> entry : CompostRegistry.entries.entrySet()) {
            ItemStack compostStack = entry.getKey().getStack();
            compostStack.stackSize = (int) Math.ceil(1f / entry.getValue().value);
            resultStacks.add(compostStack);
        }
        addCached(resultStacks, itemStack, null);
    }

    @Override
    public void loadUsageRecipes(ItemStack itemStack) {
        if (!CompostRegistry.containsItem(itemStack.getItem(), itemStack.getItemDamage())) {
            return;
        }
        List<ItemStack> resultStacks = Lists.newArrayList();
        for(Map.Entry<ItemInfo, Compostable> entry : CompostRegistry.entries.entrySet()) {
            ItemStack compostStack = entry.getKey().getStack();
            compostStack.stackSize = (int) Math.ceil(1f / entry.getValue().value);
            resultStacks.add(compostStack);
        }
        addCached(resultStacks, new ItemStack(Blocks.dirt), itemStack);
    }

    private void addCached(List<ItemStack> resultStacks, ItemStack inputStack, ItemStack highlightStack) {
        int resultCount = resultStacks.size();
        if (resultCount > SLOTS_PER_PAGE) {
            for(int i = 0; i < resultCount; i += SLOTS_PER_PAGE) {
                arecipes.add(new CachedCompostRecipe(resultStacks.subList(i, Math.min(resultCount, i + SLOTS_PER_PAGE)), inputStack, highlightStack));
            }
        } else {
            arecipes.add(new CachedCompostRecipe(resultStacks, inputStack, highlightStack));
        }
    }

}
