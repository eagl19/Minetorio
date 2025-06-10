package net.eagl.minetorio.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.eagl.minetorio.gui.ResearcherMenu;
import net.eagl.minetorio.util.Clock;
import net.eagl.minetorio.util.FlasksField;
import net.eagl.minetorio.util.Technology;
import net.eagl.minetorio.util.TechnologyRegistry;
import net.eagl.minetorio.util.enums.FlaskColor;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TechnologyTreeScreen extends Screen {
    private static final ResourceLocation NODE_TEXTURE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/tech.png");
    private static final int NODE_TEXTURE_WIDTH = 128;
    private static final int NODE_TEXTURE_HEIGHT = 60;

    private static final ResourceLocation FLASK_FIELD = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/tech_flask_field.png");
    private static final ResourceLocation TECH_DETAILS_TEXTURE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/tech_details.png");
    private static final int TECH_DETAILS_TEXTURE_WIDTH = 289;
    private static final int TECH_DETAILS_TEXTURE_HEIGHT = 326;

    private static final ResourceLocation BUTTON_OK_TEXTURE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/button_ok.png");
    private static final ResourceLocation BUTTON_CANCEL_TEXTURE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/button_cancel.png");
    private static final int BUTTON_TEXTURE_WIDTH = 358;
    private static final int BUTTON_TEXTURE_HEIGHT = 219;
    float okX;
    float okY;
    float cancelX;
    float cancelY;

    float btnWidth;
    float btnHeight;

    private float zoom;
    private float offsetX, offsetY;
    private double lastMouseX, lastMouseY;
    private boolean dragging = false;

    private final int techIndex;

    private Technology techDetails;

    private final boolean isSee;
    private final ResearcherMenu menu;
    private final Inventory playerInventory;
    private final Component researcherTitle;

    private  boolean canLearn;

    public TechnologyTreeScreen(ResearcherMenu pMenu, Inventory pPlayerInventory, Component pTitle, Technology tech, int pCurrentTech) {
        super(Component.literal("Technology Tree"));
        this.zoom = 1.0f;
        this.techDetails = tech;
        this.techIndex = pCurrentTech;
        this.menu = pMenu;
        this.playerInventory = pPlayerInventory;
        this.researcherTitle = pTitle;
        canLearn = false;
        isSee = !tech.equals(Technology.EMPTY);

    }


    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        List<Component> tooltip = null;
        if (!techDetails.equals(Technology.EMPTY)) {

            String tip = renderTechnologyDetails(guiGraphics, mouseX, mouseY);
            if (!tip.isEmpty()) {
                tooltip = List.of(Component.literal(tip));
            }

        } else {

            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(offsetX, offsetY, 0);
            guiGraphics.pose().scale(zoom, zoom, 1f);

            float adjustedMouseX = (mouseX - offsetX) / zoom;
            float adjustedMouseY = (mouseY - offsetY) / zoom;

            renderConnections(guiGraphics);


            for (Technology tech : TechnologyRegistry.getAll()) {
                List<Component> result = renderTechnology(guiGraphics, tech, adjustedMouseX, adjustedMouseY);
                if (result != null) tooltip = result;
            }

            guiGraphics.pose().popPose();


        }

        if (tooltip != null) {
            guiGraphics.renderComponentTooltip(this.font, tooltip, mouseX, mouseY);
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private String renderTechnologyDetails(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {


        float scale = (float) this.height / TECH_DETAILS_TEXTURE_HEIGHT;

        float x = (this.width - TECH_DETAILS_TEXTURE_WIDTH * scale) / 2;
        float y = (this.height - TECH_DETAILS_TEXTURE_HEIGHT * scale) / 2;

        guiGraphics.pose().pushPose();

        float adjustedMouseX = (mouseX - x) / scale;
        float adjustedMouseY = (mouseY - y) / scale;

        String tooltip;

        guiGraphics.pose().translate(x, y,0.0f);
        guiGraphics.pose().scale(scale, scale, 1.0f);

        guiGraphics.pose().pushPose();
        renderTechBackground(guiGraphics, TECH_DETAILS_TEXTURE, TECH_DETAILS_TEXTURE_WIDTH, TECH_DETAILS_TEXTURE_HEIGHT,0,0);

        renderTechName(guiGraphics, techDetails, TECH_DETAILS_TEXTURE_WIDTH / 2, 40, 2);

        renderItem(guiGraphics, new ItemStack(techDetails.getDisplayIcon()), 30, 75, 3);

        renderBenefit(guiGraphics);

        drawString(guiGraphics, techDetails.getTotalTimeAsString(),30, 130);

        tooltip = renderCost(guiGraphics, adjustedMouseX, adjustedMouseY);



        float scale_button = (float) 15 / BUTTON_TEXTURE_HEIGHT;

        okX = TECH_DETAILS_TEXTURE_WIDTH - 50 - BUTTON_TEXTURE_WIDTH * scale_button * 2;
        okY = TECH_DETAILS_TEXTURE_HEIGHT - 70;
        cancelX = TECH_DETAILS_TEXTURE_WIDTH - 40 - BUTTON_TEXTURE_WIDTH * scale_button;
        cancelY = TECH_DETAILS_TEXTURE_HEIGHT - 70;

        if(canLearn || isSee) {
            renderButton(guiGraphics, BUTTON_OK_TEXTURE, okX, okY, scale_button);
        }
        if(!isSee) {
            renderButton(guiGraphics, BUTTON_CANCEL_TEXTURE, cancelX, cancelY, scale_button);
        }


        okX = x + (TECH_DETAILS_TEXTURE_WIDTH - 50 - BUTTON_TEXTURE_WIDTH * scale_button * 2) * scale;
        okY = y + (TECH_DETAILS_TEXTURE_HEIGHT - 70) * scale;
        cancelX = x + (TECH_DETAILS_TEXTURE_WIDTH - 40 - BUTTON_TEXTURE_WIDTH * scale_button) * scale;
        cancelY = y + (TECH_DETAILS_TEXTURE_HEIGHT - 70) * scale;

        btnWidth = BUTTON_TEXTURE_WIDTH * scale_button * scale;
        btnHeight = BUTTON_TEXTURE_HEIGHT * scale_button * scale;

        guiGraphics.pose().popPose();
        guiGraphics.pose().popPose();

        return tooltip;
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

    private String renderCost(GuiGraphics guiGraphics, float mouseX, float mouseY) {
        int x = 30;
        int y = 140;
        float scale = 1.0f;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y,0);
        guiGraphics.pose().scale(scale,scale,0);


        String tooltip = "";
        float adjustedMouseX = (mouseX - x) / scale;
        float adjustedMouseY = (mouseY - y) / scale;


        drawString(guiGraphics,
                Component.translatable("tooltip.minetorio.cost").append(" : ")
                        .withStyle(ChatFormatting.DARK_RED),
                0, 0);

        FlasksField cost = techDetails.getCost();
        if (cost != null && !cost.equals(FlasksField.EMPTY)) {
            int rendered = 0;
            String tip;

            for (Map.Entry<FlaskColor, Integer> entry : cost.getAll().entrySet()) {
                int amount = entry.getValue();
                if (amount <= 0) continue;

                int row = rendered / 2;
                int dx = (rendered % 2 == 0) ? 2 : TECH_DETAILS_TEXTURE_WIDTH / 2 - 25;
                int dy = row * 12 + 12;
                int maxWidth = (rendered % 2 == 0)
                        ? TECH_DETAILS_TEXTURE_WIDTH - 80
                        : TECH_DETAILS_TEXTURE_WIDTH / 2 - 40;

                ItemStack stack = cost.getFlask(entry.getKey());
                stack.setCount(amount);

                tip = renderCostItem(guiGraphics, stack, dx, dy, maxWidth, adjustedMouseX, adjustedMouseY);
                if (!tip.isEmpty()) {
                    tooltip = tip;
                }

                rendered++;
            }
        }

        guiGraphics.pose().popPose();

        return tooltip;
    }

    private String renderCostItem(GuiGraphics guiGraphics, ItemStack stack, int x, int y, int maxWidth, float mouseX, float mouseY) {
        String tooltip = "";
        String baseText = "x" + stack.getCount() * techDetails.getCount() + " " +
                stack.getDisplayName().getString().replace("[", "").replace("]", "");
        String trimmedText = trimmedText(baseText, maxWidth);
        if (!trimmedText.equals(baseText)) {
            if (isMouseOver(mouseX, mouseY, x + 4, y - 4, maxWidth, 10)) {
                tooltip = baseText;
            }
        }
        renderItem(guiGraphics, stack, x, y - 4, 1);
        drawString(guiGraphics, trimmedText ,x + 14, y,0,1);
        return tooltip;
    }

    private  String trimmedText(String text, int maxWidth){

        String trimmedText = font.plainSubstrByWidth(text, maxWidth);
        if (!trimmedText.equals(text)) {
            trimmedText = font.plainSubstrByWidth(text, maxWidth - font.width("...")) + "...";
        }
        return trimmedText;
    }

    private void renderBenefit(GuiGraphics guiGraphics){
        int x = 83;
        int y = 75;
        float scale = 0.9f;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x,y,0);
        guiGraphics.pose().scale(scale,scale,0);
        int height;
        List<FormattedCharSequence> lines = this.font.split(techDetails.getBenefit(), (int) ((TECH_DETAILS_TEXTURE_WIDTH - x - 35) / scale));
        for (int i = 0; i < lines.size(); i++) {
            height = Math.round(i * (font.lineHeight * scale + 3));
            drawString(guiGraphics, lines.get(i), height);
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
                            y2 + NODE_TEXTURE_HEIGHT / 2
                    );
                }
            }
        }
    }

    private List<Component> renderTechnology(GuiGraphics guiGraphics, Technology tech, float mouseX, float mouseY) {
        int x = tech.getX();
        int y = tech.getY();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, NODE_TEXTURE);
        if(tech.isLearn()) {
            RenderSystem.setShaderColor(0.5f, 1.0f, 0.5f, 1.0f);
        }
        else if(tech.canLearn(menu.getTechList())){
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
        else{
            RenderSystem.setShaderColor(1.0f, 0.5f, 0.5f, 1.0f);
        }

        renderTechBackground(guiGraphics, NODE_TEXTURE, NODE_TEXTURE_WIDTH, NODE_TEXTURE_HEIGHT, x, y);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        renderItem(guiGraphics, new ItemStack(tech.getDisplayIcon()), x + 3, y + 3, 1.5f);
        renderTechName(guiGraphics, tech, x + 77, y + 1, 1);
        renderFlaskField(guiGraphics, x, y, tech);

        if (isMouseOver(mouseX, mouseY, x, y, NODE_TEXTURE_WIDTH, NODE_TEXTURE_HEIGHT)) {
            return List.of(
                    tech.getDisplayName(),
                    Component.literal("Cost: " + tech.getFormattedCost().getString())
            );
        }
        return null;
    }

    private boolean isMouseOver(float mx, float my, int x, int y, int width, int height) {
        return mx >= x && mx <= x + width && my >= y && my <= y + height;
    }

    private void renderTechBackground(GuiGraphics guiGraphics, ResourceLocation texture, int textureWidth, int textureHeight, int x, int y) {

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
        FlasksField flasksField = tech.getCost();
        for (Map.Entry<FlaskColor, Integer> entry : flasksField.getAll().entrySet()) {
            int amount = entry.getValue();
            if (amount <= 0) continue;

            FlaskColor color = entry.getKey();
            ItemStack flaskStack = flasksField.getFlask(color);
            flaskStack.setCount(amount);

            int renderX = 1 + (flaskCount % 6) * 18;
            int renderY = flaskCount < 6 ? 1 : 19;

            renderItem(guiGraphics, flaskStack, renderX, renderY, 1);

            String text = String.valueOf(amount);
            drawString(guiGraphics, text,
                    Math.round((flaskCount % 6 + 1) * 18 - this.font.width(text) * 0.75f) - 1,
                    renderY + 17 - Math.round(this.font.lineHeight * 0.75f),
                    300, 0.75f
            );

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

    private void drawString(GuiGraphics guiGraphics, MutableComponent text, int dx, int dy) {

        int dz = 0;
        float scale = 1;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(dx, dy, dz);
        guiGraphics.pose().scale(scale, scale, 1f);
        guiGraphics.drawString(this.font, text, 0, 0, 0x000000, false);
        guiGraphics.pose().popPose();
    }

    private void drawString(GuiGraphics guiGraphics, FormattedCharSequence text, int dy) {
        int dx = 0;
        int dz = 0;
        float scale = 1;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(dx, dy, dz);
        guiGraphics.pose().scale(scale, scale, 1f);
        guiGraphics.drawString(this.font, text, 0, 0, 0x000000, false);
        guiGraphics.pose().popPose();
    }

    private void drawConnectionLine(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2) {
        int color = 0xFFFFFFFF;
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
        if(techDetails.equals(Technology.EMPTY)) {
            float adjustedX = (float) ((mouseX - offsetX) / zoom);
            float adjustedY = (float) ((mouseY - offsetY) / zoom);

            for (Technology tech : TechnologyRegistry.getAll()) {
                if (isMouseOver(adjustedX, adjustedY, tech.getX(), tech.getY(), 128, 60)) {
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
        }else{
            if(canLearn || isSee) {
                if (isMouseOver((float) mouseX, (float) mouseY, (int) okX, (int) okY, (int) btnWidth, (int) btnHeight)) {
                    onOkButtonClicked();
                    return true;
                }
            }

            if(!isSee) {
                if (isMouseOver((float) mouseX, (float) mouseY, (int) cancelX, (int) cancelY, (int) btnWidth, (int) btnHeight)) {
                    onCancelButtonClicked();
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void onCancelButtonClicked() {

        techDetails = Technology.EMPTY;
    }

    private void onOkButtonClicked() {
        if(!isSee) {
            List<Technology> list = new ArrayList<>(menu.getTechList());
            list.set(techIndex,techDetails);
            list.sort((a, b) -> {
                if (a == Technology.EMPTY && b != Technology.EMPTY) return 1;
                if (a != Technology.EMPTY && b == Technology.EMPTY) return -1;
                return 0;
            });
            menu.setTechList(list);
        }
        Minecraft.getInstance().setScreen(new ResearcherScreen(menu, playerInventory, researcherTitle));
    }

    private void onTechnologyClicked(Technology tech, int button) {

        if (button == 0) {
            this.techDetails = tech;

            canLearn = !menu.getTechList().contains(techDetails) && techDetails.canLearn(menu.getTechList());
        }
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

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if(!isSee && !this.techDetails.equals(Technology.EMPTY)) {
            if (pKeyCode == GLFW.GLFW_KEY_ESCAPE) {
                this.techDetails = Technology.EMPTY;
                return true;
            }
        }else{
            Minecraft.getInstance().setScreen(new ResearcherScreen(menu, playerInventory, researcherTitle));
            return true;
        }

        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }
}