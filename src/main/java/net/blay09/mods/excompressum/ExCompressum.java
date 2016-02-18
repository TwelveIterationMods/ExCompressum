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
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.world.BlockEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;

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
        if (easyMode) {
            Block compressed = GameRegistry.findBlock("ExtraUtilities", "cobblestone_compressed");
            if (compressed != null) {
                for (int i = 0; i < 9; i++) {
                    HammerRegistry.register(compressed, 0, Item.getItemFromBlock(Blocks.gravel), 0, 1f, 0f);
                    HammerRegistry.register(compressed, 12, Item.getItemFromBlock(Blocks.sand), 0, 1f, 0f);
                    HammerRegistry.register(compressed, 14, Item.getItemFromBlock(ENBlocks.Dust), 0, 1f, 0f);
                }
            }
        } else {
            Block compressed = GameRegistry.findBlock("ExtraUtilities", "cobblestone_compressed");
            if (compressed != null) {
                CompressedHammerRegistry.register(compressed, 0, new ItemStack(Item.getItemFromBlock(Blocks.gravel), 9));
                CompressedHammerRegistry.register(compressed, 12, new ItemStack(Item.getItemFromBlock(Blocks.sand), 9));
                CompressedHammerRegistry.register(compressed, 14, new ItemStack(Item.getItemFromBlock(ENBlocks.Dust), 9));
            }
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