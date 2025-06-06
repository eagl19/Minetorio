package net.eagl.minetorio.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;



public class RemovableItemIcon extends AbstractWidget {
    private final ItemStack itemStack;
    private final Runnable onClickIcon;
    private final Runnable onClickRemove;

    public RemovableItemIcon(int x, int y, ItemStack stack, Runnable iconAction, Runnable removeAction) {
        super(x, y, 18, 18, Component.empty());
        this.itemStack = stack;
        this.onClickIcon = iconAction;
        this.onClickRemove = removeAction;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        double relX = mouseX - getX();
        double relY = mouseY - getY();

        if (relX >= 11 && relY <= 6) {
            onClickRemove.run();
        } else {
            onClickIcon.run();
        }
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.renderItem(itemStack, getX(), getY());

        graphics.pose().pushPose();
        graphics.pose().translate(0,0, 300);

        graphics.drawString(
                Minecraft.getInstance().font, "X",
                getX() + 12, getY() + 1,
                0xFF0000, true
        );
        graphics.pose().popPose();
    }
}

