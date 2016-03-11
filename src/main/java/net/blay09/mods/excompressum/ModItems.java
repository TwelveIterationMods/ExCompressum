package net.blay09.mods.excompressum;

import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.excompressum.item.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

public class ModItems {

    public static ItemChickenStick chickenStick;
    public static ItemCompressedHammer compressedHammerWood;
    public static ItemCompressedHammer compressedHammerStone;
    public static ItemCompressedHammer compressedHammerIron;
    public static ItemCompressedHammer compressedHammerGold;
    public static ItemCompressedHammer compressedHammerDiamond;
    public static ItemDoubleCompressedDiamondHammer doubleCompressedDiamondHammer;
    public static ItemCompressedCrook compressedCrook;
    public static ItemHeavySilkMesh heavySilkMesh;
    public static ItemWoodChipping woodChipping;
    public static ItemUncompressedCoal uncompressedCoal;

    public static void init() {
        chickenStick = new ItemChickenStick();
        GameRegistry.registerItem(chickenStick, "chickenStick");
        compressedHammerWood = new ItemCompressedHammer(Item.ToolMaterial.WOOD, "wood");
        GameRegistry.registerItem(compressedHammerWood, "compressedHammerWood");
        compressedHammerStone = new ItemCompressedHammer(Item.ToolMaterial.STONE, "stone");
        GameRegistry.registerItem(compressedHammerStone, "compressedHammerStone");
        compressedHammerIron = new ItemCompressedHammer(Item.ToolMaterial.IRON, "iron");
        GameRegistry.registerItem(compressedHammerIron, "compressedHammerIron");
        compressedHammerGold = new ItemCompressedHammer(Item.ToolMaterial.GOLD, "gold");
        GameRegistry.registerItem(compressedHammerGold, "compressedHammerGold");
        compressedHammerDiamond = new ItemCompressedHammer(Item.ToolMaterial.EMERALD, "diamond");
        GameRegistry.registerItem(compressedHammerDiamond, "compressedHammerDiamond");
        doubleCompressedDiamondHammer = new ItemDoubleCompressedDiamondHammer();
        GameRegistry.registerItem(doubleCompressedDiamondHammer, "doubleCompressedDiamondHammer");
        compressedCrook = new ItemCompressedCrook();
        GameRegistry.registerItem(compressedCrook, "compressedCrook");
        heavySilkMesh = new ItemHeavySilkMesh();
        GameRegistry.registerItem(heavySilkMesh, "heavySilkMesh");
        woodChipping = new ItemWoodChipping();
        GameRegistry.registerItem(woodChipping, "woodChipping");
        OreDictionary.registerOre("dustWood", new ItemStack(woodChipping));
        uncompressedCoal = new ItemUncompressedCoal();
        GameRegistry.registerItem(uncompressedCoal, "uncompressedCoal");
    }

    public static void registerRecipes(Configuration config) {
        ItemHeavySilkMesh.registerRecipes(config);
        ItemCompressedHammer.registerRecipes(config);
        ItemCompressedCrook.registerRecipes(config);
        ItemWoodChipping.registerRecipes(config);
        ItemUncompressedCoal.registerRecipes(config);
    }
}
