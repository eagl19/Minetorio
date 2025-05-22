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
import net.minecraft.world.phys.Vec3;
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

    private static final Map<Direction, ItemStack> PATTERN_ITEMS_SN = Map.of(
            Direction.UP,    new ItemStack(MinetorioItems.PATTERN_CLOUD.get()),
            Direction.DOWN,  new ItemStack(MinetorioItems.PATTERN_CLOUD.get()),
            Direction.NORTH, ItemStack.EMPTY,
            Direction.SOUTH, ItemStack.EMPTY,
            Direction.WEST,  new ItemStack(MinetorioItems.PATTERN_CLOUD.get()),
            Direction.EAST,  new ItemStack(MinetorioItems.PATTERN_CLOUD.get())
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

        poseStack.pushPose();
        poseStack.translate(0.5, 5.5, 0.5);

        // 🔄 Обчислення анімованого зсуву
        ringYOffset = getCurrentYOffset(blockEntity, ringYOffset);

        float baseRotation = partialTicks;
        if (blockEntity.getLevel() != null) {
            baseRotation = (blockEntity.getLevel().getGameTime() + partialTicks) * 2f;
        }

        renderRing(poseStack, buffer, packedLight, packedOverlay, PATTERN_ITEMS_MAIN, new Vec3(0,0,baseRotation), 0.5, ringYOffset);
        renderRing(poseStack, buffer, packedLight, packedOverlay, PATTERN_ITEMS_MAIN, new Vec3(0,0,-baseRotation), 1.0, ringYOffset);
        renderRing(poseStack, buffer, packedLight, packedOverlay, PATTERN_ITEMS_MAIN, new Vec3(0,0,0),8.0, 0.0f);
        renderRing(poseStack, buffer, packedLight, packedOverlay, PATTERN_ITEMS_EW, new Vec3(45,0,0),8.0, 0.0f);
        renderRing(poseStack, buffer, packedLight, packedOverlay, PATTERN_ITEMS_SN, new Vec3(0,0,45),8.0, 0.0f);
        renderRing(poseStack, buffer, packedLight, packedOverlay, PATTERN_ITEMS_UD, new Vec3(0,45,0),8.0, 0.0f);
        renderRing(poseStack, buffer, packedLight, packedOverlay, PATTERN_ITEMS_SN, new Vec3(0,45,45),8.0, 0.0f);


        poseStack.popPose();

    }

    private void renderRing(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay,
                            Map<Direction, ItemStack> itemMap, Vec3 rotation, double radius, float yOffset) {

        poseStack.pushPose();

        poseStack.mulPose(Axis.XP.rotationDegrees((float) (rotation.x % 360.0f)));
        poseStack.mulPose(Axis.YP.rotationDegrees((float) (rotation.y % 360.0f)));
        poseStack.mulPose(Axis.ZP.rotationDegrees((float) (rotation.z % 360.0f)));

        for (Direction direction : Direction.values()) {
            ItemStack stack = getPatternItem(itemMap, direction);
            if (stack.isEmpty()) continue;

            poseStack.pushPose();

            // Вектор зміщення
            double dx = direction.getStepX() * radius;
            double dy = direction.getStepY() * radius + yOffset;
            double dz = direction.getStepZ() * radius;

            poseStack.translate(dx, dy, dz);

            // Орієнтація предмета
            switch (direction) {
                case UP -> poseStack.mulPose(Axis.XP.rotationDegrees(90));
                case DOWN -> poseStack.mulPose(Axis.XN.rotationDegrees(90));
                case NORTH, SOUTH -> {}
                case WEST -> poseStack.mulPose(Axis.YP.rotationDegrees(90));
                case EAST -> poseStack.mulPose(Axis.YN.rotationDegrees(90));
            }

            // Масштаб тільки для зовнішнього кільця (8 блоків)
            if (radius == 8.0) {
                poseStack.scale(4.0f, 4.0f, 4.0f);
            }

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



    private static float getCurrentYOffset(@NotNull PatternsCollectorBlockEntity blockEntity, float previousYOffset) {
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
                targetYOffset = -5.0f;
            }
        }

        float smoothing = 0.1f;
        return previousYOffset + (targetYOffset - previousYOffset) * smoothing;
    }
}