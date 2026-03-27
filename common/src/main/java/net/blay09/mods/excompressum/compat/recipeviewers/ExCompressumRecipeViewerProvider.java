package net.blay09.mods.excompressum.compat.recipeviewers;

import net.blay09.mods.balm.platform.compatibility.recipeviewer.RecipeViewerInfoProvider;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.RecipeViewerRegistrar;
import net.blay09.mods.excompressum.block.HeavySieveType;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.block.WoodenCrucibleType;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.loot.LootTableUtils;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRecipe;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRecipe;
import net.blay09.mods.excompressum.tag.ModItemTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;

import static net.blay09.mods.excompressum.ExCompressum.id;

public class ExCompressumRecipeViewerProvider implements RecipeViewerInfoProvider {

    private static final Identifier HAMMER_TEXTURE = id("textures/gui/jei_hammer.png");
    private static final Identifier HEAVY_SIEVE_TEXTURE = id("textures/gui/jei_heavy_sieve.png");
    private static final Identifier WOODEN_CRUCIBLE_TEXTURE = id("textures/gui/jei_wooden_crucible.png");
    private static final Identifier CHICKEN_STICK_CRAFT_TEXTURE = id("textures/gui/jei_chicken_stick.png");

    @Override
    public void initialize(RecipeViewerRegistrar registrar) {
        registerSieveRecipes(registrar);
        registerHeavySieveRecipes(registrar);
        registerHammerRecipes(registrar);
        registerCompressedHammerRecipes(registrar);
        registerChickenStickRecipes(registrar);
        registerCraftChickenStickRecipe(registrar);
        registerWoodenCrucibleRecipes(registrar);
    }

    private static void registerSieveRecipes(RecipeViewerRegistrar registrar) {
        final var recipes = new ArrayList<ExpandedSieveRecipe>();
        for (final var recipe : ExNihilo.getInstance().getSieveRecipes()) {
            recipes.add(new ExpandedSieveRecipe(null, recipe));
        }

        registrar.registerCustomRecipeType(id("sieve"), ExpandedSieveRecipe.class)
                .withCraftingStation(ModBlocks.autoSieve)
                .withRecipes(recipes)
                .buildDisplay(display -> display
                        .title(Component.translatable("excompressum:sieve"))
                        .icon(new ItemStack(ModBlocks.autoSieve))
                        .size(166, 129)
                        .background(HEAVY_SIEVE_TEXTURE)
                        .slots((recipe, slots) -> {
                            slots.inputSlot(62, 10).add(recipe.getIngredient());
                            final var meshSlot = slots.inputSlot(88, 10);
                            for (final var meshItem : recipe.getMeshItems()) {
                                meshSlot.add(meshItem);
                            }
                            final var outputItems = recipe.getOutputItems();
                            for (int i = 0; i < outputItems.size(); i++) {
                                final int slotX = 3 + (i % 9 * 18);
                                final int slotY = 37 + (i / 9 * 18);
                                slots.outputSlot(slotX, slotY).add(outputItems.get(i));
                            }
                        })
                );
    }

    private static void registerHeavySieveRecipes(RecipeViewerRegistrar registrar) {
        final var registration = registrar.registerCustomRecipeType(id("heavy_sieve"), ExpandedHeavySieveRecipe.class)
                .withCraftingStation(ModBlocks.autoHeavySieve);
        for (final var heavySieve : ModBlocks.heavySieves.values()) {
            registration.withCraftingStation(heavySieve);
        }

        final var recipes = new ArrayList<ExpandedHeavySieveRecipe>();
        for (final var recipe : ExNihilo.getInstance().getHeavySieveRecipes()) {
            recipes.add(new ExpandedHeavySieveRecipe(null, recipe));
        }

        registration.withRecipes(recipes)
                .buildDisplay(display -> display
                        .title(Component.translatable("excompressum:heavy_sieve"))
                        .icon(new ItemStack(ModBlocks.heavySieves.get(HeavySieveType.OAK)))
                        .size(166, 129)
                        .background(HEAVY_SIEVE_TEXTURE)
                        .slots((recipe, slots) -> {
                            slots.inputSlot(62, 10).add(recipe.getIngredient());
                            final var meshSlot = slots.inputSlot(88, 10);
                            for (final var meshItem : recipe.getMeshItems()) {
                                meshSlot.add(meshItem);
                            }
                            final var outputItems = recipe.getOutputItems();
                            for (int i = 0; i < outputItems.size(); i++) {
                                final int slotX = 3 + (i % 9 * 18);
                                final int slotY = 37 + (i / 9 * 18);
                                slots.outputSlot(slotX, slotY).add(outputItems.get(i));
                            }
                        })
                );
    }

    private static void registerHammerRecipes(RecipeViewerRegistrar registrar) {
        final var registration = registrar.registerCustomRecipeType(id("hammer"), ExpandedHammerRecipe.class)
                .withCraftingStation(ModBlocks.autoHammer);

        for (final var itemHolder : BuiltInRegistries.ITEM.getTagOrEmpty(ModItemTags.HAMMERS)) {
            registration.withCraftingStation(new ItemStack(itemHolder.value()));
        }

        final var recipes = new ArrayList<ExpandedHammerRecipe>();
        for (final var recipe : ExNihilo.getInstance().getHammerRecipes()) {
            recipes.add(new ExpandedHammerRecipe(null, recipe));
        }

        registration.withRecipes(recipes)
                .buildDisplay(display -> display
                        .title(Component.translatable("excompressum:hammer"))
                        .icon(new ItemStack(ModBlocks.autoHammer))
                        .size(166, 63)
                        .background(HAMMER_TEXTURE)
                        .slots((recipe, slots) -> {
                            slots.inputSlot(75, 10).add(recipe.getIngredient());
                            final var outputItems = recipe.getOutputItems();
                            for (int i = 0; i < outputItems.size(); i++) {
                                slots.outputSlot(3 + i * 18, 37).add(outputItems.get(i));
                            }
                        })
                );
    }

