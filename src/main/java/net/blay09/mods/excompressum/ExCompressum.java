package net.blay09.mods.excompressum;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import exnihilo.ENBlocks;
import exnihilo.registries.HammerRegistry;
import exnihilo.registries.helpers.Smashable;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

@Mod(modid = ExCompressum.MOD_ID, name = "ExCompressum", dependencies = "required-after:exnihilo")
public class ExCompressum {

    public static final Logger logger = LogManager.getLogger();
    public static final String MOD_ID = "excompressum";

    @Mod.Instance
    public static ExCompressum instance;

    private Configuration config;

    public ItemCompressedHammer compressedHammerWood;
    public ItemCompressedHammer compressedHammerStone;
    public ItemCompressedHammer compressedHammerIron;
    public ItemCompressedHammer compressedHammerGold;
    public ItemCompressedHammer compressedHammerDiamond;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

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


        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        boolean easyMode = config.getBoolean("Easy Mode", "general", false, "Set this to true to enable easy-mode, which disables the compressed hammers and makes compressed smashables work for normal hammers instead.");
        String[] smashables = config.getStringList("Smashables", "general", new String[] {
                "ExtraUtilities:cobblestone_compressed:0=9:minecraft:gravel:0:1:0",
                "ExtraUtilities:cobblestone_compressed:12=9:minecraft:sand:0:1:0",
                "ExtraUtilities:cobblestone_compressed:14=9:exnihilo:dust:0:1:0"
        }, "Here you can add additional smashables for the compressed hammers. Format: modid:name:meta=stackSize:modid:name:meta:chance:luckMultiplier");
        for(String smashable : smashables) {
            String[] s = smashable.split("=");
            if(s.length < 2) {
                logger.error("Skipping smashable " + smashable + " due to invalid format");
                continue;
            }
            String[] source = s[0].split(":");
            if(source[0].equals("ore") && source.length >= 2) {
                String oreName = source[1];
                List<ItemStack> ores = OreDictionary.getOres(oreName, false);
                if(!ores.isEmpty()) {
                    for (ItemStack ore : ores) {
                        if (ore.getItem() instanceof ItemBlock) {
                            loadSmashable(((ItemBlock) ore.getItem()).field_150939_a, ore.getItem().getMetadata(ore.getItemDamage()), s[1], easyMode);
                        } else {
                            logger.error("Skipping smashable " + smashable + " because the source block is not a block");
                        }
                    }
                } else {
                    logger.error("Skipping smashable " + smashable + " because no ore dictionary entries found");
                }
            } else {
                Block sourceBlock;
                if (source.length == 1) {
                    sourceBlock = GameRegistry.findBlock("minecraft", source[0]);
                } else {
                    sourceBlock = GameRegistry.findBlock(source[0], source[1]);
                }
                if(sourceBlock == null) {
                    logger.error("Skipping smashable " + smashable + " because the source block was not found");
                }
                int sourceMeta = 0;
                if (source.length >= 3) {
                    if (source[2].equals("*")) {
                        sourceMeta = OreDictionary.WILDCARD_VALUE;
                    } else {
                        sourceMeta = Integer.parseInt(source[2]);
                    }
                }
                loadSmashable(sourceBlock, sourceMeta, s[1], easyMode);
            }
        }
        if (!easyMode) {
            if (config.getBoolean("Compressed Wooden Hammer", "general", true, "If set to false, the recipe for the compressed wooden hammer will be disabled.")) {
                Item itemHammerWood = GameRegistry.findItem("exnihilo", "hammer_wood");
                if (itemHammerWood != null) {
                    GameRegistry.addRecipe(new ItemStack(compressedHammerWood), "###", "###", "###", '#', itemHammerWood);
                }
            }

            if (config.getBoolean("Compressed Stone Hammer", "general", true, "If set to false, the recipe for the compressed stone hammer will be disabled.")) {
                Item itemHammerStone = GameRegistry.findItem("exnihilo", "hammer_stone");
                if (itemHammerStone != null) {
                    GameRegistry.addRecipe(new ItemStack(compressedHammerStone), "###", "###", "###", '#', itemHammerStone);
                }
            }

            if (config.getBoolean("Compressed Iron Hammer", "general", true, "If set to false, the recipe for the compressed iron hammer will be disabled.")) {
                Item itemHammerIron = GameRegistry.findItem("exnihilo", "hammer_iron");
                if (itemHammerIron != null) {
                    GameRegistry.addRecipe(new ItemStack(compressedHammerIron), "###", "###", "###", '#', itemHammerIron);
                }
            }

            if (config.getBoolean("Compressed Gold Hammer", "general", true, "If set to false, the recipe for the compressed gold hammer will be disabled.")) {
                Item itemHammerGold = GameRegistry.findItem("exnihilo", "hammer_gold");
                if (itemHammerGold != null) {
                    GameRegistry.addRecipe(new ItemStack(compressedHammerGold), "###", "###", "###", '#', itemHammerGold);
                }
            }

            if (config.getBoolean("Compressed Diamond Hammer", "general", true, "If set to false, the recipe for the compressed diamond hammer will be disabled.")) {
                Item itemHammerDiamond = GameRegistry.findItem("exnihilo", "hammer_diamond");
                if (itemHammerDiamond != null) {
                    GameRegistry.addRecipe(new ItemStack(compressedHammerDiamond), "###", "###", "###", '#', itemHammerDiamond);
                }
            }
        }

        config.save();
    }

    private void loadSmashable(Block sourceBlock, int sourceMeta, String reward, boolean easyMode) {
        String[] s = reward.split(":");
        if(s.length < 6) {
            logger.error("Skipping smashable " + reward + " due to invalid format");
            return;
        }
        ItemStack rewardStack = GameRegistry.findItemStack(s[1], s[2], Integer.parseInt(s[0]));
        rewardStack.setItemDamage(Integer.parseInt(s[3]));
        if(!easyMode) {
            CompressedHammerRegistry.register(sourceBlock, sourceMeta, rewardStack, Float.parseFloat(s[4]), Float.parseFloat(s[5]));
        } else {
            for(int i = 0; i < rewardStack.stackSize; i++) {
                HammerRegistry.register(sourceBlock, sourceMeta, rewardStack.getItem(), rewardStack.getItemDamage(), Float.parseFloat(s[4]), Float.parseFloat(s[5]));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onHarvestBlock(BlockEvent.HarvestDropsEvent event) {
        if (event.world.isRemote || event.harvester == null || event.isSilkTouching) {
            return;
        }
        Collection<Smashable> rewards = CompressedHammerRegistry.getRewards(event.block, event.blockMetadata);
        if (rewards == null || rewards.isEmpty()) {
            return;
        }
        ItemStack heldItem = event.harvester.getHeldItem();
        if (heldItem != null && heldItem.getItem() instanceof ICompressedHammer && ((ICompressedHammer) heldItem.getItem()).isCompressedHammer(heldItem)) {
            event.drops.clear();
            event.dropChance = 1f;
            for (Smashable reward : rewards) {
                if (event.world.rand.nextFloat() <= reward.chance + (reward.luckMultiplier * event.fortuneLevel)) {
                    event.drops.add(new ItemStack(reward.item, 1, reward.meta));
                }
            }
        }
    }

}