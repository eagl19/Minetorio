package net.eagl.minetorio.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FluidTargetWidget extends AbstractWidget {

    private final ItemStack stack;
    private final BlockPos pos;
    private boolean selected;
    private final int id;

    public FluidTargetWidget(int x, int y, int width, int height, ItemStack stack, BlockPos pos, int id) {
        super(x, y, width, height, Component.empty());
        this.stack = stack;
        this.pos = pos;
        this.selected = false;
        this.id = id;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int bgColor = selected ? 0x8822AA22 : 0x33000000;
        guiGraphics.fill(getX(), getY(), getX() + width, getY() + height, bgColor);

        if (isHovered()) {
            RenderSystem.setShaderColor(0.6f, 0.6f, 0.6f, 1.0f);
        } else {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        }

        guiGraphics.renderItem(stack, getX() + 2, getY() + 2);

        MutableComponent text = Component.translatable(stack.getItem().getDescriptionId())
                .append(Component.literal(String.format(" (%d, %d, %d)", pos.getX(), pos.getY(), pos.getZ()))
                        .withStyle(ChatFormatting.GRAY));

        guiGraphics.drawString(Minecraft.getInstance().font, text, getX() + 22, getY() + 6, 0x000000,false);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        selected = !selected;
    }

    public void setSelected(boolean b){
        selected = b;
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {

    }

    public boolean isSelected() {
        return selected;
    }

    public int getId(){
        return id;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void setVisible(boolean b) {
        visible = b;
    }
}
