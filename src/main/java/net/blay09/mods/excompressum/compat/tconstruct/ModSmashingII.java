package net.blay09.mods.excompressum.compat.tconstruct;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import tconstruct.library.tools.ToolCore;
import tconstruct.modifiers.tools.ModBoolean;

public class ModSmashingII extends ModBoolean {

    public static final String NAME = "Smashing II";

    public ModSmashingII(ItemStack[] items, int effect) {
        super(items, effect, NAME, "\u00a79", NAME);
    }

    @Override
    protected boolean canModify(ItemStack toolStack, ItemStack[] input) {
        if (toolStack.getItem() instanceof ToolCore) {
            ToolCore toolCore = (ToolCore) toolStack.getItem();
            if (!validType(toolCore)) {
                return false;
            }

            NBTTagCompound tags = toolStack.getTagCompound().getCompoundTag("InfiTool");
            if (!tags.getBoolean("Lava") && !tags.hasKey("Lapis") && !tags.hasKey("Silk Touch") && !tags.hasKey("Crooked")) {
                return tags.getInteger("Modifiers") > 0 && !tags.getBoolean(key);
            }
        }
        return false;
    }

    @Override
    public void modify(ItemStack[] input, ItemStack tool) {
        NBTTagCompound tags = tool.getTagCompound().getCompoundTag("InfiTool");
        tags.setBoolean(NAME, true);

        int modifiers = tags.getInteger("Modifiers");
        modifiers -= 1;
        tags.setInteger("Modifiers", modifiers);

        int miningSpeed = tags.getInteger("MiningSpeed");
        miningSpeed -= 400;
        if (miningSpeed < 0) {
            miningSpeed = 0;
        }
        tags.setInteger("MiningSpeed", miningSpeed);

        int attack = tags.getInteger("Attack");
        attack += 3;
        tags.setInteger("Attack", attack);

        addToolTip(tool, "\u00a79" + NAME, "\u00a79" + key);
    }

    public boolean validType(ToolCore tool) {
        return tool.getToolName().equals("Mattock") ||
                tool.getToolName().equals("Hatchet") ||
                tool.getToolName().equals("Broadsword") ||
                tool.getToolName().equals("Longsword") ||
                tool.getToolName().equals("Rapier") ||
                tool.getToolName().equals("Cutlass") ||
                tool.getToolName().equals("Cleaver") ||
                tool.getToolName().equals("Lumber Axe") ||
                tool.getToolName().equals("Scythe") ||
                tool.getToolName().equals("Pickaxe") ||
                tool.getToolName().equals("Hammer") ||
                tool.getToolName().equals("Shovel") ||
                tool.getToolName().equals("Excavator");
    }
}
