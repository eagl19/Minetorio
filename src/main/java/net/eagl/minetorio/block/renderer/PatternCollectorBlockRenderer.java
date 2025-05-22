package net.eagl.minetorio.block.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.eagl.minetorio.block.entity.PatternsCollectorBlockEntity;
import net.eagl.minetorio.item.MinetorioItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PatternCollectorBlockRenderer implements BlockEntityRenderer<PatternsCollectorBlockEntity> {


    private static final Map<Direction, ItemStack> PATTERN_ITEMS_MAIN = Map.of(
            Direction.UP,    new ItemStack(MinetorioItems.PATTERN_INFINITY.get()),
            Direction.DOWN,  new ItemStack(MinetorioItems.PATTERN_VOID.get()),
            Direction.NORTH, new ItemStack(MinetorioItems.PATTERN_AIR.get()),
            Direction.SOUTH, new ItemStack(MinetorioItems.PATTERN_FIRE.get()),
            Direction.WEST,  new ItemStack(MinetorioItems.PATTERN_EARTH.get()),
            Direction.EAST,  new ItemStack(MinetorioItems.PATTERN_WATER.get())
    );

    private static final Map<Direction, ItemStack> PATTERN_ITEMS_EW = Map.of(
            Direction.UP,    new ItemStack(MinetorioItems.PATTERN_CLOUD.get()),
            Direction.DOWN,  new ItemStack(MinetorioItems.PATTERN_CLOUD.get()),
            Direction.NORTH, new ItemStack(MinetorioItems.PATTERN_CLOUD.get()),
            Direction.SOUTH, new ItemStack(MinetorioItems.PATTERN_CLOUD.get()),
            Direction.WEST,  ItemStack.EMPTY,
            Direction.EAST,  ItemStack.EMPTY
    );

    private static final Map<Direction, ItemStack> PATTERN_ITEMS_UD = Map.of(
            Direction.UP,    ItemStack.EMPTY,
            Direction.DOWN,  ItemStack.EMPTY,
            Direction.NORTH, new ItemStack(MinetorioItems.PATTERN_AIR.get()),
            Direction.SOUTH, new ItemStack(MinetorioItems.PATTERN_FIRE.get()),
            Direction.WEST,  new ItemStack(MinetorioItems.PATTERN_EARTH.get()),
            Direction.EAST,  new ItemStack(MinetorioItems.PATTERN_WATER.get())
    );

    public float ringYOffset = 0.0f;

    public static ItemStack getPatternItem(Map<Direction, ItemStack> itemsMap,Direction direction) {
        return itemsMap.getOrDefault(direction, ItemStack.EMPTY);
    }

    @SuppressWarnings("unused")
    public PatternCollectorBlockRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(@NotNull PatternsCollectorBlockEntity blockEntity, float partialTicks, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {


        float offset = 7.0f;

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5+ offset, 0.5);


        ringYOffset = getCurrentYOffset(blockEntity, ringYOffset, offset);

        blockEntity.setCurrentOffset(ringYOffset);
        float baseRotation = blockEntity.getRotation() + partialTicks;

        float rotationX = (baseRotation * blockEntity.getSpeedX()) % 360.0f;
        float rotationY = (baseRotation * blockEntity.getSpeedY()) % 360.0f;
        float rotationZ = (baseRotation * blockEntity.getSpeedZ()) % 360.0f;

        renderScene(poseStack, buffer, packedLight, packedOverlay, rotationX, rotationY,rotationZ);


        poseStack.popPose();

    }

    private void renderScene (PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, float rotationX, float rotationY, float rotationZ){

        poseStack.pushPose();

        poseStack.pushPose();
        poseStack.translate(0, ringYOffset, 0);
        renderRing(poseStack, buffer, packedLight, packedOverlay, PATTERN_ITEMS_MAIN, rotationX, rotationY, rotationZ, 1,  0.5);
        renderRing(poseStack, buffer, packedLight, packedOverlay, PATTERN_ITEMS_MAIN, -rotationX, -rotationY, -rotationZ, 1,  1);
        poseStack.popPose();

        poseStack.mulPose(Axis.XP.rotationDegrees(rotationX % 360.0f));
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationY % 360.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotationZ % 360.0f));
        renderSphere(poseStack, buffer, packedLight, packedOverlay);

        poseStack.popPose();

    }

    private void renderSphere(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        double radius = 14;

        poseStack.pushPose();
        renderRing(poseStack, buffer, packedLight, packedOverlay, PATTERN_ITEMS_MAIN,0,0,0, 4f, radius);
        renderRing(poseStack, buffer, packedLight, packedOverlay, PATTERN_ITEMS_UD,0,45,0, 4f, radius);
        poseStack.pushPose();
        renderRing(poseStack, buffer, packedLight, packedOverlay, PATTERN_ITEMS_EW,45,0,0, 4f, radius);
        poseStack.popPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(45));
        poseStack.pushPose();
        renderRing(poseStack, buffer, packedLight, packedOverlay, PATTERN_ITEMS_EW,45,0,0, 4f, radius);
        poseStack.popPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(45));
        poseStack.pushPose();
        renderRing(poseStack, buffer, packedLight, packedOverlay, PATTERN_ITEMS_EW,45,0,0, 4f, radius);
        poseStack.popPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(45));
        poseStack.pushPose();
        renderRing(poseStack, buffer, packedLight, packedOverlay, PATTERN_ITEMS_EW,45,0,0, 4f, radius);
        poseStack.popPose();
        poseStack.popPose();
    }


    private void renderRing(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay,
                            Map<Direction, ItemStack> itemMap, float rotationX, float rotationY, float rotationZ, float scale, double radius) {

        poseStack.pushPose();

        poseStack.mulPose(Axis.XP.rotationDegrees(rotationX % 360.0f));
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationY % 360.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotationZ % 360.0f));

        for (Direction direction : Direction.values()) {
            ItemStack stack = getPatternItem(itemMap, direction);
            if (stack.isEmpty()) continue;

            poseStack.pushPose();

            double dx = direction.getStepX() * radius;
            double dy = direction.getStepY() * radius;
            double dz = direction.getStepZ() * radius;

            poseStack.translate(dx, dy, dz);

            switch (direction) {
                case UP -> poseStack.mulPose(Axis.XP.rotationDegrees(90));
                case DOWN -> poseStack.mulPose(Axis.XN.rotationDegrees(90));
                case NORTH, SOUTH -> {}
                case WEST -> poseStack.mulPose(Axis.YP.rotationDegrees(90));
                case EAST -> poseStack.mulPose(Axis.YN.rotationDegrees(90));
            }

            poseStack.scale(scale, scale, 0.1f);


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
        poseStack.popPose();
    }

    private static float getCurrentYOffset(@NotNull PatternsCollectorBlockEntity blockEntity, float previousYOffset, float offset) {
        Minecraft mc = Minecraft.getInstance();

        float targetYOffset = 0f;

        if (mc.player != null) {
            double blockX = blockEntity.getBlockPos().getX() + 0.5;
            double blockY = blockEntity.getBlockPos().getY() + 0.5;
            double blockZ = blockEntity.getBlockPos().getZ() + 0.5;

            double playerX = mc.player.getX();
            double playerY = mc.player.getY() + mc.player.getEyeHeight(); // очі
            double playerZ = mc.player.getZ();

            double dx = blockX - playerX;
            double dy = blockY - playerY;
            double dz = blockZ - playerZ;

            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

            if (distance < 3.0) {
                targetYOffset = -offset;
            }
        }

        float smoothing = 0.1f;
        return previousYOffset + (targetYOffset - previousYOffset) * smoothing;
    }
}