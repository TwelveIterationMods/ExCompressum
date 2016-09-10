package net.blay09.mods.excompressum.block;

import com.mojang.authlib.GameProfile;
import net.blay09.mods.excompressum.registry.AutoSieveSkinRegistry;
import net.blay09.mods.excompressum.tile.TileAutoSieve;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockAutoSieve extends BlockAutoSieveBase {

    private ItemStack lastHoverStack;
    private String currentRandomName;

    public BlockAutoSieve(String registryName) {
        super(Material.IRON);
        setRegistryName(registryName);
        setUnlocalizedName(getRegistryName().toString());
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileAutoSieve();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileAutoSieve tileEntity = (TileAutoSieve) world.getTileEntity(pos);
        if(tileEntity != null) {
            NBTTagCompound tagCompound = stack.getTagCompound();
            if (tagCompound != null) {
                if (tagCompound.hasKey("EnergyStored")) {
                    tileEntity.setEnergyStored(tagCompound.getInteger("EnergyStored"));
                }
            }
        }
        super.onBlockPlacedBy(world, pos, state, placer, stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> list, boolean flag) {
        NBTTagCompound tagCompound = itemStack.getTagCompound();
        if(tagCompound != null && tagCompound.hasKey("CustomSkin")) {
            GameProfile customSkin = NBTUtil.readGameProfileFromNBT(tagCompound.getCompoundTag("CustomSkin"));
            if(customSkin != null) {
                list.add(TextFormatting.GRAY + I18n.format("tooltip." + getRegistryName(), customSkin.getName()));
            }
        } else {
            if(currentRandomName == null) {
                currentRandomName = AutoSieveSkinRegistry.getRandomSkin();
            }
            list.add(TextFormatting.GRAY + I18n.format("tooltip." + getRegistryName(), currentRandomName));
        }
        if(lastHoverStack != itemStack) {
            currentRandomName = AutoSieveSkinRegistry.getRandomSkin();
            lastHoverStack = itemStack;
        }
    }

}
