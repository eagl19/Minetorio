package net.eagl.minetorio.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;

public class PlayerTimers {
    private static final Map<UUID, List<PlayerBlockChangeTimer>> timers = new HashMap<>();

    public static void addTimer(ServerPlayer player, BlockPos pos, int ticks) {
        timers.computeIfAbsent(player.getUUID(), k -> new ArrayList<>()).add(new PlayerBlockChangeTimer(pos, ticks));
    }

    public static void tick(ServerLevel level) {
        Iterator<Map.Entry<UUID, List<PlayerBlockChangeTimer>>> playerIter = timers.entrySet().iterator();
        while (playerIter.hasNext()) {
            Map.Entry<UUID, List<PlayerBlockChangeTimer>> entry = playerIter.next();
            List<PlayerBlockChangeTimer> list = entry.getValue();
            list.removeIf(timer -> timer.tick(level));
            if (list.isEmpty()) {
                playerIter.remove();
            }
        }
    }
}