    private static void registerCompressedHammerRecipes(RecipeViewerRegistrar registrar) {
        final var registration = registrar.registerCustomRecipeType(id("compressed_hammer"), ExpandedCompressedHammerRecipe.class)
                .withCraftingStation(ModBlocks.autoCompressedHammer);

        for (final var itemHolder : BuiltInRegistries.ITEM.getTagOrEmpty(ModItemTags.COMPRESSED_HAMMERS)) {
            registration.withCraftingStation(new ItemStack(itemHolder.value()));
        }

        final var recipes = new ArrayList<ExpandedCompressedHammerRecipe>();
        for (final var recipe : ExNihilo.getInstance().getCompressedHammerRecipes()) {
            recipes.add(new ExpandedCompressedHammerRecipe(null, recipe));
        }

        registration.withRecipes(recipes)
                .buildDisplay(display -> display
                        .title(Component.translatable("excompressum:compressed_hammer"))
                        .icon(new ItemStack(ModBlocks.autoCompressedHammer))
                        .size(166, 63)
                        .background(HAMMER_TEXTURE)
                        .slots((recipe, slots) -> {
                            slots.inputSlot(75, 10).add(recipe.getIngredient());
                            final var outputItems = recipe.getOutputItems();
                            for (int i = 0; i < outputItems.size(); i++) {
                                slots.outputSlot(3 + i * 18, 37).add(outputItems.get(i));
                            }
                        })
                );
    }

    private static void registerChickenStickRecipes(RecipeViewerRegistrar registrar) {
        registrar.registerRecipeType(id("chicken_stick"), ChickenStickRecipe.class)
                .withSyncedRecipes(ModRecipeTypes.chickenStick)
                .withCraftingStation(ModItems.chickenStick.createStack())
                .buildDisplay(display -> display
                        .title(Component.translatable("excompressum:chicken_stick"))
                        .icon(ModItems.chickenStick.createStack())
                        .size(166, 63)
                        .background(HAMMER_TEXTURE)
                        .slots((recipe, slots) -> {
                            slots.inputSlot(75, 10).add(recipe.getIngredient());

                            final var entries = LootTableUtils.getLootTableEntries(recipe.getLootTable());
                            final var mergedEntries = LootTableUtils.mergeLootTableEntries(entries);
                            for (int i = 0; i < mergedEntries.size(); i++) {
                                slots.outputSlot(3 + i * 18, 37).add(mergedEntries.get(i).getItemStack());
                            }
                        })
                );
    }

    private static void registerCraftChickenStickRecipe(RecipeViewerRegistrar registrar) {
        registrar.registerCustomRecipeType(id("craft_chicken_stick"), CraftChickenStickDummyRecipe.class)
                .withCraftingStation(ModItems.chickenStick.createStack())
                .withRecipe(new CraftChickenStickDummyRecipe())
                .buildDisplay(display -> display
                        .title(Component.translatable("item.excompressum.chicken_stick"))
                        .icon(ModItems.chickenStick.createStack())
                        .size(166, 58)
                        .background(CHICKEN_STICK_CRAFT_TEXTURE)
                        .slots((recipe, slots) -> {
                            slots.inputSlot(17, 21).add(recipe.getInput());
                            slots.outputSlot(131, 21).add(recipe.getOutput());
                        })
                );
    }

    private static void registerWoodenCrucibleRecipes(RecipeViewerRegistrar registrar) {
        final var registration = registrar.registerRecipeType(id("wooden_crucible"), WoodenCrucibleRecipe.class)
                .withSyncedRecipes(ModRecipeTypes.woodenCrucible);
        for (final var woodenCrucible : ModBlocks.woodenCrucibles.values()) {
            registration.withCraftingStation(woodenCrucible);
        }

        registration.buildDisplay(display -> display
                .title(Component.translatable("excompressum:wooden_crucible"))
                .icon(new ItemStack(ModBlocks.woodenCrucibles.get(WoodenCrucibleType.OAK)))
                .size(166, 129)
                .background(WOODEN_CRUCIBLE_TEXTURE)
                .slots((recipe, slots) -> {
                    final var fluid = recipe.getFluid();
                    if (fluid.getBucket() != Items.AIR) {
                        slots.outputSlot(75, 10).add(fluid.getBucket());
                    }

                    final var matchingItems = recipe.getIngredient().items().toList();
                    final int max = Math.min(matchingItems.size(), 45);
                    for (int i = 0; i < max; i++) {
                        final int slotX = 3 + (i % 9 * 18);
                        final int slotY = 37 + (i / 9 * 18);
                        slots.inputSlot(slotX, slotY).add(new ItemStack(matchingItems.get(i)));
                    }
                })
        );
    }
}
