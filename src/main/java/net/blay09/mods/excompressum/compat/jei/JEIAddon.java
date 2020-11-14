package net.blay09.mods.excompressum.compat.jei;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.compressedhammer.CompressedHammerRegistryEntry;
import net.blay09.mods.excompressum.api.heavysieve.HeavySieveRegistryEntry;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.api.woodencrucible.WoodenCrucibleRegistryEntry;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerable;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistry;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRegistry;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@JeiPlugin
public class JEIAddon implements IModPlugin {

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        List<HeavySieveRecipe> heavySieveRecipes = new ArrayList<>();
        if (ExRegistro.doMeshesSplitLootTables()) {
            Collection<HeavySieveRegistryEntry> entries = HeavySieveRegistry.INSTANCE.getEntries().values();
            for (SieveMeshRegistryEntry sieveMesh : SieveMeshRegistry.getEntries().values()) {
                for (HeavySieveRegistryEntry entry : entries) {
                    if (!entry.getRewardsForMesh(sieveMesh, ExCompressumConfig.COMMON.flattenSieveRecipes.get()).isEmpty()) {
                        heavySieveRecipes.add(new HeavySieveRecipe(entry, sieveMesh));
                    }
                }
            }
        } else {
            for (HeavySieveRegistryEntry entry : HeavySieveRegistry.INSTANCE.getEntries().values()) {
                heavySieveRecipes.add(new HeavySieveRecipe(entry));
            }
        }
        registry.addRecipes(heavySieveRecipes, HeavySieveRecipeCategory.UID);

        List<CompressedHammerRecipe> compressedHammerRecipes = new ArrayList<>();
        for (CompressedHammerable entry : ExRegistries.getCompressedHammerRegistry().getEntries()) {
            compressedHammerRecipes.add(new CompressedHammerRecipe(entry));
        }
        registry.addRecipes(compressedHammerRecipes, CompressedHammerRecipeCategory.UID);

        ArrayListMultimap<ResourceLocation, WoodenCrucibleRegistryEntry> fluidOutputMap = ArrayListMultimap.create();
        for (WoodenCrucibleRegistryEntry entry : WoodenCrucibleRegistry.INSTANCE.getEntries().values()) {
            if (!entry.getItemStack().isEmpty()) {
                fluidOutputMap.put(entry.getFluid().getRegistryName(), entry);
            }
        }

        List<WoodenCrucibleRecipe> woodenCrucibleRecipes = new ArrayList<>();
        for (ResourceLocation fluidName : fluidOutputMap.keySet()) {
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidName);
            if (fluid == null) {
                continue;
            }

            List<WoodenCrucibleRegistryEntry> entries = fluidOutputMap.get(fluidName);

            final int pageSize = 45;
            List<List<WoodenCrucibleRegistryEntry>> pages = Lists.partition(entries, pageSize);
            for (List<WoodenCrucibleRegistryEntry> page : pages) {
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
