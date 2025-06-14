package net.eagl.minetorio.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ButtonWidget extends AbstractWidget {

    private final Runnable onClickIcon;
    private final ResourceLocation texture;
    private final int texture_width;
    private final int texture_height;
    private final Component text;

    public ButtonWidget(int pX, int pY, int pWidth, int pHeight, ResourceLocation pTexture, Component pText, Runnable buttonAction) {

        super(pX, pY, pWidth, pHeight, Component.empty());
        this.onClickIcon = buttonAction;
        this.texture = pTexture;
        this.texture_width = pWidth;
        this.texture_height = pHeight;
        this.text = pText;
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        this.onClickIcon.run();
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (isHovered()) {
            RenderSystem.setShaderColor(0.6f, 0.6f, 0.6f, 1.0f);
        } else {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
        pGuiGraphics.blit(texture,
                getX(),
                getY(),
                0,
                0,
                texture_width,
                texture_height,
                texture_width,
                texture_height
        );

        pGuiGraphics.drawString(Minecraft.getInstance().font,
                this.text,
                getX() + 16, getY() + 1 + (texture_height - Minecraft.getInstance().font.lineHeight) / 2,
                0x000000,
                false);

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {

    }
}
