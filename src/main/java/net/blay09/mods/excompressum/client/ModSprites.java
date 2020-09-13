package net.blay09.mods.excompressum.client;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ExCompressum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSprites {

    public static TextureAtlasSprite iconEmptyBookSlot;
    public static TextureAtlasSprite iconEmptyMeshSlot;
    public static TextureAtlasSprite iconEmptyHammerSlot;
    public static TextureAtlasSprite iconEmptyCompressedHammerSlot;
    public static TextureAtlasSprite ironMeshSprite;
    public static TextureAtlasSprite stringMeshSprite;
    public static TextureAtlasSprite flintMeshSprite;
    public static TextureAtlasSprite diamondMeshSprite;
    public static final TextureAtlasSprite[] destroyBlockIcons = new TextureAtlasSprite[10];

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        if (event.getMap().getTextureLocation().equals(PlayerContainer.LOCATION_BLOCKS_TEXTURE)) {
            event.addSprite(new ResourceLocation(ExCompressum.MOD_ID, "items/empty_enchanted_book_slot"));
            event.addSprite(new ResourceLocation(ExCompressum.MOD_ID, "items/empty_mesh_slot"));
            event.addSprite(new ResourceLocation(ExCompressum.MOD_ID, "items/empty_hammer_slot"));
            event.addSprite(new ResourceLocation(ExCompressum.MOD_ID, "items/empty_compressed_hammer_slot"));
            event.addSprite(new ResourceLocation(ExCompressum.MOD_ID, "blocks/string_mesh"));
            event.addSprite(new ResourceLocation(ExCompressum.MOD_ID, "blocks/flint_mesh"));
            event.addSprite(new ResourceLocation(ExCompressum.MOD_ID, "blocks/iron_mesh"));
            event.addSprite(new ResourceLocation(ExCompressum.MOD_ID, "blocks/diamond_mesh"));
        }
    }

    @SubscribeEvent
    public static void afterTextureStitch(TextureStitchEvent.Post event) {
        if (event.getMap().getTextureLocation().equals(PlayerContainer.LOCATION_BLOCKS_TEXTURE)) {
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
