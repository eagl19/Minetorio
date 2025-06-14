package net.eagl.minetorio.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;



public class RemovableItemWidget extends AbstractWidget {
    private final ItemStack itemStack;
    private final Runnable onClickIcon;
    private final Runnable onClickRemove;
    private static final ResourceLocation CANCEL_TEXTURE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/cancel.png");
    private static final int CANCEL_WIDTH = 7;
    private static final int CANCEL_HEIGHT = 7;
    private static float dx;
    private static float dy;
    private static float scaleButton;

    public RemovableItemWidget(int x, int y, ItemStack stack, Runnable iconAction, Runnable removeAction) {
        super(x, y, 16, 16, Component.empty());
        this.itemStack = stack;
        this.onClickIcon = iconAction;
        this.onClickRemove = removeAction;
        scaleButton = 5f / CANCEL_WIDTH;
        dx = this.width - CANCEL_WIDTH * scaleButton;
        dy = 0;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        double relX = mouseX - getX();
        double relY = mouseY - getY();

        if (relX >= dx && relY <= CANCEL_HEIGHT * scaleButton) {
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

        RenderSystem.setShaderColor(1.0f, 0.5f, 0.5f, 1.0f);
        graphics.pose().pushPose();
        graphics.pose().translate(getX() + dx, getY() + dy, 300 );

        graphics.pose().scale(scaleButton, scaleButton, 1);
        graphics.blit(CANCEL_TEXTURE,
                0,
                0,
                0,
                0,
                CANCEL_WIDTH,
                CANCEL_HEIGHT,
                CANCEL_WIDTH,
                CANCEL_HEIGHT
        );
        graphics.pose().popPose();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}

