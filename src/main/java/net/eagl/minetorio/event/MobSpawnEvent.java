package net.eagl.minetorio.event;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.data.PlayerSettings;
import net.eagl.minetorio.data.PlayerWorldSettingsData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = Minetorio.MOD_ID)
public class MobSpawnEvent {

    @SubscribeEvent
    public static void onMobFinalizeSpawn(net.minecraftforge.event.entity.living.MobSpawnEvent.SpawnPlacementCheck event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        UUID playerId = extractPlayerIdFromLevel(level);
        if (playerId == null) return;

        PlayerWorldSettingsData data = PlayerWorldSettingsData.get(level);
        PlayerSettings settings = data.getOrCreate(playerId);
        System.out.println(settings.isInitialized());
        if (!settings.isInitialized()) return;

        EntityType<?> type = event.getEntityType();
        System.out.println(type);
        System.out.println(settings.getAllowedMobs());
        if (!settings.getAllowedMobs().contains(type)) {
            event.setResult(Event.Result.DENY);
        }
    }

    public static UUID extractPlayerIdFromLevel(ServerLevel level) {
        String path = level.dimension().location().getPath();
        if (!path.startsWith("plain_")) return null;

        String uuidStr = path.replace("plain_", "");
        try {
            return UUID.fromString(uuidStr.replaceFirst(
                    "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                    "$1-$2-$3-$4-$5"
            ));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}