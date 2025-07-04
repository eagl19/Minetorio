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

import java.util.EnumMap;
import java.util.Map;
import java.util.*;

public class PatternCollectorBlockRenderer implements BlockEntityRenderer<PatternsCollectorBlockEntity> {

    private static final Map<Direction, ItemStack> PATTERN_ITEMS_MAIN = createPatternMap(
            MinetorioItems.PATTERN_INFINITY.get().getDefaultInstance(),
            MinetorioItems.PATTERN_VOID.get().getDefaultInstance(),
            MinetorioItems.PATTERN_AIR.get().getDefaultInstance(),
            MinetorioItems.PATTERN_FIRE.get().getDefaultInstance(),
            MinetorioItems.PATTERN_EARTH.get().getDefaultInstance(),
            MinetorioItems.PATTERN_WATER.get().getDefaultInstance()
    );
    private static final List<Map<Direction, ItemStack>> PATTERN_ITEMS_EW_LIST = List.of(
            createPatternMap(
                    MinetorioItems.PATTERN_CLOUD.get().getDefaultInstance(),
                    MinetorioItems.PATTERN_SUN.get().getDefaultInstance(),
                    MinetorioItems.PATTERN_SNOW.get().getDefaultInstance(),
                    MinetorioItems.PATTERN_SNOWFLAKE.get().getDefaultInstance(),
                    ItemStack.EMPTY,
                    ItemStack.EMPTY
            ),
            createPatternMap(
                    MinetorioItems.PATTERN_ENERGY_CONSUMER.get().getDefaultInstance(),
                    MinetorioItems.PATTERN_WATER_CONSUMER.get().getDefaultInstance(),
                    MinetorioItems.PATTERN_LAVA_CONSUMER.get().getDefaultInstance(),
                    MinetorioItems.PATTERN_MINETORIO.get().getDefaultInstance(),
                    ItemStack.EMPTY,
                    ItemStack.EMPTY
            ),
            createPatternMap(
                    MinetorioItems.PATTERN_CLOUD.get().getDefaultInstance(),
                    MinetorioItems.PATTERN_SUN.get().getDefaultInstance(),
                    MinetorioItems.PATTERN_SNOW.get().getDefaultInstance(),
                    MinetorioItems.PATTERN_SNOWFLAKE.get().getDefaultInstance(),
                    ItemStack.EMPTY,
                    ItemStack.EMPTY
            ),
            createPatternMap(
                    MinetorioItems.PATTERN_CREEPER.get().getDefaultInstance(),
                    MinetorioItems.PATTERN_SKELETON.get().getDefaultInstance(),
                    MinetorioItems.PATTERN_SPIDER.get().getDefaultInstance(),
                    MinetorioItems.PATTERN_ZOMBIE.get().getDefaultInstance(),
                    ItemStack.EMPTY,
                    ItemStack.EMPTY
            )
    );

    private static final Map<Direction, ItemStack> PATTERN_ITEMS_UD = createPatternMap(
            ItemStack.EMPTY,
            ItemStack.EMPTY,
            MinetorioItems.PATTERN_RAIN.get().getDefaultInstance(),
            MinetorioItems.PATTERN_RESEARCH_BOOK.get().getDefaultInstance(),
            MinetorioItems.PATTERN_BATTERY.get().getDefaultInstance(),
            MinetorioItems.PATTERN_LIGHTNING.get().getDefaultInstance()
    );

    public static ItemStack getPatternItem(Map<Direction, ItemStack> itemsMap,Direction direction) {
        return itemsMap.getOrDefault(direction, ItemStack.EMPTY);
    }

    @SuppressWarnings("unused")
    public PatternCollectorBlockRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(@NotNull PatternsCollectorBlockEntity blockEntity, float partialTicks, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {


        float offset = 5.0f;
        float ringYOffset = getCurrentYOffset(blockEntity, blockEntity.getCurrentYOffset(), offset);

        blockEntity.setCurrentOffset(ringYOffset);
        float baseRotation = blockEntity.getRotation() + partialTicks;

        float rotationX = (baseRotation * blockEntity.getSpeedX());
        float rotationY = (baseRotation * blockEntity.getSpeedY());
        float rotationZ = (baseRotation * blockEntity.getSpeedZ());

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5+ offset, 0.5);
        renderScene(poseStack, buffer, packedLight, packedOverlay, rotationX, rotationY, rotationZ, ringYOffset);
        poseStack.popPose();

    }

    private void renderScene (PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, float rotationX, float rotationY, float rotationZ, float ringYOffset){

        poseStack.pushPose();

        poseStack.pushPose();
        poseStack.translate(0, ringYOffset, 0);
        renderRing(poseStack, buffer, packedLight, packedOverlay, PATTERN_ITEMS_MAIN, rotationX, rotationY, rotationZ, 1,  0.5);
        renderRing(poseStack, buffer, packedLight, packedOverlay, PATTERN_ITEMS_MAIN, -rotationX, -rotationY, -rotationZ, 1,  1);
        poseStack.popPose();

        poseStack.mulPose(Axis.XP.rotationDegrees(rotationX));
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationY));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotationZ));
        renderSphere(poseStack, buffer, packedLight, packedOverlay);

        poseStack.popPose();

    }

    private void renderSphere(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        double radius = 9;
        poseStack.pushPose();
        renderRing(poseStack, buffer, packedLight, packedOverlay, PATTERN_ITEMS_MAIN,0,0,0, 3f, radius);
        renderRing(poseStack, buffer, packedLight, packedOverlay, PATTERN_ITEMS_UD,0,45,0, 3f, radius);

        for (int i = 0; i < PATTERN_ITEMS_EW_LIST.size(); i++) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(45 * i));
            renderRing(poseStack, buffer, packedLight, packedOverlay, PATTERN_ITEMS_EW_LIST.get(i), 45, 0, 0, 3f, radius);
            poseStack.popPose();
        }

        poseStack.popPose();
    }


    private void renderRing(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay,
                            Map<Direction, ItemStack> itemMap, float rotationX, float rotationY, float rotationZ, float scale, double radius) {

        poseStack.pushPose();

        poseStack.mulPose(Axis.XP.rotationDegrees(rotationX));
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationY));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotationZ));

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
        if (mc.player == null) return previousYOffset;
        double dx = blockEntity.getBlockPos().getX() + 0.5 - mc.player.getX();
        double dy = blockEntity.getBlockPos().getY() + 0.5 - (mc.player.getY() + mc.player.getEyeHeight());
        double dz = blockEntity.getBlockPos().getZ() + 0.5 - mc.player.getZ();

        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        float targetYOffset = (distance < 3.0) ? -offset : 0f;
        return previousYOffset + (targetYOffset - previousYOffset) * 0.1f;
    }

    private static Map<Direction, ItemStack> createPatternMap(ItemStack up, ItemStack down, ItemStack north,
                                                              ItemStack south, ItemStack west, ItemStack east) {
        Map<Direction, ItemStack> map = new EnumMap<>(Direction.class);
        map.put(Direction.UP, up);
        map.put(Direction.DOWN, down);
        map.put(Direction.NORTH, north);
        map.put(Direction.SOUTH, south);
        map.put(Direction.WEST, west);
        map.put(Direction.EAST, east);
        return map;
    }
}