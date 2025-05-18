package net.eagl.minetorio.block.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.entity.PatternsCollectorBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PatternCollectorBlockRenderer implements BlockEntityRenderer<PatternsCollectorBlockEntity> {

    @SuppressWarnings("unused")
        public PatternCollectorBlockRenderer(BlockEntityRendererProvider.Context context) {}

        @Override
        public void render(PatternsCollectorBlockEntity blockEntity, float partialTicks, PoseStack poseStack,
                           @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {

            poseStack.pushPose();


            poseStack.translate(0.5, 0.5, 0.5);

            poseStack.scale(2.0f, 2.0f, 2.0f);

            float baseRotation = blockEntity.getRotation() + partialTicks;

            poseStack.mulPose(Axis.XP.rotationDegrees((baseRotation * blockEntity.getSpeedX()) % 360.0f));
            poseStack.mulPose(Axis.YP.rotationDegrees((baseRotation * blockEntity.getSpeedY()) % 360.0f));
            poseStack.mulPose(Axis.ZP.rotationDegrees((baseRotation * blockEntity.getSpeedZ()) % 360.0f));


            Minecraft.getInstance().getItemRenderer().renderStatic(
                    new ItemStack(MinetorioBlocks.PATTERNS_COLLECTOR.get().asItem()),
                    ItemDisplayContext.FIXED,
                    packedLight,
                    packedOverlay,
                    poseStack,
                    buffer,
                    null,
                    0
            );

            poseStack.popPose();
        }


}
