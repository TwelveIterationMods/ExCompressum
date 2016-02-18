package net.blay09.mods.excompressum;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;

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

}
