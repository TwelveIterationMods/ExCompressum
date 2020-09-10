package net.blay09.mods.excompressum.registry;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExCompressum.MOD_ID)
public class RegisterErrorInformHandler {
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (AbstractRegistry.registryErrors.size() > 0) {
            event.getPlayer().sendMessage(new StringTextComponent(TextFormatting.RED + "There were errors loading the Ex Compressum registries:"), Util.DUMMY_UUID);
            TextFormatting lastFormatting = TextFormatting.WHITE;
            for (String error : AbstractRegistry.registryErrors) {
                event.getPlayer().sendMessage(new StringTextComponent(lastFormatting + "* " + error), Util.DUMMY_UUID);
                lastFormatting = lastFormatting == TextFormatting.GRAY ? TextFormatting.WHITE : TextFormatting.GRAY;
            }
        }
    }
}
