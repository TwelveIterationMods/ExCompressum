package net.blay09.mods.excompressum.registry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.item.ItemChickenStick;
import net.blay09.mods.excompressum.registry.data.ItemAndMetadata;
import net.minecraft.block.Block;
import net.minecraftforge.common.config.Configuration;

import java.util.List;
import java.util.Map;

public class ChickenStickRegistry {

    public static float chickenStickSoundChance;
    public static float chickenStickSpawnChance;
    public static boolean chickenOut;
    public static String[] chickenStickSounds;
    public static final Map<String, String> chickenStickNames = Maps.newHashMap();
    private static final List<ItemAndMetadata> validBlocks = Lists.newArrayList();
    private static String chickenStickName;

    private static void addValidBlock(Block block, int metadata) {
        validBlocks.add(new ItemAndMetadata(block, metadata));
        ItemChickenStick.blocksEffectiveAgainst.add(block);
    }

    public static boolean isValidBlock(Block block, int metadata) {
        return validBlocks.contains(new ItemAndMetadata(block, metadata));
    }

    public static void load(Configuration config) {
        chickenStickSpawnChance = config.getFloat("Chicken Stick Spawn Chance", "general", 0.008f, 0f, 1f, "The chance for the chicken stick to spawn a chicken. Set to 0 to disable.");
        chickenStickSoundChance = config.getFloat("Chicken Stick Sound Chance", "general", 0.2f, 0f, 1f, "The chance for the chicken stick to make sounds when breaking blocks. Set to 0 to disable.");
        chickenStickSounds = config.getStringList("Chicken Stick Sounds", "general", new String[] {
                "mob.chicken.say",
                "mob.chicken.hurt",
                "mob.chicken.plop",
                "mob.chicken.step"
        }, "The sound names the chicken stick will randomly play.");
        if(config.hasKey("general", "chickenOut")) {
            chickenOut = true;
        }
        String[] chickenStickNameList = config.getStringList("Custom Chicken Stick Names", "general", new String[] {}, "Format: Username=ItemName, Username can be * to affect all users");
        chickenStickNames.put("wyld", "The Cluckington");
        chickenStickNames.put("slowpoke101", "Dark Matter Hammer");
        chickenStickNames.put("jake_evans", "Cock Stick");
        for(String name : chickenStickNameList) {
            String[] s = name.split("=");
            if(s.length >= 2) {
                chickenStickNames.put(s[0].toLowerCase(), s[1]);
            }
        }

        String[] validChickenStickBlocks = config.getStringList("Valid Chicken Stick Blocks", "registries", new String[]{
                "minecraft:cobblestone",
                "minecraft:gravel",
                "minecraft:sand"
        }, "Here you can add additional blocks t he chicken stick will be able to break. Format: modid:name:meta");
        for (String blockString : validChickenStickBlocks) {
            String[] s = blockString.split(":");
            if (s.length < 2) {
                ExCompressum.logger.error("Skipping chicken stick block " + blockString + " due to invalid format");
                continue;
            }
            Block block = GameRegistry.findBlock(s[0], s[1]);
            if(block == null) {
                ExCompressum.logger.error("Skipping chicken stick block " + blockString + " due to block not found");
                continue;
            }
            int meta = 0;
            if (s.length > 2) {
                meta = Integer.parseInt(s[2]);
            }
            addValidBlock(block, meta);
        }
    }

    public static String getChickenStickName() {
        return chickenStickName;
    }

    public static void setChickenStickName(String chickenStickName) {
        ChickenStickRegistry.chickenStickName = chickenStickName;
    }
}
