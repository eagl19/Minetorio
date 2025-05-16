package net.eagl.minetorio.event;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class Timer {
    private static final List<BlockChangeTimer> timers = new ArrayList<>();

    public static void addTimer(BlockPos pos, BlockState pBlock, ResourceKey<Level> dimension, int ticks) {
        timers.add(new BlockChangeTimer(pos, pBlock, dimension, ticks));
    }

    public static void tick(ServerLevel level) {
        timers.removeIf(timer -> timer.tick(level));
    }
}

