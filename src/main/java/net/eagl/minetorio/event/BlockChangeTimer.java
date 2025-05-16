package net.eagl.minetorio.event;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BlockChangeTimer {
    private final BlockPos targetPos;
    private final ResourceKey<Level> dimension;
    private int ticksRemaining;
    private final BlockState block;

    public BlockChangeTimer(BlockPos pos, BlockState pBlock, ResourceKey<Level> dimension, int ticks) {
        this.targetPos = pos;
        this.dimension = dimension;
        this.ticksRemaining = ticks;
        this.block=pBlock;
    }

    public boolean tick(ServerLevel level) {
        if (!level.dimension().equals(dimension)) return false;

        ticksRemaining--;
        if (ticksRemaining <= 0) {
            level.setBlock(targetPos, block, 3);
            return true;
        }

        return false;
    }

    public ResourceKey<Level> getDimension() {
        return dimension;
    }
}
