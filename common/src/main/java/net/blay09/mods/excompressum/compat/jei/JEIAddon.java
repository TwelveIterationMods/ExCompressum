package net.blay09.mods.excompressum.compat.jei;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.compat.recipeviewers.*;
import net.blay09.mods.excompressum.loot.LootTableUtils;
import net.blay09.mods.excompressum.block.HeavySieveBlock;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistry;
import net.blay09.mods.excompressum.registry.heavysieve.GeneratedHeavySieveRecipe;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRecipeImpl;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRecipe;
import net.blay09.mods.excompressum.tag.ModItemTags;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@JeiPlugin
public class JEIAddon implements IModPlugin {

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        List<ExpandedHeavySieveRecipe> expandedHeavySieveRecipes = new ArrayList<>();

        final var level = Minecraft.getInstance().level;
        final var recipeManager = level.getRecipeManager();

        final var heavySieveRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.heavySieveRecipeType);
        for (final var recipe : heavySieveRecipes) {
            expandedHeavySieveRecipes.add(new ExpandedHeavySieveRecipe(recipe));
        }
        for (final var recipe : ExNihilo.getInstance().getHeavySieveRecipes()) {
            expandedHeavySieveRecipes.add(new ExpandedHeavySieveRecipe(recipe));
        }

        final var generatedHeavySieveRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.generatedHeavySieveRecipeType);
        for (final var recipe : generatedHeavySieveRecipes) {
            loadGeneratedHeavySieveRecipe(level, false, recipe, expandedHeavySieveRecipes);
            loadGeneratedHeavySieveRecipe(level, true, recipe, expandedHeavySieveRecipes);
        }

        registry.addRecipes(HeavySieveJeiRecipeCategory.TYPE, expandedHeavySieveRecipes);

        final var expandedSieveRecipes = new ArrayList<ExpandedSieveRecipe>();
        for (final var recipe : ExNihilo.getInstance().getSieveRecipes()) {
            expandedSieveRecipes.add(new ExpandedSieveRecipe(recipe));
        }
        registry.addRecipes(SieveJeiRecipeCategory.TYPE, expandedSieveRecipes);

        List<ExpandedCompressedHammerRecipe> expandedCompressedHammerRecipes = new ArrayList<>();
        final var compressedHammerRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.compressedHammerRecipeType);
        for (final var recipe : compressedHammerRecipes) {
            expandedCompressedHammerRecipes.add(new ExpandedCompressedHammerRecipe(recipe));
        }
        for (final var recipe : ExNihilo.getInstance().getCompressedHammerRecipes()) {
            expandedCompressedHammerRecipes.add(new ExpandedCompressedHammerRecipe(recipe));
        }
        registry.addRecipes(CompressedHammerJeiRecipeCategory.TYPE, expandedCompressedHammerRecipes);

        List<ExpandedHammerRecipe> expandedHammerRecipes = new ArrayList<>();
        final var hammerRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.hammerRecipeType);
        for (final var recipe : hammerRecipes) {
            expandedHammerRecipes.add(new ExpandedHammerRecipe(recipe));
        }
        for (final var recipe : ExNihilo.getInstance().getHammerRecipes()) {
            expandedHammerRecipes.add(new ExpandedHammerRecipe(recipe));
        }
        registry.addRecipes(HammerJeiRecipeCategory.TYPE, expandedHammerRecipes);

        List<ExpandedChickenStickRecipe> expandedChickenStickRecipes = new ArrayList<>();
        final var chickenStickRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.chickenStickRecipeType);
        for (final var recipe : chickenStickRecipes) {
            expandedChickenStickRecipes.add(new ExpandedChickenStickRecipe(recipe));
        }
        registry.addRecipes(ChickenStickJeiRecipeCategory.TYPE, expandedChickenStickRecipes);

        ArrayListMultimap<ResourceLocation, WoodenCrucibleRecipe> fluidOutputMap = ArrayListMultimap.create();
        final var woodenCrucibleRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.woodenCrucibleRecipeType);
        for (final var entry : woodenCrucibleRecipes) {
            fluidOutputMap.put(entry.getFluidId(), entry);
        }

        List<ExpandedWoodenCrucibleRecipe> expandedWoodenCrucibleRecipes = new ArrayList<>();
        for (final var fluidName : fluidOutputMap.keySet()) {
            final var fluid = Balm.getRegistries().getFluid(fluidName);
            if (fluid == null) {
                continue;
            }

            final var recipes = fluidOutputMap.get(fluidName);
            List<Pair<WoodenCrucibleRecipe, ItemStack>> inputs = new ArrayList<>();
            for (final var meltable : recipes) {
                for (final var matchingStack : meltable.getInput().getItems()) {
                    inputs.add(Pair.of(meltable, matchingStack));
                }
            }

            inputs.sort(Comparator.comparingInt((Pair<WoodenCrucibleRecipe, ItemStack> pair) -> pair.getFirst().getAmount()).reversed());

            final int pageSize = 45;
            final var pages = Lists.partition(inputs, pageSize);
            for (final var page : pages) {
                expandedWoodenCrucibleRecipes.add(new ExpandedWoodenCrucibleRecipe(fluid, page));
            }
        }

        registry.addRecipes(WoodenCrucibleJeiRecipeCategory.TYPE, expandedWoodenCrucibleRecipes);

        registry.addRecipes(CraftChickenStickJeiRecipeCategory.TYPE, Lists.newArrayList(new CraftChickenStickRecipe()));
    }

    private void loadGeneratedHeavySieveRecipe(Level level, boolean waterlogged, GeneratedHeavySieveRecipe generatedRecipe, List<ExpandedHeavySieveRecipe> outRecipes) {
        final var waterLoggedState = ModBlocks.heavySieves[0].defaultBlockState().setValue(HeavySieveBlock.WATERLOGGED, waterlogged);
        for (final var mesh : SieveMeshRegistry.getEntries().values()) {
            final var rolls = HeavySieveRegistry.getGeneratedRollCount(generatedRecipe);
            final var source = Balm.getRegistries().getItem(generatedRecipe.getSource());
            final var lootTable = ExNihilo.getInstance().generateHeavySieveLootTable(level, waterLoggedState, source, rolls, mesh);
            if (!LootTableUtils.isLootTableEmpty(lootTable)) {
                final var recipe = new HeavySieveRecipeImpl(generatedRecipe.getRecipeId(),
                        generatedRecipe.getInput(),
                        lootTable,
                        waterlogged,
                        null,
                        Sets.newHashSet(mesh.getMeshType()));
                outRecipes.add(new ExpandedHeavySieveRecipe(recipe));
            }
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.autoSieve), SieveJeiRecipeCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.autoHeavySieve), HeavySieveJeiRecipeCategory.TYPE);
        for (final var heavySieve : ModBlocks.heavySieves) {
            registry.addRecipeCatalyst(new ItemStack(heavySieve), HeavySieveJeiRecipeCategory.TYPE);
        }
        for (final var woodenCrucible : ModBlocks.woodenCrucibles) {
            registry.addRecipeCatalyst(new ItemStack(woodenCrucible), WoodenCrucibleJeiRecipeCategory.TYPE);
        }
        registry.addRecipeCatalyst(new ItemStack(ModItems.chickenStick), ChickenStickJeiRecipeCategory.TYPE);

        registry.addRecipeCatalyst(new ItemStack(ModBlocks.autoCompressedHammer), CompressedHammerJeiRecipeCategory.TYPE);
        for (final var itemHolder : BuiltInRegistries.ITEM.getTagOrEmpty(ModItemTags.COMPRESSED_HAMMERS)) {
            registry.addRecipeCatalyst(new ItemStack(itemHolder.value()), CompressedHammerJeiRecipeCategory.TYPE);
        }

        registry.addRecipeCatalyst(new ItemStack(ModBlocks.autoHammer), HammerJeiRecipeCategory.TYPE);
        for (final var itemHolder : BuiltInRegistries.ITEM.getTagOrEmpty(ModItemTags.HAMMERS)) {
            registry.addRecipeCatalyst(new ItemStack(itemHolder.value()), HammerJeiRecipeCategory.TYPE);
        }
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ExCompressum.MOD_ID, "jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(
                new SieveJeiRecipeCategory(registry.getJeiHelpers()),
                new HeavySieveJeiRecipeCategory(registry.getJeiHelpers()),
                new HammerJeiRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new CompressedHammerJeiRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new WoodenCrucibleJeiRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new CraftChickenStickJeiRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new ChickenStickJeiRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

}
