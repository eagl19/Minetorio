package net.eagl.minetorio.block.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.eagl.minetorio.block.entity.ResearcherBlockEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class ResearcherBlockRenderer implements BlockEntityRenderer<ResearcherBlockEntity> {

    @SuppressWarnings("unused")
    public ResearcherBlockRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(@NotNull ResearcherBlockEntity pBlockEntity, float pPartialTick, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {

        poseStack.pushPose();


        poseStack.translate(0.5, 2.25, 0.5);
        poseStack.scale(2.0f, 2.0f, 2.0f);


        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        poseStack.mulPose(camera.rotation());
        poseStack.mulPose(Axis.XN.rotationDegrees(90));

        float size = 0.5f;
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath("minetorio", "textures/gui/clock.png");
        RenderType renderType = RenderType.text(texture);
        VertexConsumer consumer = buffer.getBuffer(renderType);
        Matrix4f matrix = poseStack.last().pose();

        consumer.vertex(matrix, -size, 0, -size).color(255, 255, 255, 255)
                .uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight)
                .normal(poseStack.last().normal(), 0, 0, -1).endVertex();
        consumer.vertex(matrix, -size, 0, size).color(255, 255, 255, 255)
                .uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight)
                .normal(poseStack.last().normal(), 0, 0, -1).endVertex();
        consumer.vertex(matrix, size, 0, size).color(255, 255, 255, 255)
                .uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight)
                .normal(poseStack.last().normal(), 0, 0, -1).endVertex();
        consumer.vertex(matrix, size, 0, -size).color(255, 255, 255, 255)
                .uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight)
                .normal(poseStack.last().normal(), 0, 0, -1).endVertex();

        poseStack.popPose();
    }
}
