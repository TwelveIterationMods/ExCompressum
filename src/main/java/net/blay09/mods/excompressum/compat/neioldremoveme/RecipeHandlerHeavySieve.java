//package net.blay09.mods.excompressum.compat.jei;
//
//import codechicken.lib.gui.GuiDraw;
//import codechicken.nei.NEIServerUtils;
//import codechicken.nei.PositionedStack;
//import codechicken.nei.recipe.GuiRecipe;
//import codechicken.nei.recipe.TemplateRecipeHandler;
//import com.google.common.collect.*;
//import exnihilo.registries.helpers.HeavySieveRegistryEntry;
//import net.blay09.mods.excompressum.registry.HeavySieveRegistryOld;
//import net.blay09.mods.excompressum.registry.data.ItemAndMetadata;
//import net.minecraft.block.Block;
//import net.minecraft.item.ItemStack;
//import org.lwjgl.opengl.GL11;
//
//import java.awt.*;
//import java.util.*;
//import java.util.List;
//
//public class RecipeHandlerHeavySieve extends TemplateRecipeHandler {
//
//    private static final int SLOTS_PER_PAGE = 45;
//
//    public class CachedHeavySieveRecipe extends CachedRecipe {
//
//        private final List<PositionedStack> input = Lists.newArrayList();
//        private final List<PositionedStack> outputs = Lists.newArrayList();
//        private Point highlightPoint;
//
//        public CachedHeavySieveRecipe(List<ItemStack> resultStacks, ItemStack inputStack, ItemStack highlightStack) {
//            PositionedStack positionedStack = new PositionedStack(inputStack != null ? inputStack : resultStacks, 74, 4);
//            positionedStack.setMaxSize(1);
//            input.add(positionedStack);
//
//            int row = 0;
//            int column = 0;
//            for (ItemStack resultStack : resultStacks) {
//                outputs.add(new PositionedStack(resultStack, 3 + 18 * column, 37 + 18 * row));
//
//                if (highlightStack != null && NEIServerUtils.areStacksSameTypeCrafting(highlightStack, resultStack)) {
//                    highlightPoint = new Point(2 + 18 * column, 36 + 18 * row);
//                }
//
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
//    }
//
//    @Override
//    public void drawBackground(int recipeIndex) {
//        GL11.glColor4f(1f, 1f, 1f, 1f);
//        GuiDraw.changeTexture(getGuiTexture());
//        GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 166, 130);
//
//        Point highlightPoint = ((CachedHeavySieveRecipe) arecipes.get(recipeIndex)).highlightPoint;
//        if (highlightPoint != null) {
//            GuiDraw.drawTexturedModalRect(highlightPoint.x, highlightPoint.y, 166, 0, 18, 18);
//        }
//    }
//
//    private final Multiset<String> condensedTooltip = HashMultiset.create();
//    @Override
//    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack itemStack, List<String> list, int recipeIdx) {
//        CachedHeavySieveRecipe recipe = (CachedHeavySieveRecipe) arecipes.get(recipeIdx);
//        ItemStack sourceStack = recipe.input.get(0).item;
//        if (itemStack != null && itemStack != sourceStack) {
//            list.add("Drop Chance:");
//            condensedTooltip.clear();
//            for (HeavySieveRegistryEntry result : HeavySieveRegistryOld.rollHammerRewards(Block.getBlockFromItem(sourceStack.getItem()), sourceStack.getItemDamage())) {
//                if (NEIServerUtils.areStacksSameTypeCrafting(itemStack, new ItemStack(result.item, 1, result.meta))) {
//                    condensedTooltip.add(Integer.toString((int) Math.round(100 * (1.0 / result.rarity))) + "%");
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
//        transferRects.add(new RecipeTransferRect(new Rectangle(75, 22, 15, 13), "excompressum.heavySieve"));
//    }
//
//    @Override
//    public int recipiesPerPage() {
//        return 1;
//    }
//
//    @Override
//    public String getGuiTexture() {
//        return "exnihilo:textures/sieveNEI.png";
//    }
//
//    @Override
//    public String getRecipeName() {
//        return "Heavy Sieve";
//    }
//
//    @Override
//    public void loadCraftingRecipes(String outputId, Object... wat) {
//        if (!outputId.equals("excompressum.heavySieve")) {
//            super.loadCraftingRecipes(outputId, wat);
//            return;
//        }
//        Multimap<ItemAndMetadata, HeavySieveRegistryEntry> siftables = HeavySieveRegistryOld.getSiftables();
//        for (ItemAndMetadata sourceInfo : siftables.keySet()) {
//            Collection<HeavySieveRegistryEntry> results = siftables.get(sourceInfo);
//            Multiset<ItemAndMetadata> countedSet = HashMultiset.create();
//            for(HeavySieveRegistryEntry result : results) {
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
//        Multimap<ItemAndMetadata, HeavySieveRegistryEntry> siftables = HeavySieveRegistryOld.getSiftables();
//        for (ItemAndMetadata sourceInfo : siftables.keySet()) {
//            Collection<HeavySieveRegistryEntry> results = siftables.get(sourceInfo);
//            Multiset<ItemAndMetadata> countedSet = HashMultiset.create();
//            for(HeavySieveRegistryEntry result : results) {
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
//        if (!HeavySieveRegistryOld.isRegistered(itemStack)) {
//            return;
//        }
//        for (HeavySieveRegistryEntry result : HeavySieveRegistryOld.rollHammerRewards(itemStack)) {
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
//                arecipes.add(new CachedHeavySieveRecipe(resultStacks.subList(i, Math.min(resultCount, i + SLOTS_PER_PAGE)), inputStack, highlightStack));
//            }
//        } else {
//            arecipes.add(new CachedHeavySieveRecipe(resultStacks, inputStack, highlightStack));
//        }
//    }
//}
