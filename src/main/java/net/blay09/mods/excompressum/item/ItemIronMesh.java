package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;

//@Optional.Interface(iface = "exnihiloomnia.items.meshs.ISieveMesh", modid = Compat.EXNIHILO_OMNIA)
public class ItemIronMesh extends ItemCompressum {// implements ISieveMesh { // TODO awaiting Ex Nihilo Omnia port

    public ItemIronMesh() {
        setRegistryName("iron_mesh");
        setUnlocalizedName(getRegistryNameString());
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
