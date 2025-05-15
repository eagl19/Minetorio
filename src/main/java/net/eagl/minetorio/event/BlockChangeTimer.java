package net.eagl.minetorio.event;

import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.custom.GlowingBedrockBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class BlockChangeTimer {
    private final BlockPos targetPos;
    private final ResourceKey<Level> dimension;
    private int ticksRemaining;

    public BlockChangeTimer(BlockPos pos, ResourceKey<Level> dimension, int ticks) {
        this.targetPos = pos;
        this.dimension = dimension;
        this.ticksRemaining = ticks;
    }

    public boolean tick(ServerLevel level) {
        if (!level.dimension().equals(dimension)) return false;

        ticksRemaining--;
        if (ticksRemaining <= 0) {
            level.setBlock(targetPos,
                    MinetorioBlocks.GLOWING_BEDROCK.get()
                            .defaultBlockState()
                            .setValue(GlowingBedrockBlock.NUMBER, 0), 11);
            return true;
        }

        return false;
    }

    public ResourceKey<Level> getDimension() {
        return dimension;
    }
}
