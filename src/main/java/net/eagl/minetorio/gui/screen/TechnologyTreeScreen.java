package net.eagl.minetorio.gui.screen;


import com.mojang.blaze3d.systems.RenderSystem;
import net.eagl.minetorio.util.Technology;
import net.eagl.minetorio.util.TechnologyRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TechnologyTreeScreen extends Screen {
    private static final ResourceLocation NODE_TEXTURE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/tech.png");
    private static final ResourceLocation FLASK_FIELD = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/tech_flask_field.png");
    private float zoom = 1.0f;
    private float offsetX = 0;
    private float offsetY = 0;

    private double lastMouseX, lastMouseY;
    private boolean dragging = false;

    public TechnologyTreeScreen() {
        super(Component.literal("Technology Tree"));
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(offsetX, offsetY, 0);
        guiGraphics.pose().scale(zoom, zoom, 1f);

        float adjustedMouseX = (mouseX - offsetX) / zoom;
        float adjustedMouseY = (mouseY - offsetY) / zoom;

        for (Technology tech : TechnologyRegistry.getAll()) {
            int x1 = tech.getX();
            int y1 = tech.getY();

            for (String parentId : tech.getPrerequisites()) {
                Technology parent = TechnologyRegistry.get(parentId);
                if (parent != null) {
                    int x2 = parent.getX();
                    int y2 = parent.getY();
                    drawConnectionLine(guiGraphics, x1 + 64, y1 + 16, x2 + 64, y2 + 16, 0xFFFFFFFF);
                }
            }
        }


        List<Component> tooltip = null;

        for (Technology tech : TechnologyRegistry.getAll()) {
            int x = tech.getX();
            int y = tech.getY();

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, NODE_TEXTURE);

            guiGraphics.blit(
                    NODE_TEXTURE,
                    x,
                    y,
                    0,
                    0,
                    128,
                    60,
                    128,
                    60
            );


            renderItem(guiGraphics, new ItemStack(tech.getDisplayIcon()), x + 3, y + 3, 1.5f);

            drawString(
                    guiGraphics,
                    tech.getDisplayName().getString(),
                    x + 77 - this.font.width(tech.getDisplayName().getString())/2,
                    y+5,
                    0,
                    1
            );

            renderFlaskField(guiGraphics, x, y, tech);

            if (adjustedMouseX >= x && adjustedMouseX <= x + 128 && adjustedMouseY >= y && adjustedMouseY <= y + 60) {

                tooltip = List.of(
                        tech.getDisplayName(),
                        Component.literal("Cost: " + tech.getFormattedCost().getString())
                );

            }

        }
        guiGraphics.pose().popPose();

        if (tooltip != null) {
            guiGraphics.renderComponentTooltip(this.font, tooltip, mouseX, mouseY);
        }


        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void renderFlaskField(GuiGraphics guiGraphics, int x, int y, Technology tech){
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x + 3, y + 30, 0);
        guiGraphics.pose().scale( 0.7f, 0.7f, 1f);
        guiGraphics.blit(FLASK_FIELD,0,0,0,0,108,36,108,36);

        int flaskCount = 0;

        for (ItemStack baseCost : tech.getCost()) {
            String text = String.valueOf(baseCost.getCount());
            if (flaskCount < 6) {
                renderItem(guiGraphics, baseCost,1 + flaskCount * 18, 1, 1);
                drawString(
                        guiGraphics,
                        text,
                        Math.round ((flaskCount + 1) * 18 - this.font.width(text) * 0.75f) - 1,
                        Math.round(18 - this.font.lineHeight * 0.75f),
                        300,
                        0.75f
                );
            } else {
                renderItem(guiGraphics, baseCost, 1 + (flaskCount - 6) * 18, 19, 1);
                drawString(
                        guiGraphics,
                        text,
                        Math.round ((flaskCount - 5) * 18 - this.font.width(text) * 0.75f) - 1,
                        Math.round(36 - this.font.lineHeight * 0.75f),
                        300,
                        0.75f
                );
            }
            flaskCount++;
        }

        drawString(
                guiGraphics,
                " x " + timeToString(tech.getTime()) + " x " + tech.getCount(),
                110,
                18 - this.font.lineHeight/2,
                0,
                1
        );

        guiGraphics.pose().popPose();

    }

    private void renderItem(GuiGraphics guiGraphics, ItemStack itemStack, int dx, int dy, float scale){

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(dx, dy, 0);
        guiGraphics.pose().scale(scale, scale, 1f);
        guiGraphics.renderItem(itemStack, 0, 0);
        guiGraphics.pose().popPose();

    }

    private void drawString(GuiGraphics guiGraphics, String text, int dx, int dy, int dz, float scale){

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(dx, dy, dz);
        guiGraphics.pose().scale(scale, scale, 1f);
        guiGraphics.drawString(this.font, text, 0, 0, 0x000000, false);
        guiGraphics.pose().popPose();

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        float adjustedMouseX = (float) ((mouseX - offsetX) / zoom);
        float adjustedMouseY = (float) ((mouseY - offsetY) / zoom);

        for (Technology tech : TechnologyRegistry.getAll()) {
            int x = tech.getX();
            int y = tech.getY();
            int width = 128;
            int height = 60;

            if (adjustedMouseX >= x && adjustedMouseX <= x + width && adjustedMouseY >= y && adjustedMouseY <= y + height) {
                onTechnologyClicked(tech, button);
                return true;
            }
        }

        if (button == 0) {
            dragging = true;
            lastMouseX = mouseX;
            lastMouseY = mouseY;
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void onTechnologyClicked(Technology tech, int button) {
        // Тут логіка: наприклад, відкрити новий екран, зробити research тощо
        System.out.println("Clicked on: " + tech.getId() + " with button " + button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        float oldZoom = zoom;
        zoom += (float) (delta * 0.1f);
        zoom = Math.max(0.25f, Math.min(zoom, 3.0f));

        // Корекція позиції після зміни масштабу
        float zoomRatio = zoom / oldZoom;
        offsetX = (float) (mouseX + (offsetX - mouseX) * zoomRatio);
        offsetY = (float) (mouseY + (offsetY - mouseY) * zoomRatio);

        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && dragging) {
            dragging = false;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging && button == 0) {
            offsetX += (float) (mouseX - lastMouseX);
            offsetY += (float) (mouseY - lastMouseY);
            lastMouseX = mouseX;
            lastMouseY = mouseY;
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private void drawConnectionLine(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int color) {
        if (x1 == x2) {
            guiGraphics.vLine(x1, Math.min(y1, y2), Math.max(y1, y2), color);
        } else if (y1 == y2) {
            guiGraphics.hLine(Math.min(x1, x2), Math.max(x1, x2), y1, color);
        } else {
            guiGraphics.hLine(Math.min(x1, x2), Math.max(x1, x2), y1, color);
            guiGraphics.vLine(x2, Math.min(y1, y2), Math.max(y1, y2), color);
        }
    }

    private String timeToString(int time){

        int totalSeconds = time / 20;
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        StringBuilder sb = new StringBuilder();

        if (hours > 0) {
            sb.append(String.format("%d:", hours));
        }
        sb.append(String.format("%02d:", minutes));
        sb.append(String.format("%02d", seconds));

        return sb.toString().trim();
    }
}