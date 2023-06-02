package com.github.sib_energy_craft.energy_container.screen;

import com.github.sib_energy_craft.energy_api.utils.Identifiers;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * @since 0.0.1
 * @author sibmaks
 */
public class EnergyContainerScreen extends HandledScreen<EnergyContainerScreenHandler> {
    private static final Identifier TEXTURE = Identifiers.of("textures/gui/container/energy_container.png");

    public EnergyContainerScreen(@NotNull EnergyContainerScreenHandler handler,
                                 @NotNull PlayerInventory inventory,
                                 @NotNull Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(@NotNull DrawContext drawContext, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawContext.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
        int i = this.x;
        int j = this.y;
        int chargeProgress = this.handler.getChargeProgress();
        drawContext.drawTexture(TEXTURE, i + 84, j + 38, 176, 0, chargeProgress, 9);
        if(mouseX >= i + 84 && mouseX <= i + 84 + 24 && mouseY >= j + 38 && mouseY <= j + 38 + 9) {
            int charge = this.handler.getCharge();
            int maxCharge = this.handler.getMaxCharge();
            var charging = Text.translatable("energy.range.text", charge, maxCharge);
            drawContext.drawTooltip(textRenderer, charging, mouseX, mouseY);
        }
        var output = Text.translatable("energy.out.text", this.handler.getPacketSize());
        drawContext.drawText(textRenderer, output, i + 81, j + 58, Color.GRAY.getRGB(), false);
    }

    @Override
    public void render(@NotNull DrawContext drawContext, int mouseX, int mouseY, float delta) {
        renderBackground(drawContext);
        super.render(drawContext, mouseX, mouseY, delta);
        drawMouseoverTooltip(drawContext, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }
}