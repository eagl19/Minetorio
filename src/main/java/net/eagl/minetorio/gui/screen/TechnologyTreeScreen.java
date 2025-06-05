package net.eagl.minetorio.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.eagl.minetorio.util.Clock;
import net.eagl.minetorio.util.Technology;
import net.eagl.minetorio.util.TechnologyRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TechnologyTreeScreen extends Screen {
    private static final ResourceLocation NODE_TEXTURE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/tech.png");
    private  static final int NODE_TEXTURE_WIDTH = 128;
    private  static final int NODE_TEXTURE_HEIGHT = 60;

    private static final ResourceLocation FLASK_FIELD = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/tech_flask_field.png");
    private static final ResourceLocation TECH_DETAILS_TEXTURE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/tech_details.png");
    private  static final int TECH_DETAILS_TEXTURE_WIDTH = 219;
    private  static final int TECH_DETAILS_TEXTURE_HEIGHT = 326;


    private float zoom = 1.0f;
    private float offsetX = 0, offsetY = 0;
    private double lastMouseX, lastMouseY;
    private boolean dragging = false;

    private Technology techDetails = null;
    public TechnologyTreeScreen() {
        super(Component.literal("Technology Tree"));
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        if (techDetails != null) {

            renderTechnologyDetails(guiGraphics);

        } else {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(offsetX, offsetY, 0);
            guiGraphics.pose().scale(zoom, zoom, 1f);

            float adjustedMouseX = (mouseX - offsetX) / zoom;
            float adjustedMouseY = (mouseY - offsetY) / zoom;

            renderConnections(guiGraphics);

            List<Component> tooltip = null;
            for (Technology tech : TechnologyRegistry.getAll()) {
                List<Component> result = renderTechnology(guiGraphics, tech, adjustedMouseX, adjustedMouseY);
                if (result != null) tooltip = result;
            }

            guiGraphics.pose().popPose();

            if (tooltip != null) {
                guiGraphics.renderComponentTooltip(this.font, tooltip, mouseX, mouseY);
            }
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void renderTechnologyDetails(@NotNull GuiGraphics guiGraphics) {
        int x =  (this.width - TECH_DETAILS_TEXTURE_WIDTH) / 2;
        int y = (this.height - TECH_DETAILS_TEXTURE_HEIGHT) / 2;

        guiGraphics.pose().pushPose();

        guiGraphics.pose().translate(x, y,0.0f);
        renderTechBackground(guiGraphics, TECH_DETAILS_TEXTURE, TECH_DETAILS_TEXTURE_WIDTH, TECH_DETAILS_TEXTURE_HEIGHT,0,0);

        renderTechName(guiGraphics, techDetails, TECH_DETAILS_TEXTURE_WIDTH / 2, 40, 2);

        renderItem(guiGraphics, new ItemStack(techDetails.getDisplayIcon()), 30, 75, 3);

        renderBenefit(guiGraphics, 83, 75, 0.9f);

        drawString(guiGraphics, techDetails.getTimeAsString(),30, 130, 0, 1);

        renderCost(guiGraphics, 30, 140, 1);

        guiGraphics.pose().popPose();
    }

    private void renderCost(GuiGraphics guiGraphics, int x, int y, float scale) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y,0);
        guiGraphics.pose().scale(scale,scale,0);

        drawString(guiGraphics,
                Component.translatable("tooltip.minetorio.cost").append(" : ")
                        .withStyle(ChatFormatting.DARK_RED),
                0, 0, 0, 1);
        int i = 0;
        for (ItemStack stack : techDetails.getCost()) {
            i++;
            System.out.println(stack);
            renderCostItem(guiGraphics, stack, 2, 12 * i);
        }
        guiGraphics.pose().popPose();
    }

    private void renderCostItem(GuiGraphics guiGraphics, ItemStack stack, int x, int y) {
        renderItem(guiGraphics, stack, x, y - 4, 1);
        drawString(
                guiGraphics,
                " x " + stack.getCount() + " " +
                        stack.getDisplayName().getString()
                                .replace("[","")
                                .replace("]",""),
                x + 16,
                y,
                0,
                1
        );
    }


    private void renderBenefit(GuiGraphics guiGraphics, int x, int y, float scale){
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x,y,0);
        guiGraphics.pose().scale(scale,scale,0);
        int height;
        List<FormattedCharSequence> lines = this.font.split(techDetails.getBenefit(), (int) ((TECH_DETAILS_TEXTURE_WIDTH - x -5) / scale));
        for (int i = 0; i < lines.size(); i++) {
            height = Math.round(i * (font.lineHeight * scale + 3));
            drawString(guiGraphics, lines.get(i), 0, height, 0, 1);
        }
        guiGraphics.pose().popPose();
    }

    private void renderConnections(GuiGraphics guiGraphics) {
        for (Technology tech : TechnologyRegistry.getAll()) {
            int x1 = tech.getX();
            int y1 = tech.getY();
            for (String parentId : tech.getPrerequisites()) {
                Technology parent = TechnologyRegistry.get(parentId);
                if (parent != null) {
                    int x2 = parent.getX();
                    int y2 = parent.getY();
                    drawConnectionLine(
                            guiGraphics,
                            x1 + NODE_TEXTURE_WIDTH / 2,
                            y1 + NODE_TEXTURE_HEIGHT / 2,
                            x2 + NODE_TEXTURE_WIDTH / 2,
                            y2 + NODE_TEXTURE_HEIGHT / 2,
                            0xFFFFFFFF
                    );
                }
            }
        }
    }

    private List<Component> renderTechnology(GuiGraphics guiGraphics, Technology tech, float mouseX, float mouseY) {
        int x = tech.getX();
        int y = tech.getY();

        renderTechBackground(guiGraphics, NODE_TEXTURE, NODE_TEXTURE_WIDTH, NODE_TEXTURE_HEIGHT, x, y);
        renderItem(guiGraphics, new ItemStack(tech.getDisplayIcon()), x + 3, y + 3, 1.5f);
        renderTechName(guiGraphics, tech, x + 77, y + 1, 1);
        renderFlaskField(guiGraphics, x, y, tech);

        if (isMouseOverNode(mouseX, mouseY, x, y, NODE_TEXTURE_WIDTH, NODE_TEXTURE_HEIGHT)) {
            return List.of(
                    tech.getDisplayName(),
                    Component.literal("Cost: " + tech.getFormattedCost().getString())
            );
        }
        return null;
    }

    private boolean isMouseOverNode(float mx, float my, int x, int y, int width, int height) {
        return mx >= x && mx <= x + width && my >= y && my <= y + height;
    }

    private void renderTechBackground(GuiGraphics guiGraphics, ResourceLocation texture, int textureWidth, int textureHeight, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        guiGraphics.blit(texture, x, y, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
    }

    private void renderTechName(GuiGraphics guiGraphics, Technology tech, int x, int y, float scale) {
        String name = tech.getDisplayName().getString();
        int nameY = Math.round(y + this.font.lineHeight * scale / 2);
        int nameX = Math.round(x - this.font.width(name) * scale / 2);
        drawString(guiGraphics, name, nameX, nameY, 0, scale);
    }

    private void renderFlaskField(GuiGraphics guiGraphics, int x, int y, Technology tech) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x + 3, y + 30, 0);
        guiGraphics.pose().scale(0.7f, 0.7f, 1f);
        guiGraphics.blit(FLASK_FIELD, 0, 0, 0, 0, 108, 36, 108, 36);

        int flaskCount = 0;
        for (ItemStack baseCost : tech.getCost()) {
            String text = String.valueOf(baseCost.getCount());
            int renderX = 1 + (flaskCount % 6) * 18;
            int renderY = flaskCount < 6 ? 1 : 19;
            renderItem(guiGraphics, baseCost, renderX, renderY, 1);
            drawString(guiGraphics, text,
                    Math.round((flaskCount % 6 + 1) * 18 - this.font.width(text) * 0.75f) - 1,
                    renderY + 17 - Math.round(this.font.lineHeight * 0.75f),
                    300, 0.75f);
            flaskCount++;
        }

        new Clock(guiGraphics, 128, 18, tech.getTime()).render();
        drawString(guiGraphics, "x " + tech.getCount(), 152, 18 - this.font.lineHeight / 2, 0, 1);
        guiGraphics.pose().popPose();
    }

    private void renderItem(GuiGraphics guiGraphics, ItemStack itemStack, int dx, int dy, float scale) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(dx, dy, 0);
        guiGraphics.pose().scale(scale, scale, 1f);
        guiGraphics.renderItem(itemStack, 0, 0);
        guiGraphics.pose().popPose();
    }

    private void drawString(GuiGraphics guiGraphics, String text, int dx, int dy, int dz, float scale) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(dx, dy, dz);
        guiGraphics.pose().scale(scale, scale, 1f);
        guiGraphics.drawString(this.font, text, 0, 0, 0x000000, false);
        guiGraphics.pose().popPose();
    }

    private void drawString(GuiGraphics guiGraphics, MutableComponent text, int dx, int dy, int dz, float scale) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(dx, dy, dz);
        guiGraphics.pose().scale(scale, scale, 1f);
        guiGraphics.drawString(this.font, text, 0, 0, 0x000000, false);
        guiGraphics.pose().popPose();
    }

    private void drawString(GuiGraphics guiGraphics, FormattedCharSequence text, int dx, int dy, int dz, float scale) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(dx, dy, dz);
        guiGraphics.pose().scale(scale, scale, 1f);
        guiGraphics.drawString(this.font, text, 0, 0, 0x000000, false);
        guiGraphics.pose().popPose();
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

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        float adjustedX = (float) ((mouseX - offsetX) / zoom);
        float adjustedY = (float) ((mouseY - offsetY) / zoom);

        for (Technology tech : TechnologyRegistry.getAll()) {
            if (isMouseOverNode(adjustedX, adjustedY, tech.getX(), tech.getY(), 128, 60)) {
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
        if (button == 0) {
            this.techDetails = tech;
        }
        //System.out.println("Clicked on: " + tech.getId() + " with button " + button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        float oldZoom = zoom;
        zoom = Math.max(0.25f, Math.min(zoom + (float) delta * 0.1f, 3.0f));
        float ratio = zoom / oldZoom;
        offsetX = (float) (mouseX + (offsetX - mouseX) * ratio);
        offsetY = (float) (mouseY + (offsetY - mouseY) * ratio);
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
}