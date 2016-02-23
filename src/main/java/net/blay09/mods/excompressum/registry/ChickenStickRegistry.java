package net.blay09.mods.excompressum.registry;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.item.ItemChickenStick;
import net.minecraft.block.Block;
import net.minecraftforge.common.config.Configuration;

import java.util.List;

public class ChickenStickRegistry {

    private static class BlockInfo {
        public final Block block;
        public final int meta;

        public BlockInfo(Block block, int meta) {
            this.block = block;
            this.meta = meta;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BlockInfo blockInfo = (BlockInfo) o;

            return meta == blockInfo.meta && block.equals(blockInfo.block);
        }

        @Override
        public int hashCode() {
            int result = block.hashCode();
            result = 31 * result + meta;
            return result;
        }
    }

    private static final List<BlockInfo> validBlocks = Lists.newArrayList();

    public static void addValidBlock(Block block, int meta) {
        validBlocks.add(new BlockInfo(block, meta));
        ItemChickenStick.blocksEffectiveAgainst.add(block);
    }

    public static boolean isValidBlock(Block block, int meta) {
        return validBlocks.contains(new BlockInfo(block, meta));
    }

    public static void load(Configuration config) {
        String[] validChickenStickBlocks = config.getStringList("Valid Chicken Stick Blocks", "general", new String[]{
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
            int meta = 0;
            if (s.length > 2) {
                meta = Integer.parseInt(s[2]);
            }
            addValidBlock(block, meta);
        }
    }

}
