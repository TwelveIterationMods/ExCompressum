package net.blay09.mods.excompressum;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import exnihilo.ENBlocks;
import exnihilo.ENItems;
import exnihilo.registries.HammerRegistry;
import exnihilo.registries.helpers.Smashable;
import net.blay09.mods.excompressum.block.BlockCompressedDust;
import net.blay09.mods.excompressum.block.BlockHeavySieve;
import net.blay09.mods.excompressum.item.*;
import net.blay09.mods.excompressum.registry.ChickenStickRegistry;
import net.blay09.mods.excompressum.registry.CompressedHammerRegistry;
import net.blay09.mods.excompressum.registry.HeavySieveRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
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

    @SidedProxy(serverSide = "net.blay09.mods.excompressum.CommonProxy", clientSide = "net.blay09.mods.excompressum.client.ClientProxy")
    public static CommonProxy proxy;

    private Configuration config;
    public static float compressedCrookDurabilityMultiplier;
    public static float compressedCrookSpeedMultiplier;
    public static float chickenStickSoundChance;
    public static boolean allowHeavySieveAutomation;

    public static ItemChickenStick chickenStick;
    public static ItemCompressedHammer compressedHammerWood;
    public static ItemCompressedHammer compressedHammerStone;
    public static ItemCompressedHammer compressedHammerIron;
    public static ItemCompressedHammer compressedHammerGold;
    public static ItemCompressedHammer compressedHammerDiamond;
    public static ItemCompressedCrook compressedCrook;
    public static ItemHeavySilkMesh heavySilkMesh;

    public static BlockCompressedDust compressedDust;
    public static BlockHeavySieve heavySieve;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        chickenStickSoundChance = config.getFloat("Chicken Stick Sound Chance", "general", 0.15f, 0f, 1f, "The chance for the chicken stick to make sounds when breaking blocks. Set to 0 to disable.");
        compressedCrookDurabilityMultiplier = config.getFloat("Compressed Crook Durability Multiplier", "general", 2f, 0.1f, 10f, "The multiplier applied to the Compressed Crook's durability (based on the normal wooden crook)");
        compressedCrookSpeedMultiplier = config.getFloat("Compressed Crook Speed Multiplier", "general", 4f, 0.1f, 10f, "The multiplier applied to the Compressed Crook's speed (based on the normal wooden crook)");
        allowHeavySieveAutomation = config.getBoolean("Allow Heavy Sieve Automation", "general", false, "Set this to true if you want to allow automation of the heavy sieve through fake players (i.e. Autonomous Activator)");

        compressedDust = new BlockCompressedDust();
        GameRegistry.registerBlock(compressedDust, "compressed_dust");
        heavySieve = new BlockHeavySieve();
        GameRegistry.registerBlock(heavySieve, ItemBlockHeavySieve.class, "heavySieve");

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
        heavySilkMesh = new ItemHeavySilkMesh();
        GameRegistry.registerItem(heavySilkMesh, "heavySilkMesh");

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        boolean easyMode = config.getBoolean("Easy Mode", "general", false, "Set this to true to enable easy-mode, which disables the compressed hammers and makes compressed smashables work for normal hammers instead.");
        CompressedHammerRegistry.load(config, easyMode);
        ChickenStickRegistry.load(config);
        HeavySieveRegistry.load(config);

        if (!easyMode) {
            ItemCompressedHammer.registerRecipes(config);
        }
        ItemCompressedCrook.registerRecipes(config);
        BlockHeavySieve.registerRecipes(config);

        if (config.getBoolean("Enable Compressed Dust", "general", true, "Set this to false to disable the recipe for the compressed dust.")) {
            GameRegistry.addRecipe(new ItemStack(compressedDust), "###", "###", "###", '#', GameRegistry.findBlock("exnihilo", "dust"));
            GameRegistry.addShapelessRecipe(new ItemStack(GameRegistry.findBlock("exnihilo", "dust"), 9), compressedDust);
        }

        config.save();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onHarvestBlock(BlockEvent.HarvestDropsEvent event) {
        if (event.world.isRemote || event.harvester == null || event.isSilkTouching) {
            return;
        }
        ItemStack heldItem = event.harvester.getHeldItem();
        if (heldItem != null && heldItem.getItem() == chickenStick) {
            if (!ChickenStickRegistry.isValidBlock(event.block, event.blockMetadata)) {
                return;
            }
            Collection<Smashable> rewards = HammerRegistry.getRewards(event.block, event.blockMetadata);
            if (rewards == null || rewards.isEmpty()) {
                return;
            }
            event.drops.clear();
            event.dropChance = 1f;
            for (Smashable reward : rewards) {
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