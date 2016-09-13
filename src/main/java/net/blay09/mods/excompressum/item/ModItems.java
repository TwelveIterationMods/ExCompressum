package net.blay09.mods.excompressum.item;

import com.google.common.collect.Lists;
import net.blay09.mods.excompressum.IRegisterModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class ModItems {

    private static final List<Item> modItems = Lists.newArrayList();
    public static ItemChickenStick chickenStick;
    public static ItemCompressedHammer compressedHammerWood;
    public static ItemCompressedHammer compressedHammerStone;
    public static ItemCompressedHammer compressedHammerIron;
    public static ItemCompressedHammer compressedHammerGold;
    public static ItemCompressedHammer compressedHammerDiamond;
    public static ItemDoubleCompressedDiamondHammer doubleCompressedDiamondHammer;
    public static ItemCompressedCrook compressedCrook;
    public static ItemIronMesh ironMesh;
    public static ItemWoodChipping woodChipping;
    public static ItemUncompressedCoal uncompressedCoal;
    public static ItemBatZapper batZapper;
    public static ItemOreSmasher oreSmasher;

    public static void init() {
        chickenStick = new ItemChickenStick();
        register(chickenStick);

        compressedHammerWood = new ItemCompressedHammer(Item.ToolMaterial.WOOD, "wood");
        register(compressedHammerWood);
        compressedHammerStone = new ItemCompressedHammer(Item.ToolMaterial.STONE, "stone");
        register(compressedHammerStone);
        compressedHammerIron = new ItemCompressedHammer(Item.ToolMaterial.IRON, "iron");
        register(compressedHammerIron);
        compressedHammerGold = new ItemCompressedHammer(Item.ToolMaterial.GOLD, "gold");
        register(compressedHammerGold);
        compressedHammerDiamond = new ItemCompressedHammer(Item.ToolMaterial.DIAMOND, "diamond");
        register(compressedHammerDiamond);

        doubleCompressedDiamondHammer = new ItemDoubleCompressedDiamondHammer();
        register(doubleCompressedDiamondHammer);

        compressedCrook = new ItemCompressedCrook();
        register(compressedCrook);

        ironMesh = new ItemIronMesh();
        register(ironMesh);

        woodChipping = new ItemWoodChipping();
        register(woodChipping);
        OreDictionary.registerOre("dustWood", new ItemStack(woodChipping));

        uncompressedCoal = new ItemUncompressedCoal();
        register(uncompressedCoal);

        batZapper = new ItemBatZapper();
        register(batZapper);

        oreSmasher = new ItemOreSmasher();
        register(oreSmasher);
    }

    public static void register(Item item) {
        GameRegistry.register(item);
        modItems.add(item);
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels() {
        for(Item item : modItems) {
            if (item instanceof IRegisterModel) {
                ((IRegisterModel) item).registerModel(item);
            } else {
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
            }
        }
    }

}
