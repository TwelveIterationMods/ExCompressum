package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

//@Optional.Interface(iface = "exnihiloomnia.items.meshs.ISieveMesh", modid = Compat.EXNIHILO_OMNIA)
public class ItemIronMesh extends Item {// implements ISieveMesh { // TODO awaiting Ex Nihilo Omnia port

    public static final String name = "iron_mesh";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public ItemIronMesh() {
        setUnlocalizedName(registryName.toString());
        setCreativeTab(ExCompressum.creativeTab);
        setMaxDamage(256);
        setMaxStackSize(1);
    }

    @Override
    public int getItemEnchantability() {
        return 30;
    }

    //@Override // TODO awaiting Ex Nihilo Omnia port
    //@SideOnly(Side.CLIENT)
    //public TextureAtlasSprite getMeshTexture() {
    //    return ClientProxy.ironMeshSprite;
    //}

}
