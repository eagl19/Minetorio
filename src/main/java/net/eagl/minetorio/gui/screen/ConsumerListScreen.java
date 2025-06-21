package net.eagl.minetorio.gui.screen;

import net.eagl.minetorio.gui.WaterGeneratorMenu;
import net.eagl.minetorio.gui.widget.FluidTargetWidget;
import net.eagl.minetorio.util.CachedBlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ConsumerListScreen extends Screen {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/tech_details.png");
    private static final int TEXTURE_WIDTH = 289;
    private static final int TEXTURE_HEIGHT = 326;

    private static final ResourceLocation BUTTON_OK_TEXTURE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/button_ok.png");
    private static final ResourceLocation BUTTON_CANCEL_TEXTURE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/button_cancel.png");
    private static final int BUTTON_TEXTURE_WIDTH = 358;
    private static final int BUTTON_TEXTURE_HEIGHT = 219;


    private  int selectedWidget;
    private final WaterGeneratorMenu menu;
    private final Inventory playerInventory;
    private final Component title;

    private final List<FluidTargetWidget> fluidWidgets = new ArrayList<>();

    private int scrollOffsetY = 0;
    private final int maxVisibleWidgets = 6;
    private final int widgetHeight = 28;

    float okX;
    float okY;
    float cancelX;
    float cancelY;

    float btnWidth;
    float btnHeight;


    protected ConsumerListScreen(WaterGeneratorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pTitle);
        this.menu = pMenu;
        this.playerInventory = pPlayerInventory;
        this.title = pTitle;
    }

    @Override
    protected void init() {
        super.init();
       update();
    }

    public void update(){
        this.clearWidgets();
        fluidWidgets.clear();

        List<BlockPos> list = menu.getFluidTargets().getListPos();
        for (BlockPos pos : list) {
            ItemStack stack = menu.getItemFromBlockPos(pos);
            if (!stack.isEmpty()) {
                FluidTargetWidget widget = new FluidTargetWidget(0, 0, 0, 0, stack, pos);
                fluidWidgets.add(widget);
                addRenderableWidget(widget);
            }
        }

        updateWidgetPositions();
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        float scale = (float) this.height / TEXTURE_HEIGHT;

        float x = (this.width - TEXTURE_WIDTH * scale) / 2;
        float y = (this.height - TEXTURE_HEIGHT * scale) / 2;

        pGuiGraphics.pose().pushPose();

        pGuiGraphics.pose().translate(x, y,0.0f);
        pGuiGraphics.pose().scale(scale, scale, 1.0f);

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.blit(TEXTURE, 0, 0, 0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);

        renderName(pGuiGraphics);


        float scale_button = (float) 15 / BUTTON_TEXTURE_HEIGHT;

        okX = TEXTURE_WIDTH - 50 - BUTTON_TEXTURE_WIDTH * scale_button * 2;
        okY = TEXTURE_HEIGHT - 70;
        cancelX = TEXTURE_WIDTH - 40 - BUTTON_TEXTURE_WIDTH * scale_button;
        cancelY = TEXTURE_HEIGHT - 70;

        renderButton(pGuiGraphics, BUTTON_OK_TEXTURE, okX, okY, scale_button);
        renderButton(pGuiGraphics, BUTTON_CANCEL_TEXTURE, cancelX, cancelY, scale_button);

        okX = x + (TEXTURE_WIDTH - 50 - BUTTON_TEXTURE_WIDTH * scale_button * 2) * scale;
        okY = y + (TEXTURE_HEIGHT - 70) * scale;
        cancelX = x + (TEXTURE_WIDTH - 40 - BUTTON_TEXTURE_WIDTH * scale_button) * scale;
        cancelY = y + (TEXTURE_HEIGHT - 70) * scale;

        btnWidth = BUTTON_TEXTURE_WIDTH * scale_button * scale;
        btnHeight = BUTTON_TEXTURE_HEIGHT * scale_button * scale;

        pGuiGraphics.pose().popPose();
        pGuiGraphics.pose().popPose();


        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderScrollbar(pGuiGraphics);
    }

    private void renderScrollbar(GuiGraphics graphics) {
        int totalHeight = fluidWidgets.size() * widgetHeight;
        int visibleHeight = maxVisibleWidgets * widgetHeight;

        if (totalHeight <= visibleHeight) return;

        float scale = (float) this.height / TEXTURE_HEIGHT;
        float x = (this.width - TEXTURE_WIDTH * scale) / 2;
        float y = (this.height - TEXTURE_HEIGHT * scale) / 2;

        int barX = (int) (x + (TEXTURE_WIDTH - 25) * scale);
        int barY = (int) (y + 70 * scale);
        int barHeight = (int) (visibleHeight * scale);

        int trackHeight = (int) ((float) visibleHeight / totalHeight * barHeight);
        int trackOffset = (int) ((float) scrollOffsetY / totalHeight * barHeight);

        graphics.fill(barX, barY, barX + 2, barY + barHeight, 0xFFAAAAAA);
        graphics.fill(barX, barY + trackOffset, barX + 2, barY + trackOffset + trackHeight, 0xFF555555);
    }

    private void renderName(GuiGraphics guiGraphics) {
        String name = "Water Consumer";
        int x = TEXTURE_WIDTH / 2;
        int y = 40;
        float scale = 2;
        int nameY = Math.round(y + this.font.lineHeight * scale / 2);
        int nameX = Math.round(x - this.font.width(name) * scale / 2);
        drawString(guiGraphics, name, nameX, nameY, scale);
    }

    private void drawString(GuiGraphics guiGraphics, String text, int dx, int dy, float scale) {
        int dz = 0;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(dx, dy, dz);
        guiGraphics.pose().scale(scale, scale, 1f);
        guiGraphics.drawString(this.font, text, 0, 0, 0x000000, false);
        guiGraphics.pose().popPose();
    }

    private void renderButton(@NotNull GuiGraphics guiGraphics, ResourceLocation texture, float dx, float dy, float scaleButton) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(dx, dy, 0 );

        guiGraphics.pose().scale(scaleButton, scaleButton, 1);
        guiGraphics.blit(texture,
                0,
                0,
                0,
                0,
                BUTTON_TEXTURE_WIDTH,
                BUTTON_TEXTURE_HEIGHT,
                BUTTON_TEXTURE_WIDTH,
                BUTTON_TEXTURE_HEIGHT
        );
        guiGraphics.pose().popPose();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        boolean handled = super.mouseClicked(mouseX, mouseY, button);

        selectedWidget = 0;
        for (var widget : this.renderables) {
            if (widget instanceof FluidTargetWidget fluidWidget) {
                if(fluidWidget.isSelected()){
                    selectedWidget++;
                }
            }
        }
        updateWidgetPositions();

        if (isMouseOver((float) mouseX, (float) mouseY, (int) okX, (int) okY, (int) btnWidth, (int) btnHeight)) {
            onOkButtonClicked();
            return true;
        }

        if (isMouseOver((float) mouseX, (float) mouseY, (int) cancelX, (int) cancelY, (int) btnWidth, (int) btnHeight)) {
            onCancelButtonClicked();
            return true;
        }

        return handled;
    }

    private void onOkButtonClicked() {

    }

    private void onCancelButtonClicked() {
        Minecraft.getInstance().setScreen(new WaterGeneratorScreen(menu, this.playerInventory, this.title));
    }

    private boolean isMouseOver(float mx, float my, int x, int y, int width, int height) {
        return mx >= x && mx <= x + width && my >= y && my <= y + height;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int totalHeight = fluidWidgets.size() * widgetHeight;
        int maxOffset = Math.max(0, totalHeight - maxVisibleWidgets * widgetHeight);

        int previousOffset = scrollOffsetY;
        scrollOffsetY -= (int) (delta * 10);
        scrollOffsetY = Math.max(0, Math.min(scrollOffsetY, maxOffset));

        if (scrollOffsetY != previousOffset) {
            updateWidgetPositions();
        }

        return true;
    }

    private void updateWidgetPositions() {
        float scale = (float) this.height / TEXTURE_HEIGHT;
        float x = (this.width - TEXTURE_WIDTH * scale) / 2;
        float y = (this.height - TEXTURE_HEIGHT * scale) / 2;

        float minDY = y + 60 * scale;
        float maxDY = y + (60 + 6 * widgetHeight) * scale;

        for (int i = 0; i < fluidWidgets.size(); i++) {
            FluidTargetWidget widget = fluidWidgets.get(i);
            int dx = (int) (x + 30 * scale);
            int dy = (int) (y + (70 + i * widgetHeight - scrollOffsetY) * scale);
            int w = (int) ((TEXTURE_WIDTH - 60) * scale);
            int h = (int) (24 * scale);
            if(dy > minDY && dy < maxDY) {
                widget.setX(dx);
                widget.setY(dy);
                widget.setWidth(w);
                widget.setHeight(h);
                widget.setVisible(widget.isSelected() || selectedWidget < 6);
            }else {
                widget.setVisible(false);
            }
        }
    }

    public CachedBlockPos getFluidTargets() {
        return menu.getFluidTargets();
    }
}
