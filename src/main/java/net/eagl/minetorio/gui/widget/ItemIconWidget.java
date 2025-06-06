package net.eagl.minetorio.gui.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;


public class ItemIconWidget extends AbstractWidget {
    private final ItemStack stack;

    public ItemIconWidget(int x, int y, ItemStack stack) {
        super(x, y, 16, 16, Component.empty());
        this.stack = stack;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.renderItem(stack, getX(), getY());

        if (isHoveredOrFocused()) {
            graphics.fill(getX(), getY(), getX() + 16, getY() + 16, 0x44FFFFFF);
        }
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {

    }

}

