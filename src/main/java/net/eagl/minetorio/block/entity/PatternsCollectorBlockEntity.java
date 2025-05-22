package net.eagl.minetorio.block.entity;

import net.eagl.minetorio.gui.PatternsCollectorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class PatternsCollectorBlockEntity extends BlockEntity implements MenuProvider {

    private float currentX = 0f, currentY = 0f, currentZ = 0f;
    private float targetX = 0f, targetY = 0f, targetZ = 0f;
    private float prevX, prevY, prevZ;
    private float currentYOffset = 0;

    private int ticksSinceLastTargetUpdate = 0;
    private static final int TARGET_UPDATE_INTERVAL = 40;

    public PatternsCollectorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MinetorioBlockEntities.PATTERNS_COLLECTOR.get(), pPos, pBlockState);
    }

    public void tickRotation() {
        ticksSinceLastTargetUpdate++;

        if (this.level != null && ticksSinceLastTargetUpdate >= TARGET_UPDATE_INTERVAL) {
            ticksSinceLastTargetUpdate = 0;

            final float maxTargetChange = 10f; // максимум зміни цілі за раз

            targetX = (targetX + (level.random.nextFloat() * 2f - 1f) * maxTargetChange) % 360f;
            targetY = (targetY + (level.random.nextFloat() * 2f - 1f) * maxTargetChange) % 360f;
            targetZ = (targetZ + (level.random.nextFloat() * 2f - 1f) * maxTargetChange) % 360f;
        }

        prevX = currentX;
        prevY = currentY;
        prevZ = currentZ;

        // плавне наближення (speed = 0.05f-0.1f для плавного руху)
        currentX = approachAngle(currentX, targetX, 0.08f);
        currentY = approachAngle(currentY, targetY, 0.07f);
        currentZ = approachAngle(currentZ, targetZ, 0.06f);
    }

    private float approachAngle(float current, float target, float speed) {
        float diff = (target - current + 540f) % 360f - 180f;
        return current + diff * speed;
    }
    public float getInterpolatedX(float partialTick) {
        return prevX + (currentX - prevX) * partialTick;
    }

    public float getInterpolatedY(float partialTick) {
        return prevY + (currentY - prevY) * partialTick;
    }

    public float getInterpolatedZ(float partialTick) {
        return prevZ + (currentZ - prevZ) * partialTick;
    }

    public void setCurrentOffset(float offset) {
        this.currentYOffset=offset;
    }

    public void tickClient() {

    }

    public void tickServer() {

    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Patterns Collector");
    }


    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new PatternsCollectorMenu(pContainerId, pPlayerInventory,this);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putFloat("currentX", currentX);
        tag.putFloat("currentY", currentY);
        tag.putFloat("currentZ", currentZ);
        tag.putFloat("currentYOffset", currentYOffset);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        this.currentX = tag.getFloat("currentX");
        this.currentY = tag.getFloat("currentY");
        this.currentZ = tag.getFloat("currentZ");
        this.currentYOffset = tag.getFloat("currentYOffset");
    }

    public float getCurrentYOffset() {
        return this.currentYOffset;
    }
}
