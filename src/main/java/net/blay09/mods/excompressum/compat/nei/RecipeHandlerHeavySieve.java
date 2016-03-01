package net.blay09.mods.excompressum.compat.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import exnihilo.registries.helpers.SiftingResult;
import exnihilo.utils.ItemInfo;
import net.blay09.mods.excompressum.registry.HeavySieveRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class RecipeHandlerHeavySieve extends TemplateRecipeHandler {

    private static final int SLOTS_PER_PAGE = 45;

    public class CachedSieveRecipe extends CachedRecipe {

        private List<PositionedStack> input = new ArrayList<PositionedStack>();
        private List<PositionedStack> outputs = new ArrayList<PositionedStack>();
        public Point focus;

        public CachedSieveRecipe(List<ItemStack> variations, ItemStack base, ItemStack focus)
        {
            PositionedStack pstack = new PositionedStack(base != null ? base : variations, 74, 4);
            pstack.setMaxSize(1);
            this.input.add(pstack);

            int row = 0;
            int col = 0;
            for (ItemStack v : variations)
            {
                this.outputs.add(new PositionedStack(v, 3 + 18 * col, 37 + 18 * row));

                if (focus != null && NEIServerUtils.areStacksSameTypeCrafting(focus, v)) {
                    this.focus = new Point(2 + 18 * col, 36 + 18 * row);
                }

                col++;
                if (col > 8) {
                    col = 0;
                    row++;
                }
            }
        }

        public CachedSieveRecipe(List<ItemStack> variations)
        {
            this(variations, null, null);
        }

        @Override
        public List<PositionedStack> getIngredients()
        {
            return this.getCycledIngredients(cycleticks / 20, this.input);
        }

        @Override
        public List<PositionedStack> getOtherStacks()
        {
            return this.outputs;
        }

        @Override
        public PositionedStack getResult()
        {
            return null;
        }

    }

    @Override
    public String getRecipeName()
    {
        return "Heavy Sieve";
    }

    @Override
    public String getGuiTexture()
    {
        return "exnihilo:textures/sieveNEI.png";
    }

    private void addCached(List<ItemStack> variations, ItemStack base, ItemStack focus)
    {
        if (variations.size() > SLOTS_PER_PAGE)
        {
            List<List<ItemStack>> parts = new ArrayList<List<ItemStack>>();
            int size = variations.size();
            for (int i = 0; i < size; i += SLOTS_PER_PAGE)
            {
                parts.add(new ArrayList<ItemStack>(variations.subList(i, Math.min(size, i + SLOTS_PER_PAGE))));
            }
            for (List<ItemStack> part : parts)
            {
                this.arecipes.add(new CachedSieveRecipe(part, base, focus));
            }
        }
        else
        {
            this.arecipes.add(new CachedSieveRecipe(variations, base, focus));
        }
    }

    @Override
    public void drawBackground(int recipeIndex) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 166, 130);

        Point focus = ((CachedSieveRecipe) this.arecipes.get(recipeIndex)).focus;
        if (focus != null)
        {
            GuiDraw.drawTexturedModalRect(focus.x, focus.y, 166, 0, 18, 18);
        }
    }

    @Override
    public int recipiesPerPage() {
        return 1;
    }

    @Override
    public void loadTransferRects() {
        this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(75, 22, 15, 13), "exnihilo.sieve", new Object[0]));
    }

    @Override
    public void loadCraftingRecipes(String outputID, Object... results)
    {
        if (outputID.equals("exnihilo.sieve"))
        {
            for (ItemInfo itemInfo : HeavySieveRegistry.getSiftables().keySet())
            {
                ItemStack inputStack = itemInfo.getStack();
                ArrayList<ItemStack> resultStack = new ArrayList<ItemStack>();
                for (SiftingResult s : HeavySieveRegistry.getSiftingOutput(itemInfo))
                {
                    resultStack.add(new ItemStack(s.item, 1, s.meta));
                }
                addCached(resultStack, inputStack, null);
            }
        }
        else
            super.loadCraftingRecipes(outputID, results);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result)
    {
        HashSet<ItemInfo> completed = new HashSet<ItemInfo>();
        for (ItemInfo ii : HeavySieveRegistry.getSources(result))
        {
            if (!completed.contains(ii))
            {
                HashMap<ItemInfo, Integer> stored = new HashMap<ItemInfo, Integer>();
                for (SiftingResult results : HeavySieveRegistry.getSiftingOutput(Block.getBlockFromItem(ii.getItem()), ii.getMeta()))
                {
                    ItemInfo current = new ItemInfo(results.item, results.meta);
                    if (stored.containsKey(current))
                        stored.put(current, stored.get(current)+1);
                    else
                        stored.put(current, 1);
                }
                ArrayList<ItemStack> resultVars = new ArrayList<ItemStack>();
                for (ItemInfo info : stored.keySet())
                {
                    ItemStack stack = info.getStack();
                    stack.stackSize = stored.get(info);
                    resultVars.add(stack);
                }
                addCached(resultVars, ii.getStack(), result);
                completed.add(ii);
            }
        }

    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient)
    {
        HashMap<ItemInfo, Integer> stored = new HashMap<ItemInfo, Integer>();

        if (Block.getBlockFromItem(ingredient.getItem()) == Blocks.air)
            return;

        if (!HeavySieveRegistry.isRegistered(Block.getBlockFromItem(ingredient.getItem()), ingredient.getItemDamage()))
        {
            return;
        }

        for (SiftingResult results : HeavySieveRegistry.getSiftingOutput(Block.getBlockFromItem(ingredient.getItem()), ingredient.getItemDamage()))
        {
            ItemInfo current = new ItemInfo(results.item, results.meta);
            if (stored.containsKey(current))
                stored.put(current, stored.get(current)+1);
            else
                stored.put(current, 1);
        }
        ArrayList<ItemStack> resultVars = new ArrayList<ItemStack>();
        for (ItemInfo info : stored.keySet())
        {
            ItemStack stack = info.getStack();
            stack.stackSize = stored.get(info);
            resultVars.add(stack);
        }
        addCached(resultVars, ingredient, ingredient);
        //completed.add(ii);
    }

    @SuppressWarnings("unused")
    private void addCached(List<ItemStack> variations)
    {
        addCached(variations, null, null);
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe guiRecipe, ItemStack itemStack, List<String> currenttip, int recipe) {
        super.handleItemTooltip(guiRecipe, itemStack, currenttip, recipe);
        CachedSieveRecipe crecipe = (CachedSieveRecipe) this.arecipes.get(recipe);
        Point mouse = GuiDraw.getMousePosition();
        Point offset = guiRecipe.getRecipePosition(recipe);
        Point relMouse = new Point(mouse.x - (guiRecipe.width - 176) / 2 - offset.x, mouse.y - (guiRecipe.height - 166) / 2 - offset.y);

        if (itemStack != null && relMouse.y > 34)
        {
            currenttip.add("Drop Chance:");
            ItemStack sourceStack = crecipe.input.get(0).item;
            Block inBlock = Block.getBlockFromItem(sourceStack.getItem());
            int meta = sourceStack.getItemDamage();
            for (SiftingResult smash : HeavySieveRegistry.getSiftingOutput(inBlock, meta))
            {
                if (NEIServerUtils.areStacksSameTypeCrafting(itemStack, new ItemStack(smash.item, 1, smash.meta)))
                {
                    int chance = (int) Math.round(100 * (1.0/smash.rarity));
                    currenttip.add("  * "+Integer.toString(chance)+"%");

                }

            }

        }
        return currenttip;
    }

}
