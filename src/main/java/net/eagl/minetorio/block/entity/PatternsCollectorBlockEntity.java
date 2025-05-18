package net.eagl.minetorio.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;


public class PatternsCollectorBlockEntity extends BlockEntity {

    private final float speedX;
    private final float speedY;
    private final float speedZ;

    private float currentYOffset = 0f;
    private float rotation =0;

    public PatternsCollectorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MinetorioBlockEntities.PATTERNS_COLLECTOR.get(), pPos, pBlockState);

        RandomSource rand = RandomSource.create(pPos.asLong()); // Детермінований для кожного блоку
        this.speedX = 0.5f + rand.nextFloat(); // від 0.5 до 1.5
        this.speedY = 0.5f + rand.nextFloat();
        this.speedZ = 0.5f + rand.nextFloat();
    }

    public void tickClient() {
        rotation += 1.0f;
    }

    public void tickServer() {

    }

    public float getCurrentYOffset() {
        return currentYOffset;
    }

    public void setCurrentYOffset(float yOffset) {
        this.currentYOffset = yOffset;
    }

    public float getSpeedX() {
        return speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public float getSpeedZ() {
        return speedZ;
    }


    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putFloat("rotation", rotation);
        tag.putFloat("currentYOffset", currentYOffset);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        this.rotation = tag.getFloat("rotation");
        this.currentYOffset = tag.getFloat("currentYOffset");
    }

    public float getRotation() {
        return rotation;
    }
}
