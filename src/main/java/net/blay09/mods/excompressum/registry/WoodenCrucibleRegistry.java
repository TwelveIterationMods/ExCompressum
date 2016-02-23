package net.blay09.mods.excompressum.registry;

import cpw.mods.fml.common.registry.GameRegistry;
import exnihilo.utils.ItemInfo;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Hashtable;
import java.util.List;

public class WoodenCrucibleRegistry {

    public static class WoodenMeltable {
        public final ItemStack itemStack;
        public final FluidStack fluidStack;
        public final Block appearance;
        public final int appearanceMeta;

        public WoodenMeltable(ItemStack itemStack, FluidStack fluidStack, Block appearance, int appearanceMeta) {
            this.itemStack = itemStack;
            this.fluidStack = fluidStack;
            this.appearance = appearance;
            this.appearanceMeta = appearanceMeta;
        }
    }

    private static Hashtable<ItemInfo, WoodenMeltable> entries = new Hashtable<ItemInfo, WoodenMeltable>();

    public static void register(ItemStack itemStack, FluidStack fluidStack, Block appearance, int appearanceMeta) {
        entries.put(new ItemInfo(itemStack), new WoodenMeltable(itemStack, fluidStack, appearance, appearanceMeta));
    }

    public static WoodenMeltable getMeltable(ItemStack itemStack) {
        WoodenMeltable meltable = entries.get(new ItemInfo(itemStack));
        if(meltable != null) {
            return meltable;
        }
        return entries.get(new ItemInfo(itemStack.getItem(), OreDictionary.WILDCARD_VALUE));
    }

    public static boolean isRegistered(ItemStack itemStack) {
        return entries.containsKey(new ItemInfo(itemStack));
    }

    public static void load(Configuration config) {
        String[] meltables = config.getStringList("Wooden Meltables", "registries", new String[] {
                "ore:treeSapling=100:water:minecraft:leaves:0",
                "ore:treeLeaves=250:water:minecraft:leaves:0",
                "minecraft:apple=50:water:minecraft:leaves:0",
                "minecraft:cactus=250:water:minecraft:cactus:0",
                "ore:listAllfruit=50:water:minecraft:leaves:0" // Pam's Harvestcraft Fruits
        }, "Here you can specify additional blocks and items that will melt into water in a wooden crucible. Format: modid:name:meta=amount:fluidName:appearanceModID:appareanceBlock:appearanceMeta, modid can be ore for OreDictionary");
        for(String meltable : meltables) {
            String[] s = meltable.split("=");
            if (s.length < 2) {
                ExCompressum.logger.error("Skipping wooden meltable " + meltable + " due to invalid format");
                continue;
            }
            String[] source = s[0].split(":");
            if (source[0].equals("ore") && source.length >= 2) {
                String oreName = source[1];
                List<ItemStack> ores = OreDictionary.getOres(oreName, false);
                if (!ores.isEmpty()) {
                    for (ItemStack ore : ores) {
                        if (ore.getItem() instanceof ItemBlock) {
                            loadMeltable(ore, s[1]);
                        } else {
                            ExCompressum.logger.error("Skipping wooden meltable " + meltable + " because the source block is not a block");
                        }
                    }
                } else {
                    ExCompressum.logger.error("Skipping wooden meltable " + meltable + " because no ore dictionary entries found");
                }
            } else {
                ItemStack sourceStack = GameRegistry.findItemStack(source[0], source[1], 1);
                if (sourceStack == null) {
                    ExCompressum.logger.error("Skipping wooden meltable " + meltable + " because the source block was not found");
                    continue;
                }
                sourceStack.setItemDamage(source.length > 2 ? Integer.parseInt(source[2]) : OreDictionary.WILDCARD_VALUE);
                loadMeltable(sourceStack, s[1]);
            }
        }
    }

    private static void loadMeltable(ItemStack source, String result) {
        String[] s = result.split(":");
        if(s.length < 5) {
            ExCompressum.logger.error("Skipping wooden meltable " + result + " due to invalid format");
            return;
        }
        Fluid fluid = FluidRegistry.getFluid(s[1]);
        if(fluid == null) {
            ExCompressum.logger.error("Skipping wooden meltable " + result + " due to fluid not found");
            return;
        }
        FluidStack fluidStack = new FluidStack(fluid, Integer.parseInt(s[0]));
        Block appearance = GameRegistry.findBlock(s[2], s[3]);
        if(appearance == null) {
            ExCompressum.logger.error("Skipping wooden meltable " + result + " due to appearance block not found");
            return;
        }
        register(source, fluidStack, appearance, Integer.parseInt(s[4]));
    }
}
