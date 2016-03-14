package net.blay09.mods.excompressum.block;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ModBlocks;
import net.blay09.mods.excompressum.tile.TileEntityAutoSieveMana;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockAutoSieveMana extends BlockAutoSieve {

    public BlockAutoSieveMana() {
        super(Material.iron);
        setBlockName(ExCompressum.MOD_ID + ":auto_sieve_mana");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityAutoSieveMana();
    }

    public static void registerRecipes(Configuration config) {
        if (Loader.isModLoaded("Botania")) {
            if (config.getBoolean("Mana Sieve", "blocks", true, "Set this to false to disable the recipe for the mana sieve.")) {
                ItemStack sieve = new ItemStack(GameRegistry.findBlock("exnihilo", "sifting_table"), 1, OreDictionary.WILDCARD_VALUE);
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.autoSieve), "BGB", "GSG", "IGI", 'B', "blockIron", 'S', sieve, 'G', "paneGlassColorless", 'I', "ingotIron"));
            }
        }
    }
}
