package net.eagl.minetorio.gui.screen;


import net.eagl.minetorio.gui.slot.FlaskSlot;
import net.eagl.minetorio.gui.widget.ItemIconWidget;
import net.eagl.minetorio.gui.widget.RemovableItemWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;

public abstract class AbstractFluidGeneratorScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> implements IGeneratorScreen {

    private final ResourceLocation texture;

    protected AbstractFluidGeneratorScreen(T menu, Inventory inv, Component title, ResourceLocation texture) {
        super(menu, inv, title);
        this.texture = texture;
        this.imageHeight = 222;
        this.imageWidth = 176;
    }

    @Override
    protected void init() {
        super.init();

        update();
    }

    protected abstract int getEnergy();

    protected abstract int getMaxEnergy();

    protected abstract int getFluid();

    protected abstract int getMaxFluid();

    protected abstract int getProduce();

    protected abstract int getMaxProduce();

    protected abstract List<BlockPos> getGeneratorConsumers();

    protected abstract ItemStack getItemFromBlockPos(BlockPos pos);

    protected abstract ItemStack getUpdateIcon();

    protected abstract void sendOpenPacket();
    protected abstract void sendRemovePacket(int index);

    protected abstract ItemStack getPatternIcon();
    protected abstract ItemStack getConsumerPatternIcon();

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.setColor(1f, 1f, 1f, 1f);
        graphics.blit(texture, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        // Energy bar
        int energy = getEnergy();
        int maxEnergy = getMaxEnergy();
        if (maxEnergy > 0 && energy > 0) {
            int barHeight = 128;
            int height = (int) ((float) energy / maxEnergy * barHeight);
            graphics.blit(texture, leftPos + 155, topPos + 5 + (barHeight - height), 178, 5 + barHeight - height, 12, height);
        }
        if(mouseX > leftPos + 155 && mouseX < leftPos + 167 && mouseY > topPos + 5 && mouseY < topPos + 133) {
            graphics.renderTooltip(this.font, Component.literal(String.valueOf(energy)).withStyle(ChatFormatting.GOLD), mouseX, mouseY);
        }

        // Fluid bar
        int fluid = getFluid();
        int maxFluid = getMaxFluid();
        if (maxFluid > 0 && fluid > 0) {
            int barHeight = 128;
            int height = (int) ((float) fluid / maxFluid * barHeight);
            graphics.blit(texture, leftPos + 138, topPos + 5 + (barHeight - height), 192, 5 + barHeight - height, 12, height);
        }
        if(mouseX > leftPos + 138 && mouseX < leftPos + 150 && mouseY > topPos + 5 && mouseY < topPos + 133) {
            graphics.renderTooltip(this.font, Component.literal(String.valueOf(fluid)).withStyle(ChatFormatting.GOLD), mouseX, mouseY);
        }

        // Produce bar
        int produce = getMaxProduce() - getProduce();
        if (getMaxProduce() > 0 && produce > 0) {
            int barWidth = 102;
            int width = (int) ((float) produce / getMaxProduce() * barWidth);
            graphics.blit(texture, leftPos + 28, topPos + 56, 28, 225, width, 24);
        }
        if(mouseX > leftPos + 28 && mouseX < leftPos + 130 && mouseY > topPos + 56 && mouseY < topPos + 80) {
            double percent = (double) produce / getMaxProduce() * 100;
            DecimalFormat df = new DecimalFormat("0.00");
            graphics.renderTooltip(this.font, Component.literal(df.format(percent) + "%").withStyle(ChatFormatting.GREEN), mouseX, mouseY);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics g, int mx, int my, float pt) {
        renderBackground(g);
        super.render(g, mx, my, pt);
        renderTooltip(g, mx, my);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics pGuiGraphics, int mx, int my) {
        for (Slot slot : menu.slots) {
            if (slot instanceof FlaskSlot flask && !flask.isVisible()) {
                pGuiGraphics.renderItem(new ItemStack(flask.getFlask()),slot.x, slot.y);
                pGuiGraphics.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, 0x88000000);
            }
        }
    }

    public void update() {

        this.clearWidgets();

        this.addRenderableWidget(new ItemIconWidget(
                leftPos + 8 , topPos + 5,
                getPatternIcon(),
                null
        ));

        List<BlockPos> list = getGeneratorConsumers();

        for(int i = 0; i < 6; i++){
            if(i < list.size()) {
                int finalI = i;
                this.addRenderableWidget(new RemovableItemWidget(
                        leftPos + 27 + i * 18, topPos + 5,
                        getItemFromBlockPos(list.get(i)),
                        this::openAction,
                        () -> removeAction(finalI)
                ));
            }else {
                this.addRenderableWidget(new ItemIconWidget(
                        leftPos + 27 + i * 18, topPos + 5,
                        getConsumerPatternIcon(),
                        this::openAction
                ));
            }
        }

        this.addRenderableWidget(new ItemIconWidget(
                leftPos + 8 , topPos + 117,
                getUpdateIcon(),
                null
        ));

        for(int i = 0; i < 6; i++){
            if( i + 6 < list.size()) {
                int finalI = i;
                this.addRenderableWidget(new RemovableItemWidget(
                        leftPos + 27 + i * 18, topPos + 117,
                        getItemFromBlockPos(list.get(i + 6)),
                        this::openAction,
                        () -> removeAction(finalI + 6)
                ));
            }else {
                this.addRenderableWidget(new ItemIconWidget(
                        leftPos + 27 + i * 18, topPos + 117,
                        getConsumerPatternIcon(),
                        this::openAction
                ));
            }
        }
    }

    private void openAction(){

        sendOpenPacket();
    }

    private void removeAction(int index){
        sendRemovePacket(index);
    }
}

