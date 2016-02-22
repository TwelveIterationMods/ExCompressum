package net.blay09.mods.excompressum;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import exnihilo.registries.HammerRegistry;
import exnihilo.registries.helpers.Smashable;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.List;

@Mod(modid = ExCompressum.MOD_ID, name = "ExCompressum", dependencies = "required-after:exnihilo")
public class ExCompressum {

    public static final Logger logger = LogManager.getLogger();
    public static final String MOD_ID = "excompressum";

    @Mod.Instance
    public static ExCompressum instance;

    @SidedProxy(serverSide = "net.blay09.mods.excompressum.CommonProxy", clientSide = "net.blay09.mods.excompressum.ClientProxy")
    public static CommonProxy proxy;

    private Configuration config;
    public static float compressedCrookDurabilityMultiplier;
    public static float compressedCrookSpeedMultiplier;
    public static float chickenStickSoundChance;

    public static ItemChickenStick chickenStick;
    public static ItemCompressedHammer compressedHammerWood;
    public static ItemCompressedHammer compressedHammerStone;
    public static ItemCompressedHammer compressedHammerIron;
    public static ItemCompressedHammer compressedHammerGold;
    public static ItemCompressedHammer compressedHammerDiamond;
    public static ItemCompressedCrook compressedCrook;
    public static BlockCompressedDust compressedDust;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        chickenStickSoundChance = config.getFloat("Chicken Stick Sound Chance", "general", 0.15f, 0f, 1f, "The chance for the chicken stick to make sounds when breaking blocks. Set to 0 to disable.");
        compressedCrookDurabilityMultiplier = config.getFloat("Compressed Crook Durability Multiplier", "general", 2f, 0.1f, 10f, "The multiplier applied to the Compressed Crook's durability (based on the normal wooden crook)");
        compressedCrookSpeedMultiplier = config.getFloat("Compressed Crook Speed Multiplier", "general", 4f, 0.1f, 10f, "The multiplier applied to the Compressed Crook's speed (based on the normal wooden crook)");

