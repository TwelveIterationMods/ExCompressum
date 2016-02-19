package net.blay09.mods.excompressum;

import com.google.common.collect.Sets;
import exnihilo.items.hammers.ItemHammerBase;
import exnihilo.registries.HammerRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

import java.util.Set;

public class ItemCompressedHammer extends ItemTool implements ICompressedHammer {

    public static Set blocksEffectiveAgainst = Sets.newHashSet(ItemHammerBase.blocksEffectiveAgainst);

    private final String name;

    protected ItemCompressedHammer(ToolMaterial material, String name) {
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
        if ((CompressedHammerRegistry.registered(block, meta) || HammerRegistry.registered(new ItemStack(block, 1, meta))) && block.getHarvestLevel(meta) <= toolMaterial.getHarvestLevel()) {
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
}
