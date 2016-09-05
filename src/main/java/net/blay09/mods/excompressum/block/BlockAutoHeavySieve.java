package net.blay09.mods.excompressum.block;

import cofh.api.block.IDismantleable;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModAPIManager;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.excompressum.ModBlocks;
import net.blay09.mods.excompressum.tile.TileEntityAutoHeavySieve;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockAutoHeavySieve extends BlockAutoSieve implements IDismantleable {

    public BlockAutoHeavySieve() {
        setRegistryName("auto_heavy_sieve");
    }

    @Override
    public IIcon getIcon(int side, int metadata) {
        return BlockHeavySieve.meshIcon;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityAutoHeavySieve();
    }

    public static void registerRecipes(Configuration config) {
        if(ModAPIManager.INSTANCE.hasAPI("CoFHAPI")) {
            if (config.getBoolean("Auto Heavy Sieve", "blocks", true, "Set this to false to disable the recipe for the auto heavy sieve.")) {
                if(OreDictionary.getOres("blockSteel", false).isEmpty() || OreDictionary.getOres("ingotSteel", false).isEmpty()) {
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.autoHeavySieve), "BGB", "GSG", "IGI", 'B', "blockIron", 'S', new ItemStack(ModBlocks.heavySieve, 1, OreDictionary.WILDCARD_VALUE), 'G', "paneGlassColorless", 'I', "ingotSteel"));
                } else {
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.autoHeavySieve), "BGB", "GSG", "IGI", 'B', "blockSteel", 'S', new ItemStack(ModBlocks.heavySieve, 1, OreDictionary.WILDCARD_VALUE), 'G', "paneGlassColorless", 'I', "ingotSteel"));
                }
            }
        }
    }
}
