package net.eagl.minetorio.block.entity;

import net.eagl.minetorio.block.custom.Barrier;
import net.eagl.minetorio.block.custom.GeneratorState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BarrierBlockEntity extends BlockEntity {
    private int currentTime;
    private final int timeInterval = 200;
    public BarrierBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MinetorioBlockEntities.BARRIER_ENTITY.get(), pPos, pBlockState);
        this.currentTime = timeInterval;
    }

    public void tickClient() {
    }

    public void tickServer() {
        if (getBlockState().getValue(Barrier.STATE) == GeneratorState.UNSTABLE) {
            this.currentTime--;
            if (level != null && currentTime < 1) {
                level.setBlock(getBlockPos(), getBlockState().setValue(Barrier.STATE, GeneratorState.STABILIZED), 2);
                currentTime = timeInterval;
            }
        }
    }
}