        compressedDust = new BlockCompressedDust();
        GameRegistry.registerBlock(compressedDust, "compressed_dust");

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
        compressedCrook = new ItemCompressedCrook();
        GameRegistry.registerItem(compressedCrook, "compressedCrook");

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        boolean easyMode = config.getBoolean("Easy Mode", "general", false, "Set this to true to enable easy-mode, which disables the compressed hammers and makes compressed smashables work for normal hammers instead.");
        String[] smashables = config.getStringList("Smashables", "general", new String[]{
                "ExtraUtilities:cobblestone_compressed:0=9:minecraft:gravel:0:1:0",
                "ExtraUtilities:cobblestone_compressed:12=9:minecraft:sand:0:1:0",
                "ExtraUtilities:cobblestone_compressed:14=9:exnihilo:dust:0:1:0"
        }, "Here you can add additional smashables for the compressed hammers. Format: modid:name:meta=stackSize:modid:name:meta:chance:luckMultiplier");
        for (String smashable : smashables) {
            String[] s = smashable.split("=");
            if (s.length < 2) {
                logger.error("Skipping smashable " + smashable + " due to invalid format");
                continue;
            }
            String[] source = s[0].split(":");
            if (source[0].equals("ore") && source.length >= 2) {
                String oreName = source[1];
                List<ItemStack> ores = OreDictionary.getOres(oreName, false);
                if (!ores.isEmpty()) {
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
                if (sourceBlock == null) {
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

        if(config.getBoolean("Compressed Crook", "general", true, "If set to false, the recipe for the compressed crook will be disabled.")) {
            Item itemCrook = GameRegistry.findItem("exnihilo", "crook");
            if(itemCrook != null) {
                GameRegistry.addRecipe(new ItemStack(compressedCrook), "## ", " # ", " # ", '#', itemCrook);
            }
        }

        String[] validChickenStickBlocks = config.getStringList("Valid Chicken Stick Blocks", "general", new String[]{
                "minecraft:cobblestone",
                "minecraft:gravel",
                "minecraft:sand"
        }, "Here you can add additional blocks t he chicken stick will be able to break. Format: modid:name:meta");
        for (String blockString : validChickenStickBlocks) {
            String[] s = blockString.split(":");
            if (s.length < 2) {
                logger.error("Skipping chicken stick block " + blockString + " due to invalid format");
                continue;
            }
            Block block = GameRegistry.findBlock(s[0], s[1]);
            int meta = 0;
            if (s.length > 2) {
                meta = Integer.parseInt(s[2]);
            }
            ChickenStickRegistry.addValidBlock(block, meta);
        }

        if(config.getBoolean("Enable Compressed Dust", "general", true, "Set this to false to disable the recipe for the compressed dust.")) {
            GameRegistry.addRecipe(new ItemStack(compressedDust), "###", "###", "###", '#', GameRegistry.findBlock("exnihilo", "dust"));
            GameRegistry.addShapelessRecipe(new ItemStack(GameRegistry.findBlock("exnihilo", "dust"), 9), compressedDust);
        }

        config.save();
    }

    private void loadSmashable(Block sourceBlock, int sourceMeta, String reward, boolean easyMode) {
        String[] s = reward.split(":");
        if (s.length < 6) {
            logger.error("Skipping smashable " + reward + " due to invalid format");
            return;
        }
        ItemStack rewardStack = GameRegistry.findItemStack(s[1], s[2], Integer.parseInt(s[0]));
        rewardStack.setItemDamage(Integer.parseInt(s[3]));
        if (!easyMode) {
            CompressedHammerRegistry.register(sourceBlock, sourceMeta, rewardStack, Float.parseFloat(s[4]), Float.parseFloat(s[5]));
        } else {
            for (int i = 0; i < rewardStack.stackSize; i++) {
                HammerRegistry.register(sourceBlock, sourceMeta, rewardStack.getItem(), rewardStack.getItemDamage(), Float.parseFloat(s[4]), Float.parseFloat(s[5]));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onHarvestBlock(BlockEvent.HarvestDropsEvent event) {
        if (event.world.isRemote || event.harvester == null || event.isSilkTouching) {
            return;
        }
        ItemStack heldItem = event.harvester.getHeldItem();
        if (heldItem != null && heldItem.getItem() == chickenStick) {
            if(!ChickenStickRegistry.isValidBlock(event.block, event.blockMetadata)) {
                return;
            }
            Collection<Smashable> rewards = HammerRegistry.getRewards(event.block, event.blockMetadata);
            if (rewards == null || rewards.isEmpty()) {
                return;
            }
            event.drops.clear();
            event.dropChance = 1f;
            for(Smashable reward : rewards) {
                if (event.world.rand.nextFloat() <= reward.chance + (reward.luckMultiplier * event.fortuneLevel)) {
                    event.drops.add(new ItemStack(reward.item, 1, reward.meta));
                }
            }
            return;
        }
        Collection<Smashable> rewards = CompressedHammerRegistry.getRewards(event.block, event.blockMetadata);
        if (rewards == null || rewards.isEmpty()) {
            return;
        }
        if (isCompressedHammer(heldItem)) {
            event.drops.clear();
            event.dropChance = 1f;
            for (Smashable reward : rewards) {
                if (event.world.rand.nextFloat() <= reward.chance + (reward.luckMultiplier * event.fortuneLevel)) {
                    event.drops.add(new ItemStack(reward.item, 1, reward.meta));
                }
            }
        }
    }

    public boolean isCompressedHammer(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        if (itemStack.getItem() instanceof ICompressedHammer && ((ICompressedHammer) itemStack.getItem()).isCompressedHammer(itemStack)) {
            return true;
        }
        return itemStack.hasTagCompound() && itemStack.stackTagCompound.getBoolean("CompressedHammered");
    }
}