package net.eagl.minetorio.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;

public class PlayerBlockChangeTimer {
    private final BlockPos targetPos;
    private int ticksRemaining;

    public PlayerBlockChangeTimer(BlockPos targetPos, int ticks) {
        this.targetPos = targetPos;
        this.ticksRemaining = ticks;
    }

    public boolean tick(ServerLevel level) {
        ticksRemaining--;
        if (ticksRemaining <= 0) {
            level.setBlock(targetPos, Blocks.GLOWSTONE.defaultBlockState(), 3);
            return true; // completed
        }
        return false;
    }
}
