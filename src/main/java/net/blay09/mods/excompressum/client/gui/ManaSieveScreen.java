package net.blay09.mods.excompressum.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.container.AutoSieveContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ManaSieveScreen extends AutoSieveScreen {

    private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/mana_sieve.png");

    public ManaSieveScreen(AutoSieveContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
    }

    @Override
    public ResourceLocation getBackgroundTexture() {
        return texture;
    }

    @Override
    protected void renderEnergyBar(MatrixStack matrixStack) {
    }

    @Override
    protected void renderPowerTooltip(MatrixStack matrixStack, int mouseX, int mouseY) {
    }
}
