package net.eagl.minetorio.event;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.capability.MinetorioCapabilities;
import net.eagl.minetorio.network.MinetorioNetwork;
import net.eagl.minetorio.network.PatternLearnSyncPacket;
import net.eagl.minetorio.util.PatternItemsCollector;
import net.eagl.minetorio.worldgen.dimension.MinetorioDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;


@Mod.EventBusSubscriber(modid = Minetorio.MOD_ID)
public class PlayerLoginEvents {

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {

        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        if (!player.getPersistentData().getBoolean("minetorio_teleported")) {

            ServerLevel minetorioLevel = player.server.getLevel(MinetorioDimensions.MINETORIO_DIM_EMPTY_LEVEL_KEY);

            if (minetorioLevel == null) return;

            BlockPos spawnPos = new BlockPos(0, 100, 0);
            player.setRespawnPosition(
                    minetorioLevel.dimension(),
                        spawnPos,
                        0.0F,
                        true,
                        true
                );

            player.teleportTo(minetorioLevel,
                    spawnPos.getX() + 0.5,
                    spawnPos.getY() + 0.5,
                    spawnPos.getZ() + 0.5,
                    player.getYRot(),
                    player.getXRot());

            player.getPersistentData().putBoolean("minetorio_teleported", true);
        }


        player.getCapability(MinetorioCapabilities.PATTERN_LEARN).ifPresent(patternLearn -> {
            boolean anyLearned = false;
            for (var item : PatternItemsCollector.getPatternItems()) {
                var id = ForgeRegistries.ITEMS.getKey(item);
                if (id == null) continue;
                if (patternLearn.isLearned(id.toString())) {
                    anyLearned = true;
                    break;
                }
            }

            if (!anyLearned) {
                for (var item : PatternItemsCollector.getPatternItems()) {
                    var id = ForgeRegistries.ITEMS.getKey(item);
                    if (id == null) continue;
                    patternLearn.setLearned(id.toString(), false);
                }
            }

        });

        player.getCapability(MinetorioCapabilities.PATTERN_LEARN)
                .ifPresent(cap -> MinetorioNetwork.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> player),
                        new PatternLearnSyncPacket(cap.getPatterns())
                ));

    }
}

