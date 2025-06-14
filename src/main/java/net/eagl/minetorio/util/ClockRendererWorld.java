package net.eagl.minetorio.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class ClockRendererWorld {

    private static final ResourceLocation TEXTURE_BASE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/clock.png");
    private static final ResourceLocation TEXTURE_HOUR = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/clock_hour.png");
    private static final ResourceLocation TEXTURE_MINUTE = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/clock_min.png");
    private static final ResourceLocation TEXTURE_SECOND = TEXTURE_MINUTE; // або окрема

    private static final float BASE_SIZE = 1.0f;
    private static final float BASE_HALF = BASE_SIZE / 2f;

    public static void render(PoseStack poseStack, MultiBufferSource buffer, int light, long gameTime) {
        poseStack.pushPose();

        // Малюємо основу годинника
        renderQuad(buffer, poseStack, TEXTURE_BASE, BASE_SIZE, light);

        // Обчислення часу
        long time = gameTime % (20 * 60 * 60); // 1 година = 72000 тік
        float secondDeg = (time % 1200) * 0.3f;                    // 6 град/сек
        float minuteDeg = (((float) time / 20) % 3600) / 60f * 6f;
        float hourDeg   = (((float) time / 72000) % 12) * 30f + (((float) time / 3600) % 60) * 0.5f;

        // Рендеримо стрілки
        renderHand(poseStack, buffer, TEXTURE_HOUR, hourDeg, light, 0.04f, 0.25f);
        renderHand(poseStack, buffer, TEXTURE_MINUTE, minuteDeg, light, 0.03f, 0.4f);
        renderHand(poseStack, buffer, TEXTURE_SECOND, secondDeg, light, 0.02f, 0.45f);

        poseStack.popPose();
    }

    private static void renderQuad(MultiBufferSource buffer, PoseStack poseStack, ResourceLocation texture, float size, int light) {
        RenderType renderType = RenderType.text(texture);
        VertexConsumer consumer = buffer.getBuffer(renderType);
        Matrix4f matrix = poseStack.last().pose();

        float half = size / 2f;

        consumer.vertex(matrix, -half, 0, -half).color(255, 255, 255, 255)
                .uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0, 1, 0).endVertex();
        consumer.vertex(matrix, -half, 0, half).color(255, 255, 255, 255)
                .uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0, 1, 0).endVertex();
        consumer.vertex(matrix, half, 0, half).color(255, 255, 255, 255)
                .uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0, 1, 0).endVertex();
        consumer.vertex(matrix, half, 0, -half).color(255, 255, 255, 255)
                .uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0, 1, 0).endVertex();
    }

    private static void renderHand(PoseStack poseStack, MultiBufferSource buffer, ResourceLocation texture, float angleDeg, int light, float width, float length) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.ZP.rotationDegrees(angleDeg));
        RenderType renderType = RenderType.text(texture);
        VertexConsumer consumer = buffer.getBuffer(renderType);
        Matrix4f matrix = poseStack.last().pose();

        float w = width;
        float h = length;

        consumer.vertex(matrix, -w, 0, 0).color(255, 255, 255, 255).uv(0, 1).uv2(light).overlayCoords(OverlayTexture.NO_OVERLAY).normal(0, 1, 0).endVertex();
        consumer.vertex(matrix, w, 0, 0).color(255, 255, 255, 255).uv(1, 1).uv2(light).overlayCoords(OverlayTexture.NO_OVERLAY).normal(0, 1, 0).endVertex();
        consumer.vertex(matrix, w, h, 0).color(255, 255, 255, 255).uv(1, 0).uv2(light).overlayCoords(OverlayTexture.NO_OVERLAY).normal(0, 1, 0).endVertex();
        consumer.vertex(matrix, -w, h, 0).color(255, 255, 255, 255).uv(0, 0).uv2(light).overlayCoords(OverlayTexture.NO_OVERLAY).normal(0, 1, 0).endVertex();

        poseStack.popPose();
    }
}

