package net.blay09.mods.excompressum.client;

import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import net.blay09.mods.excompressum.CommonProxy;
import net.minecraft.client.Minecraft;

import java.util.Set;

public class ClientProxy extends CommonProxy {

    private final Set<GameProfile> skinRequested = Sets.newHashSet();

    @Override
    public void preloadSkin(GameProfile customSkin) {
        if (!skinRequested.contains(customSkin)) {
            Minecraft.getInstance().getSkinManager().loadProfileTextures(customSkin, (typeIn, location, profileTexture) -> {
            }, true);
            skinRequested.add(customSkin);
        }
    }

}
