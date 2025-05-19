package net.eagl.minetorio.event;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.capability.MinetorioCapabilities;
import net.eagl.minetorio.util.PatternItemsCollector;
import net.eagl.minetorio.worldgen.dimension.MinetorioDimensions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;


@Mod.EventBusSubscriber(modid = Minetorio.MOD_ID)
public class PlayerLoginEvents {

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;


        // Уникаємо повторної телепортації
        if (!player.getPersistentData().getBoolean("minetorio_teleported")) {

            ServerLevel minetorioLevel = player.server.getLevel(MinetorioDimensions.MINETORIO_DIM_EMPTY_LEVEL_KEY);

            if (minetorioLevel == null) return;

            player.teleportTo(minetorioLevel,
                    0 + 0.5,
                    100,
                    0 + 0.5,
                    player.getYRot(),
                    player.getXRot());

            // Позначаємо, що телепортація вже виконана
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
                    patternLearn.setLearned(player, id.toString(), true);
                }
            }
        });
    }
}

