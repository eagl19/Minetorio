package net.eagl.minetorio.event;

import net.eagl.minetorio.block.MinetorioBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;

public class PlayerBlockChangeTimer {
    private final BlockPos targetPos;
    private int ticksRemaining;

    public PlayerBlockChangeTimer(BlockPos pTargetPos, int pTicks) {
        this.targetPos = pTargetPos;
        this.ticksRemaining = pTicks;
    }

    public boolean tick(ServerLevel level) {
        ticksRemaining--;
        if (ticksRemaining <= 0) {
            level.setBlock(targetPos, MinetorioBlocks.GLOWING_BEDROCK.get().defaultBlockState(), 3);
            return true;
        }
        return false;
    }
}
