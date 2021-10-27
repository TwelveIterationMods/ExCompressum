package net.blay09.mods.excompressum.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.menu.AutoSieveMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ManaSieveScreen extends AutoSieveScreen {

    private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/mana_sieve.png");

    public ManaSieveScreen(AutoSieveMenu container, Inventory inventory, Component title) {
        super(container, inventory, title);
    }

    @Override
    public ResourceLocation getBackgroundTexture() {
        return texture;
    }

    @Override
    protected void renderEnergyBar(PoseStack poseStack) {
    }

    @Override
    protected void renderPowerTooltip(PoseStack poseStack, int mouseX, int mouseY) {
    }
}
