package net.blay09.mods.excompressum.forge.compat.jei;

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
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.api.IHammerRecipe;
import net.blay09.mods.excompressum.loot.LootTableUtils;
import net.blay09.mods.excompressum.registry.LootTableProvider;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.block.HeavySieveBlock;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRecipe;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRecipe;
import net.blay09.mods.excompressum.registry.hammer.HammerRecipe;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistry;
import net.blay09.mods.excompressum.registry.heavysieve.GeneratedHeavySieveRecipe;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRecipe;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@JeiPlugin
public class JEIAddon implements IModPlugin {

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        List<JeiHeavySieveRecipe> jeiHeavySieveRecipes = new ArrayList<>();

        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<HeavySieveRecipe> heavySieveRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.heavySieveRecipeType);
        for (HeavySieveRecipe recipe : heavySieveRecipes) {
            jeiHeavySieveRecipes.add(new JeiHeavySieveRecipe(recipe));
        }

        List<GeneratedHeavySieveRecipe> generatedHeavySieveRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.generatedHeavySieveRecipeType);
        for (GeneratedHeavySieveRecipe recipe : generatedHeavySieveRecipes) {
            loadGeneratedHeavySieveRecipe(false, recipe, jeiHeavySieveRecipes);
            loadGeneratedHeavySieveRecipe(true, recipe, jeiHeavySieveRecipes);
        }

        registry.addRecipes(HeavySieveRecipeCategory.TYPE, jeiHeavySieveRecipes);

        List<JeiCompressedHammerRecipe> jeiCompressedHammerRecipes = new ArrayList<>();
        List<CompressedHammerRecipe> compressedHammerRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.compressedHammerRecipeType);
        for (CompressedHammerRecipe recipe : compressedHammerRecipes) {
            jeiCompressedHammerRecipes.add(new JeiCompressedHammerRecipe(recipe));
        }
        registry.addRecipes(CompressedHammerRecipeCategory.TYPE, jeiCompressedHammerRecipes);

        List<JeiHammerRecipe> jeiHammerRecipes = new ArrayList<>();
        List<HammerRecipe> hammerRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.hammerRecipeType);
        for (HammerRecipe recipe : hammerRecipes) {
            jeiHammerRecipes.add(new JeiHammerRecipe(recipe));
        }
        for (IHammerRecipe recipe : ExNihilo.getInstance().getHammerRecipes()) {
            jeiHammerRecipes.add(new JeiHammerRecipe(recipe));
        }
        registry.addRecipes(HammerRecipeCategory.TYPE, jeiHammerRecipes);

        List<JeiChickenStickRecipe> jeiChickenStickRecipes = new ArrayList<>();
        List<ChickenStickRecipe> chickenStickRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.chickenStickRecipeType);
        for (ChickenStickRecipe recipe : chickenStickRecipes) {
            jeiChickenStickRecipes.add(new JeiChickenStickRecipe(recipe));
        }
        registry.addRecipes(ChickenStickRecipeCategory.TYPE, jeiChickenStickRecipes);

        ArrayListMultimap<ResourceLocation, WoodenCrucibleRecipe> fluidOutputMap = ArrayListMultimap.create();
        List<WoodenCrucibleRecipe> woodenCrucibleRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.woodenCrucibleRecipeType);
        for (WoodenCrucibleRecipe entry : woodenCrucibleRecipes) {
            fluidOutputMap.put(entry.getFluidId(), entry);
        }

        List<JeiWoodenCrucibleRecipe> jeiWoodenCrucibleRecipes = new ArrayList<>();
        for (ResourceLocation fluidName : fluidOutputMap.keySet()) {
            Fluid fluid = Balm.getRegistries().getFluid(fluidName);
            if (fluid == null) {
                continue;
            }

            List<WoodenCrucibleRecipe> recipes = fluidOutputMap.get(fluidName);
            List<Pair<WoodenCrucibleRecipe, ItemStack>> inputs = new ArrayList<>();
            for (WoodenCrucibleRecipe meltable : recipes) {
                for (ItemStack matchingStack : meltable.getInput().getItems()) {
                    inputs.add(Pair.of(meltable, matchingStack));
                }
            }

            inputs.sort(Comparator.comparingInt((Pair<WoodenCrucibleRecipe, ItemStack> pair) -> pair.getFirst().getAmount()).reversed());

            final int pageSize = 45;
            List<List<Pair<WoodenCrucibleRecipe, ItemStack>>> pages = Lists.partition(inputs, pageSize);
            for (List<Pair<WoodenCrucibleRecipe, ItemStack>> page : pages) {
                jeiWoodenCrucibleRecipes.add(new JeiWoodenCrucibleRecipe(fluid, page));
            }
        }

        registry.addRecipes(WoodenCrucibleRecipeCategory.TYPE, jeiWoodenCrucibleRecipes);

        registry.addRecipes(CraftChickenStickRecipeCategory.TYPE, Lists.newArrayList(new CraftChickenStickRecipe()));
    }

    private void loadGeneratedHeavySieveRecipe(boolean waterlogged, GeneratedHeavySieveRecipe generatedRecipe, List<JeiHeavySieveRecipe> outRecipes) {
        BlockState waterLoggedState = ModBlocks.heavySieves[0].defaultBlockState().setValue(HeavySieveBlock.WATERLOGGED, waterlogged);
        for (SieveMeshRegistryEntry mesh : SieveMeshRegistry.getEntries().values()) {
            int rolls = HeavySieveRegistry.getGeneratedRollCount(generatedRecipe);
            ItemLike source = Balm.getRegistries().getItem(generatedRecipe.getSource());
            LootTable lootTable = ExNihilo.getInstance().generateHeavySieveLootTable(waterLoggedState, source, rolls, mesh);
            if (!LootTableUtils.isLootTableEmpty(lootTable)) {
                HeavySieveRecipe recipe = new HeavySieveRecipe(generatedRecipe.getId(), generatedRecipe.getInput(), new LootTableProvider(lootTable), waterlogged, null, Sets.newHashSet(mesh.getMeshType()));
                outRecipes.add(new JeiHeavySieveRecipe(recipe));
            }
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        for (Block heavySieve : ModBlocks.heavySieves) {
            registry.addRecipeCatalyst(new ItemStack(heavySieve), HeavySieveRecipeCategory.TYPE);
        }
        for (Block woodenCrucible : ModBlocks.woodenCrucibles) {
            registry.addRecipeCatalyst(new ItemStack(woodenCrucible), WoodenCrucibleRecipeCategory.TYPE);
        }
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.autoCompressedHammer), CompressedHammerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(ModItems.compressedNetheriteHammer), CompressedHammerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(ModItems.compressedDiamondHammer), CompressedHammerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(ModItems.compressedGoldenHammer), CompressedHammerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(ModItems.compressedIronHammer), CompressedHammerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(ModItems.compressedStoneHammer), CompressedHammerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(ModItems.compressedWoodenHammer), CompressedHammerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(ModItems.chickenStick), ChickenStickRecipeCategory.TYPE);

        registry.addRecipeCatalyst(new ItemStack(ModBlocks.autoHammer), HammerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(ExNihilo.getInstance().getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_NETHERITE), HammerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(ExNihilo.getInstance().getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_DIAMOND), HammerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(ExNihilo.getInstance().getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_GOLD), HammerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(ExNihilo.getInstance().getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_IRON), HammerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(ExNihilo.getInstance().getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_STONE), HammerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(ExNihilo.getInstance().getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_WOODEN), HammerRecipeCategory.TYPE);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ExCompressum.MOD_ID, "jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(
                new HeavySieveRecipeCategory(registry.getJeiHelpers()),
                new HammerRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new CompressedHammerRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new WoodenCrucibleRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new CraftChickenStickRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new ChickenStickRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

}
