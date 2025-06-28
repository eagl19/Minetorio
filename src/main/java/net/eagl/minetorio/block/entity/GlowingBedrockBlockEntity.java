package net.eagl.minetorio.block.entity;

import net.eagl.minetorio.block.custom.MinetorioBlockState;
import net.eagl.minetorio.block.custom.GlowingBedrockBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class GlowingBedrockBlockEntity extends BlockEntity {

    private int currentTime;
    private final int timeInterval = 200;

    public GlowingBedrockBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MinetorioBlockEntities.GLOWING_BEDROCK_ENTITY.get(), pPos, pBlockState);
        this.currentTime = timeInterval;
    }

    public void tickClient() {
    }

    public void tickServer() {
        if (getBlockState().getValue(GlowingBedrockBlock.STATE) == MinetorioBlockState.UNSTABLE) {
            this.currentTime--;
            if (level != null && currentTime < 1) {
                level.setBlock(getBlockPos(), getBlockState().setValue(GlowingBedrockBlock.STATE, MinetorioBlockState.STABILIZED), 2);
                currentTime = timeInterval;
            }
        }
    }

}
