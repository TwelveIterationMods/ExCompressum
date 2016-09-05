package net.blay09.mods.excompressum;

import net.blay09.mods.excompressum.item.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
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
    public static ItemBatZapper batZapper;
    public static ItemOreSmasher oreSmasher;

    public static void init() {
        chickenStick = new ItemChickenStick();
        GameRegistry.register(chickenStick);

        compressedHammerWood = new ItemCompressedHammer(Item.ToolMaterial.WOOD, "wood");
        GameRegistry.register(compressedHammerWood);
        compressedHammerStone = new ItemCompressedHammer(Item.ToolMaterial.STONE, "stone");
        GameRegistry.register(compressedHammerStone);
        compressedHammerIron = new ItemCompressedHammer(Item.ToolMaterial.IRON, "iron");
        GameRegistry.register(compressedHammerIron);
        compressedHammerGold = new ItemCompressedHammer(Item.ToolMaterial.GOLD, "gold");
        GameRegistry.register(compressedHammerGold);
        compressedHammerDiamond = new ItemCompressedHammer(Item.ToolMaterial.DIAMOND, "diamond");
        GameRegistry.register(compressedHammerDiamond);

        doubleCompressedDiamondHammer = new ItemDoubleCompressedDiamondHammer();
        GameRegistry.register(doubleCompressedDiamondHammer);

        compressedCrook = new ItemCompressedCrook();
        GameRegistry.register(compressedCrook);

        heavySilkMesh = new ItemHeavySilkMesh();
        GameRegistry.register(heavySilkMesh);

        woodChipping = new ItemWoodChipping();
        GameRegistry.register(woodChipping);
        OreDictionary.registerOre("dustWood", new ItemStack(woodChipping));

        uncompressedCoal = new ItemUncompressedCoal();
        GameRegistry.register(uncompressedCoal);

        batZapper = new ItemBatZapper();
        GameRegistry.register(batZapper);

        oreSmasher = new ItemOreSmasher();
        GameRegistry.register(oreSmasher);
    }

}
