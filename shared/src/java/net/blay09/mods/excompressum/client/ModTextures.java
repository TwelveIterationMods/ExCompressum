package net.blay09.mods.excompressum.client;

import net.blay09.mods.balm.api.client.rendering.BalmTextures;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModTextures {

    public static TextureAtlasSprite iconEmptyBookSlot;
    public static TextureAtlasSprite iconEmptyMeshSlot;
    public static TextureAtlasSprite iconEmptyHammerSlot;
    public static TextureAtlasSprite iconEmptyCompressedHammerSlot;
    public static TextureAtlasSprite ironMeshSprite;
    public static TextureAtlasSprite stringMeshSprite;
    public static TextureAtlasSprite flintMeshSprite;
    public static TextureAtlasSprite diamondMeshSprite;
    public static final TextureAtlasSprite[] destroyBlockIcons = new TextureAtlasSprite[10];

    public static void initialize(BalmTextures textures) {
        textures.addSprite(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ExCompressum.MOD_ID, "items/empty_enchanted_book_slot"));
        textures.addSprite(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ExCompressum.MOD_ID, "items/empty_mesh_slot"));
        textures.addSprite(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ExCompressum.MOD_ID, "items/empty_hammer_slot"));
        textures.addSprite(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ExCompressum.MOD_ID, "items/empty_compressed_hammer_slot"));
        textures.addSprite(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ExCompressum.MOD_ID, "blocks/string_mesh"));
        textures.addSprite(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ExCompressum.MOD_ID, "blocks/flint_mesh"));
        textures.addSprite(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ExCompressum.MOD_ID, "blocks/iron_mesh"));
        textures.addSprite(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ExCompressum.MOD_ID, "blocks/diamond_mesh"));
        textures.addSprite(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ExCompressum.MOD_ID, "blocks/emerald_mesh"));
        textures.addSprite(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ExCompressum.MOD_ID, "blocks/netherite_mesh"));
    }

    @SubscribeEvent
    public static void afterTextureStitch(TextureStitchEvent.Post event) {
        if (event.getMap().location().equals(InventoryMenu.BLOCK_ATLAS)) {
            iconEmptyBookSlot = event.getMap().getSprite(new ResourceLocation(ExCompressum.MOD_ID, "items/empty_enchanted_book_slot"));
            iconEmptyMeshSlot = event.getMap().getSprite(new ResourceLocation(ExCompressum.MOD_ID, "items/empty_mesh_slot"));
            iconEmptyHammerSlot = event.getMap().getSprite(new ResourceLocation(ExCompressum.MOD_ID, "items/empty_hammer_slot"));
            iconEmptyCompressedHammerSlot = event.getMap().getSprite(new ResourceLocation(ExCompressum.MOD_ID, "items/empty_compressed_hammer_slot"));
            stringMeshSprite = event.getMap().getSprite(new ResourceLocation(ExCompressum.MOD_ID, "blocks/string_mesh"));
            flintMeshSprite = event.getMap().getSprite(new ResourceLocation(ExCompressum.MOD_ID, "blocks/flint_mesh"));
            ironMeshSprite = event.getMap().getSprite(new ResourceLocation(ExCompressum.MOD_ID, "blocks/iron_mesh"));
            diamondMeshSprite = event.getMap().getSprite(new ResourceLocation(ExCompressum.MOD_ID, "blocks/diamond_mesh"));

            for (int i = 0; i < destroyBlockIcons.length; i++) {
                destroyBlockIcons[i] = event.getMap().getSprite(new ResourceLocation("blocks/destroy_stage_" + i));
            }
        }
    }
}
