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
        public void render(@NotNull PatternsCollectorBlockEntity blockEntity, float partialTicks, PoseStack poseStack,
                           @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {

            poseStack.pushPose();

            float currentYOffset = getCurrentYOffset(blockEntity);

            blockEntity.setCurrentYOffset(currentYOffset);

            // Транслюємо з урахуванням currentYOffset
            poseStack.translate(0.5, 0.5 - currentYOffset, 0.5);

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

    private static float getCurrentYOffset(@NotNull PatternsCollectorBlockEntity blockEntity) {
        Minecraft mc = Minecraft.getInstance();

        float targetYOffset = -10f;

        if (mc.player != null) {
            double blockX = blockEntity.getBlockPos().getX() + 0.5;
            double blockY = blockEntity.getBlockPos().getY() + 0.5;
            double blockZ = blockEntity.getBlockPos().getZ() + 0.5;

            double playerX = mc.player.getX();
            double playerY = mc.player.getY() + mc.player.getEyeHeight();
            double playerZ = mc.player.getZ();

            double dx = Math.abs(blockX - playerX);
            double dz = Math.abs(blockZ - playerZ);

            boolean isUnder = dx < 1.0 && dz < 1.0 && playerY < blockY;

            if (isUnder) {
                targetYOffset = (float) ((blockY) - (playerY + 1.0));
                if (targetYOffset < 0) targetYOffset = 0;
            }
        }

        float currentYOffset = blockEntity.getCurrentYOffset();

        float smoothing = 0.1f; // швидкість плавності (0.1 - повільно, 1 - миттєво)
        currentYOffset += (targetYOffset - currentYOffset) * smoothing;
        return currentYOffset;
    }


}
