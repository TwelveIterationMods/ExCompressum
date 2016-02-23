package net.blay09.mods.excompressum.item;

import com.google.common.collect.Sets;
import cpw.mods.fml.common.registry.GameRegistry;
import exnihilo.items.hammers.ItemHammerBase;
import exnihilo.registries.HammerRegistry;
import net.blay09.mods.excompressum.registry.CompressedHammerRegistry;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.common.config.Configuration;

import java.util.Set;

public class ItemCompressedHammer extends ItemTool implements ICompressedHammer {

    public static Set blocksEffectiveAgainst = Sets.newHashSet(ItemHammerBase.blocksEffectiveAgainst);

    private final String name;

    public ItemCompressedHammer(ToolMaterial material, String name) {
        super(5f, material, blocksEffectiveAgainst);
        this.name = name;
        setUnlocalizedName(ExCompressum.MOD_ID + ":compressed_hammer_" + name);
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack itemStack) {
        return HammerRegistry.registered(new ItemStack(block));
    }

    @Override
    public float getDigSpeed(ItemStack item, Block block, int meta) {
        if ((CompressedHammerRegistry.isRegistered(block, meta) || HammerRegistry.registered(new ItemStack(block, 1, meta))) && block.getHarvestLevel(meta) <= toolMaterial.getHarvestLevel()) {
            return efficiencyOnProperMaterial * 0.75f;
        }
        return 0.8f;
    }

    @Override
    public boolean isHammer(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean isCompressedHammer(ItemStack itemStack) {
        return true;
    }

    @Override
    public void registerIcons(IIconRegister register) {
        itemIcon = register.registerIcon("excompressum:compressed_hammer_" + name);
    }

    public static void registerRecipes(Configuration config) {
        if (config.getBoolean("Compressed Wooden Hammer", "items", true, "If set to false, the recipe for the compressed wooden hammer will be disabled.")) {
            Item itemHammerWood = GameRegistry.findItem("exnihilo", "hammer_wood");
            if (itemHammerWood != null) {
                GameRegistry.addRecipe(new ItemStack(ExCompressum.compressedHammerWood), "###", "###", "###", '#', itemHammerWood);
            }
        }

        if (config.getBoolean("Compressed Stone Hammer", "items", true, "If set to false, the recipe for the compressed stone hammer will be disabled.")) {
            Item itemHammerStone = GameRegistry.findItem("exnihilo", "hammer_stone");
            if (itemHammerStone != null) {
                GameRegistry.addRecipe(new ItemStack(ExCompressum.compressedHammerStone), "###", "###", "###", '#', itemHammerStone);
            }
        }

        if (config.getBoolean("Compressed Iron Hammer", "items", true, "If set to false, the recipe for the compressed iron hammer will be disabled.")) {
            Item itemHammerIron = GameRegistry.findItem("exnihilo", "hammer_iron");
            if (itemHammerIron != null) {
                GameRegistry.addRecipe(new ItemStack(ExCompressum.compressedHammerIron), "###", "###", "###", '#', itemHammerIron);
            }
        }

        if (config.getBoolean("Compressed Gold Hammer", "items", true, "If set to false, the recipe for the compressed gold hammer will be disabled.")) {
            Item itemHammerGold = GameRegistry.findItem("exnihilo", "hammer_gold");
            if (itemHammerGold != null) {
                GameRegistry.addRecipe(new ItemStack(ExCompressum.compressedHammerGold), "###", "###", "###", '#', itemHammerGold);
            }
        }

        if (config.getBoolean("Compressed Diamond Hammer", "items", true, "If set to false, the recipe for the compressed diamond hammer will be disabled.")) {
            Item itemHammerDiamond = GameRegistry.findItem("exnihilo", "hammer_diamond");
            if (itemHammerDiamond != null) {
                GameRegistry.addRecipe(new ItemStack(ExCompressum.compressedHammerDiamond), "###", "###", "###", '#', itemHammerDiamond);
            }
        }
    }
}
