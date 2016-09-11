package net.blay09.mods.excompressum.item;

import exnihiloomnia.items.meshs.ISieveMesh;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.client.ClientProxy;
import net.blay09.mods.excompressum.compat.Compat;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Optional.Interface(iface = "exnihiloomnia.items.meshs.ISieveMesh", modid = Compat.EXNIHILO_OMNIA)
public class ItemIronMesh extends Item implements ISieveMesh {

    public ItemIronMesh() {
        setRegistryName("iron_mesh");
        setUnlocalizedName(getRegistryName().toString());
        setCreativeTab(ExCompressum.creativeTab);
        setMaxDamage(256);
        setMaxStackSize(1);
    }

    @Override
    public int getItemEnchantability() {
        return 30;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getMeshTexture() {
        return ClientProxy.ironMeshSprite;
    }
}
