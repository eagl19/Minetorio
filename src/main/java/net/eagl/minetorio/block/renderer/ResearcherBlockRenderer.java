package net.eagl.minetorio.block.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.eagl.minetorio.block.entity.ResearcherBlockEntity;
import net.eagl.minetorio.item.MinetorioItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ResearcherBlockRenderer implements BlockEntityRenderer<ResearcherBlockEntity> {

    @SuppressWarnings("unused")
    public ResearcherBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(@NotNull ResearcherBlockEntity pBlockEntity, float pPartialTick, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {

        ItemStack stack =  pBlockEntity.getRendererItemStack();
        if(stack != null && !stack.isEmpty() && !stack.getItem().equals(MinetorioItems.PATTERN_EMPTY.get())) {
            poseStack.pushPose();


            poseStack.translate(0.5, 3.5, 0.5);
            poseStack.scale(2.0f, 2.0f, 2.0f);
            float yaw = Minecraft.getInstance().gameRenderer.getMainCamera().getYRot();
            poseStack.mulPose(Axis.YN.rotationDegrees(yaw));

            Minecraft.getInstance().getItemRenderer().renderStatic(
                    stack,
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
}
