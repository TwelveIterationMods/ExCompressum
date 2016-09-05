//package net.blay09.mods.excompressum.compat.jei;
//
//import codechicken.lib.gui.GuiDraw;
//import codechicken.nei.NEIServerUtils;
//import codechicken.nei.PositionedStack;
//import codechicken.nei.recipe.GuiRecipe;
//import codechicken.nei.recipe.TemplateRecipeHandler;
//import com.google.common.collect.HashMultiset;
//import com.google.common.collect.Lists;
//import com.google.common.collect.Multimap;
//import com.google.common.collect.Multiset;
//import exnihilo.registries.helpers.Smashable;
//import net.blay09.mods.excompressum.registry.CompressedHammerRegistry;
//import net.blay09.mods.excompressum.registry.data.ItemAndMetadata;
//import net.minecraft.block.Block;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.EnumChatFormatting;
//import org.lwjgl.opengl.GL11;
//
//import java.awt.*;
//import java.util.*;
//import java.util.List;
//
//public class RecipeHandlerCompressedHammer extends TemplateRecipeHandler {
//
//    private static final int SLOTS_PER_PAGE = 9;
//
//    public class CachedHammerRecipe extends CachedRecipe {
//
//        private final List<PositionedStack> input = Lists.newArrayList();
//        private final List<PositionedStack> outputs = Lists.newArrayList();
//        private Point highlightPoint;
//
//        public CachedHammerRecipe(List<ItemStack> resultStacks, ItemStack inputStack, ItemStack highlightStack) {
//            PositionedStack positionedStack = new PositionedStack(inputStack != null ? inputStack : resultStacks, 74, 4);
//            positionedStack.setMaxSize(1);
//            input.add(positionedStack);
//
//            int row = 0;
//            int column = 0;
//            for (ItemStack resultStack : resultStacks) {
//                outputs.add(new PositionedStack(resultStack, 3 + 18 * column, 37 + 18 * row));
//                if (highlightStack != null && NEIServerUtils.areStacksSameTypeCrafting(highlightStack, resultStack)) {
//                    highlightPoint = new Point(2 + 18 * column, 36 + 18 * row);
//                }
//                column++;
//                if (column > 8) {
//                    column = 0;
//                    row++;
//                }
//            }
//        }
//
//        @Override
//        public List<PositionedStack> getIngredients() {
//            return getCycledIngredients(cycleticks / 20, input);
//        }
//
//        @Override
//        public List<PositionedStack> getOtherStacks() {
//            return outputs;
//        }
//
//        @Override
//        public PositionedStack getResult() {
//            return null;
//        }
//
//    }
//
//    @Override
//    public void drawBackground(int recipeIndex) {
//        GL11.glColor4f(1f, 1f, 1f, 1f);
//        GuiDraw.changeTexture(getGuiTexture());
//        GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 166, 58);
//
//        Point highlightPoint = ((CachedHammerRecipe) arecipes.get(recipeIndex)).highlightPoint;
//        if (highlightPoint != null) {
//            GuiDraw.drawTexturedModalRect(highlightPoint.x, highlightPoint.y, 166, 0, 18, 18);
//        }
//    }
//
//    private final Multiset<String> condensedTooltip = HashMultiset.create();
//    @Override
//    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack itemStack, List<String> list, int recipeIdx) {
//        CachedHammerRecipe recipe = (CachedHammerRecipe) arecipes.get(recipeIdx);
//        ItemStack sourceStack = recipe.input.get(0).item;
//        if (itemStack != null && itemStack != sourceStack) {
//            list.add("Drop Chance:");
//            condensedTooltip.clear();
//            for (Smashable smashable : CompressedHammerRegistry.getSmashables(Block.getBlockFromItem(sourceStack.getItem()), sourceStack.getItemDamage())) {
//                if (NEIServerUtils.areStacksSameTypeCrafting(itemStack, new ItemStack(smashable.item, 1, smashable.meta))) {
//                    int chance = (int) (100 * smashable.chance);
//                    int fortune = (int) (100 * smashable.luckMultiplier);
//                    if (fortune > 0) {
//                        condensedTooltip.add(Integer.toString(chance) + "%" + EnumChatFormatting.BLUE + " (+" + Integer.toString(fortune) + "% luck bonus)" + EnumChatFormatting.RESET);
//                    } else {
//                        condensedTooltip.add(Integer.toString(chance) + "%");
//                    }
//                }
//            }
//            for(String line : condensedTooltip.elementSet()) {
//                list.add("  * " + condensedTooltip.count(line) + "x " + line);
//            }
//        }
//        return list;
//    }
//
//    @Override
//    public void loadTransferRects() {
//        transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(75, 22, 15, 13), "excompressum.compressedHammer"));
//    }
//
//    @Override
//    public int recipiesPerPage() {
//        return 2;
//    }
//
//    @Override
//    public String getGuiTexture() {
//        return "exnihilo:textures/hammerNEI.png";
//    }
//
//    @Override
//    public String getRecipeName() {
//        return "Compressed Hammer";
//    }
//
//    @Override
//    public void loadCraftingRecipes(String outputID, Object... wat) {
//        if (!outputID.equals("excompressum.hammer")) {
//            super.loadCraftingRecipes(outputID, wat);
//            return;
//        }
//        Multimap<ItemAndMetadata, Smashable> smashables = CompressedHammerRegistry.getSmashables();
//        for(ItemAndMetadata sourceInfo : smashables.keySet()) {
//            Collection<Smashable> results = smashables.get(sourceInfo);
//            Multiset<ItemAndMetadata> countedSet = HashMultiset.create();
//            for(Smashable result : results) {
//                countedSet.add(new ItemAndMetadata(result.item, result.meta));
//            }
//            List<ItemStack> resultStacks = Lists.newArrayList();
//            for(ItemAndMetadata resultInfo : countedSet.elementSet()) {
//                resultStacks.add(resultInfo.createStack(countedSet.count(resultInfo)));
//            }
//            addCached(resultStacks, sourceInfo.createStack(1), null);
//        }
//    }
//
//    @Override
//    public void loadCraftingRecipes(ItemStack itemStack) {
//        Multimap<ItemAndMetadata, Smashable> smashables = CompressedHammerRegistry.getSmashables();
//        for (ItemAndMetadata sourceInfo : smashables.keySet()) {
//            Collection<Smashable> results = smashables.get(sourceInfo);
//            Multiset<ItemAndMetadata> countedSet = HashMultiset.create();
//            for(Smashable result : results) {
//                countedSet.add(new ItemAndMetadata(result.item, result.meta));
//            }
//            if (!countedSet.contains(new ItemAndMetadata(itemStack))) {
//                continue;
//            }
//            List<ItemStack> resultStacks = Lists.newArrayList();
//            for(ItemAndMetadata resultInfo : countedSet.elementSet()) {
//                resultStacks.add(resultInfo.createStack(countedSet.count(resultInfo)));
//            }
//            addCached(resultStacks, sourceInfo.createStack(1), itemStack);
//        }
//    }
//
//    @Override
//    public void loadUsageRecipes(ItemStack itemStack) {
//        Multiset<ItemAndMetadata> countedSet = HashMultiset.create();
//        if (!CompressedHammerRegistry.isRegistered(itemStack)) {
//            return;
//        }
//        for (Smashable result : CompressedHammerRegistry.getSmashables(itemStack)) {
//            countedSet.add(new ItemAndMetadata(result.item, result.meta));
//        }
//        List<ItemStack> resultStacks = Lists.newArrayList();
//        for (ItemAndMetadata itemInfo : countedSet.elementSet()) {
//            resultStacks.add(itemInfo.createStack(countedSet.count(itemInfo)));
//        }
//        addCached(resultStacks, itemStack, itemStack);
//    }
//
//    private void addCached(List<ItemStack> resultStacks, ItemStack inputStack, ItemStack highlightStack) {
//        int resultCount = resultStacks.size();
//        if (resultCount > SLOTS_PER_PAGE) {
//            for(int i = 0; i < resultCount; i += SLOTS_PER_PAGE) {
//                arecipes.add(new CachedHammerRecipe(resultStacks.subList(i, Math.min(resultCount, i + SLOTS_PER_PAGE)), inputStack, highlightStack));
//            }
//        } else {
//            arecipes.add(new CachedHammerRecipe(resultStacks, inputStack, highlightStack));
//        }
//    }
//
//}
