package net.blay09.mods.excompressum.compat;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import exnihilo.registries.helpers.Smashable;
import exnihilo.utils.ItemInfo;
import net.blay09.mods.excompressum.registry.CompressedHammerRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class RecipeHandlerCompressedHammer extends TemplateRecipeHandler {

    private static final int SLOTS_PER_PAGE = 9;

    public class CachedHammerRecipe extends CachedRecipe {

        private final List<PositionedStack> input = new ArrayList<PositionedStack>();
        private final List<PositionedStack> outputs = new ArrayList<PositionedStack>();
        private Point focus;

        public CachedHammerRecipe(List<ItemStack> variations, ItemStack baseStack, ItemStack focusStack) {
            PositionedStack itemStack = new PositionedStack(baseStack != null ? baseStack : variations, 74, 4);
            itemStack.setMaxSize(1);
            input.add(itemStack);

            int row = 0;
            int column = 0;
            for (ItemStack v : variations) {
                outputs.add(new PositionedStack(v, 3 + 18 * column, 37 + 18 * row));
                if (focusStack != null && NEIServerUtils.areStacksSameTypeCrafting(focusStack, v)) {
                    focus = new Point(2 + 18 * column, 36 + 18 * row);
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
    public String getRecipeName() {
        return "Compressed Hammer";
    }

    @Override
    public String getGuiTexture() {
        return "exnihilo:textures/hammerNEI.png";
    }

    private void addCached(List<ItemStack> variations, ItemStack base, ItemStack focus) {
        if (variations.size() > SLOTS_PER_PAGE) {
            List<List<ItemStack>> parts = new ArrayList<List<ItemStack>>();
            int size = variations.size();
            for (int i = 0; i < size; i += SLOTS_PER_PAGE) {
                parts.add(new ArrayList<ItemStack>(variations.subList(i, Math.min(size, i + SLOTS_PER_PAGE))));
            }
            for (List<ItemStack> part : parts) {
                arecipes.add(new CachedHammerRecipe(part, base, focus));
            }
        } else {
            arecipes.add(new CachedHammerRecipe(variations, base, focus));
        }
    }

    @Override
    public void drawBackground(int recipeIndex) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 166, 58);

        Point focus = ((CachedHammerRecipe) this.arecipes.get(recipeIndex)).focus;
        if (focus != null) {
            GuiDraw.drawTexturedModalRect(focus.x, focus.y, 166, 0, 18, 18);
        }
    }

    @Override
    public int recipiesPerPage() {
        return 2;
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(75, 22, 15, 13), "excompressum.hammer"));
    }

    @Override
    public void loadCraftingRecipes(String outputID, Object... results) {
        if (outputID.equals("excompressum.hammer")) {
            for (ItemInfo itemInfo : CompressedHammerRegistry.getRewards().keySet()) {
                ItemStack inputStack = itemInfo.getStack();
                ArrayList<ItemStack> resultStack = new ArrayList<ItemStack>();
                HashMap<ItemInfo, Integer> cache = new HashMap<ItemInfo, Integer>();
                for (Smashable s : CompressedHammerRegistry.getRewards(itemInfo)) {
                    ItemInfo currInfo = new ItemInfo(s.item, s.meta);
                    if (cache.containsKey(currInfo))
                        cache.put(currInfo, cache.get(currInfo) + 1);
                    else
                        cache.put(currInfo, 1);

                }
                for (ItemInfo outputInfos : cache.keySet()) {
                    ItemStack stack = outputInfos.getStack();
                    stack.stackSize = cache.get(outputInfos);
                    resultStack.add(stack);
                }
                addCached(resultStack, inputStack, null);
            }
        } else
            super.loadCraftingRecipes(outputID, results);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        HashSet<ItemInfo> completed = new HashSet<ItemInfo>();
        for (ItemInfo ii : CompressedHammerRegistry.getSources(result)) {
            if (!completed.contains(ii)) {
                HashMap<ItemInfo, Integer> stored = new HashMap<ItemInfo, Integer>();
                for (Smashable results : CompressedHammerRegistry.getRewards(Block.getBlockFromItem(ii.getItem()), ii.getMeta())) {
                    ItemInfo current = new ItemInfo(results.item, results.meta);
                    if (stored.containsKey(current))
                        stored.put(current, stored.get(current) + 1);
                    else
                        stored.put(current, 1);
                }
                ArrayList<ItemStack> resultVars = new ArrayList<ItemStack>();
                for (ItemInfo info : stored.keySet()) {
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
    public void loadUsageRecipes(ItemStack ingredient) {
        HashMap<ItemInfo, Integer> stored = new HashMap<ItemInfo, Integer>();

        if (Block.getBlockFromItem(ingredient.getItem()) == Blocks.air) {
            return;
        }

        if (!CompressedHammerRegistry.isRegistered(ingredient)) {
            return;
        }

        for (Smashable results : CompressedHammerRegistry.getRewards(Block.getBlockFromItem(ingredient.getItem()), ingredient.getItemDamage())) {
            ItemInfo current = new ItemInfo(results.item, results.meta);
            if (stored.containsKey(current)) {
                stored.put(current, stored.get(current) + 1);
            } else {
                stored.put(current, 1);
            }
        }
        ArrayList<ItemStack> resultVars = new ArrayList<ItemStack>();
        for (ItemInfo itemInfo : stored.keySet()) {
            ItemStack stack = itemInfo.getStack();
            stack.stackSize = stored.get(itemInfo);
            resultVars.add(stack);
        }
        addCached(resultVars, ingredient, ingredient);
    }

    @SuppressWarnings("unused")
    private void addCached(List<ItemStack> variations) {
        addCached(variations, null, null);
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe guiRecipe, ItemStack itemStack, List<String> tooltip, int recipe) {
        super.handleItemTooltip(guiRecipe, itemStack, tooltip, recipe);
        CachedHammerRecipe cachedRecipe = (CachedHammerRecipe) this.arecipes.get(recipe);
        Point mouse = GuiDraw.getMousePosition();
        Point offset = guiRecipe.getRecipePosition(recipe);
        Point relMouse = new Point(mouse.x - (guiRecipe.width - 176) / 2 - offset.x, mouse.y - (guiRecipe.height - 166) / 2 - offset.y);

        if (itemStack != null && (relMouse.y > 34 && relMouse.y < 55)) {
            tooltip.add("Drop Chance:");
            ItemStack sourceStack = cachedRecipe.input.get(0).item;
            Block inBlock = Block.getBlockFromItem(sourceStack.getItem());
            int meta = sourceStack.getItemDamage();
            for (Smashable smash : CompressedHammerRegistry.getRewards(inBlock, meta)) {
                if (NEIServerUtils.areStacksSameTypeCrafting(itemStack, new ItemStack(smash.item, 1, smash.meta))) {
                    int chance = (int) (100 * smash.chance);
                    int fortune = (int) (100 * smash.luckMultiplier);
                    if (fortune > 0)
                        tooltip.add("  * " + Integer.toString(chance) + "%"
                                + EnumChatFormatting.BLUE + " (+" + Integer.toString(fortune) + "% luck bonus)" + EnumChatFormatting.RESET);
                    else
                        tooltip.add("  * " + Integer.toString(chance) + "%");

                }

            }

        }
        return tooltip;
    }
}
