package net.blay09.mods.excompressum.compat.jei;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.heavysieve.HeavySieveRegistryEntry;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerable;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySiftable;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleMeltable;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class JEIAddon implements IModPlugin {

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        List<HeavySieveRecipe> heavySieveRecipes = new ArrayList<>();
        for (HeavySiftable entry : ExRegistries.getHeavySieveRegistry().getEntries()) {
            heavySieveRecipes.add(new HeavySieveRecipe(entry));
        }
        for (HeavySiftable entry : ExRegistries.getHeavySieveRegistry().getEntries()) {
            heavySieveRecipes.add(new HeavySieveRecipe(entry));
        }

        registry.addRecipes(heavySieveRecipes, HeavySieveRecipeCategory.UID);

        List<CompressedHammerRecipe> compressedHammerRecipes = new ArrayList<>();
        for (CompressedHammerable entry : ExRegistries.getCompressedHammerRegistry().getEntries()) {
            compressedHammerRecipes.add(new CompressedHammerRecipe(entry));
        }
        registry.addRecipes(compressedHammerRecipes, CompressedHammerRecipeCategory.UID);

        ArrayListMultimap<ResourceLocation, WoodenCrucibleMeltable> fluidOutputMap = ArrayListMultimap.create();
        for (WoodenCrucibleMeltable entry : ExRegistries.getWoodenCrucibleRegistry().getEntries()) {
            fluidOutputMap.put(entry.getFluid(), entry);
        }

        List<WoodenCrucibleRecipe> woodenCrucibleRecipes = new ArrayList<>();
        for (ResourceLocation fluidName : fluidOutputMap.keySet()) {
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidName);
            if (fluid == null) {
                continue;
            }

            List<WoodenCrucibleMeltable> meltables = fluidOutputMap.get(fluidName);
            List<Pair<WoodenCrucibleMeltable, ItemStack>> inputs = new ArrayList<>();
            for (WoodenCrucibleMeltable meltable : meltables) {
                for (ItemStack matchingStack : meltable.getSource().getMatchingStacks()) {
                    inputs.add(Pair.of(meltable, matchingStack));
                }
            }

            final int pageSize = 45;
            List<List<Pair<WoodenCrucibleMeltable, ItemStack>>> pages = Lists.partition(inputs, pageSize);
            for (List<Pair<WoodenCrucibleMeltable, ItemStack>> page : pages) {
                woodenCrucibleRecipes.add(new WoodenCrucibleRecipe(fluid, page));
            }
        }

        registry.addRecipes(woodenCrucibleRecipes, WoodenCrucibleRecipeCategory.UID);

        registry.addRecipes(Lists.newArrayList(new ChickenStickRecipe()), ChickenStickRecipeCategory.UID);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        for (Block heavySieve : ModBlocks.heavySieves) {
            registry.addRecipeCatalyst(new ItemStack(heavySieve), HeavySieveRecipeCategory.UID);
        }
        for (Block woodenCrucible : ModBlocks.woodenCrucibles) {
            registry.addRecipeCatalyst(new ItemStack(woodenCrucible), WoodenCrucibleRecipeCategory.UID);
        }
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.autoCompressedHammer), CompressedHammerRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModItems.compressedHammerNetherite), CompressedHammerRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModItems.compressedHammerDiamond), CompressedHammerRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModItems.compressedHammerGold), CompressedHammerRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModItems.compressedHammerIron), CompressedHammerRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModItems.compressedHammerStone), CompressedHammerRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModItems.compressedHammerWood), CompressedHammerRecipeCategory.UID);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ExCompressum.MOD_ID, "jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(
                new HeavySieveRecipeCategory(registry.getJeiHelpers()),
                new CompressedHammerRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new WoodenCrucibleRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new ChickenStickRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

}
