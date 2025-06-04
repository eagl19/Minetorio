package net.eagl.minetorio.util;

import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

/**
 * Рендерить аналоговий годинник у GUI.
 */
public class Clock {

    private static final ResourceLocation TEXTURE_BASE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/clock.png");
    private static final ResourceLocation TEXTURE_HOUR = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/clock_hour.png");
    private static final ResourceLocation TEXTURE_MINUTE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/clock_min.png");
    private static final ResourceLocation TEXTURE_SECOND = TEXTURE_MINUTE; // заміни, якщо буде інша текстура

    // Розміри текстур годинника
    private static final int BASE_SIZE = 2700;
    private static final float BASE_SCALE = 0.015f;

    // Розміри текстур секундної стрілки
    private static final int SECOND_WIDTH = 136, SECOND_HEIGHT = 741;
    private static final int SECOND_OFFSET_X = -68, SECOND_OFFSET_Y = -452;

    // Розміри текстур хвилинної стрілки
    private static final int MINUTE_WIDTH = 136, MINUTE_HEIGHT = 741;
    private static final int MINUTE_OFFSET_X = -68, MINUTE_OFFSET_Y = -452;

    // Розміри текстур годинної стрілки
    private static final int HOUR_WIDTH = 176, HOUR_HEIGHT = 505;
    private static final int HOUR_OFFSET_X = -109, HOUR_OFFSET_Y = -382;

    private static final float SECOND_SCALE = 1.5f;
    private static final float MINUTE_SCALE = 2.0f;
    private static final float HOUR_SCALE = 2.0f;

    private final GuiGraphics guiGraphics;
    private final int centerX;
    private final int centerY;
    private final int time; // тики

    public Clock(GuiGraphics guiGraphics, int centerX, int centerY, int time) {
        this.guiGraphics = guiGraphics;
        this.centerX = centerX;
        this.centerY = centerY;
        this.time = time;
    }

    /** Повертає повну кількість секунд. */
    public long getTotalSeconds() {
        return time / 20;
    }

    /** Повертає секунди у time. */
    public long getSeconds() {
        return getTotalSeconds() % 60;
    }

    /** Повертає хвилини у time. */
    public long getMinutes() {
        return (getTotalSeconds() / 60) % 60;
    }

    /** Повертає години у time */
    public long getHours() {
        return (getTotalSeconds() / 3600) % 12;
    }

    /** Кут повороту секундної стрілки. */
    public float getSecondsDegree() {
        return getSeconds() * 6f + ((time % 20) / 20f) * 6f;
    }

    /** Кут повороту хвилинної стрілки. */
    public float getMinutesDegree() {
        return getMinutes() * 6f + (getSeconds() / 60f) * 6f;
    }

    /** Кут повороту годинникової стрілки. */
    public float getHourDegree() {
        return getHours() * 30f + (getMinutes() / 60f) * 30f;
    }

    /** Рендерить повний годинник. */
    public void render() {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(centerX, centerY, 0);
        guiGraphics.pose().scale(BASE_SCALE, BASE_SCALE, 1f);
        guiGraphics.blit(TEXTURE_BASE, -BASE_SIZE / 2, -BASE_SIZE / 2, 0, 0, BASE_SIZE, BASE_SIZE, BASE_SIZE, BASE_SIZE);

        renderHand(TEXTURE_SECOND,  SECOND_WIDTH,   SECOND_HEIGHT,  SECOND_OFFSET_X,    SECOND_OFFSET_Y,    getSecondsDegree(), SECOND_SCALE);
        renderHand(TEXTURE_MINUTE,  MINUTE_WIDTH,   MINUTE_HEIGHT,  MINUTE_OFFSET_X,    MINUTE_OFFSET_Y,    getMinutesDegree(), MINUTE_SCALE);
        renderHand(TEXTURE_HOUR,    HOUR_WIDTH,     HOUR_HEIGHT,    HOUR_OFFSET_X,      HOUR_OFFSET_Y,      getHourDegree(),    HOUR_SCALE);

        guiGraphics.pose().popPose();
    }

    /**
     * Рендерить окрему стрілку годинника.
     */
    private void renderHand(ResourceLocation texture, int width, int height, int dx, int dy, float degree, float scale) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scale, scale, 1f);
        guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(degree));
        guiGraphics.blit(texture, dx, dy, 0, 0, width, height, width, height);
        guiGraphics.pose().popPose();
    }
}
