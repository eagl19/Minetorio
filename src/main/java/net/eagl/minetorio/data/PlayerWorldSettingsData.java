package net.eagl.minetorio.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerWorldSettingsData extends SavedData {
    private static final String ID = "player_world_settings";

    private final Map<UUID, PlayerSettings> playerSettingsMap = new HashMap<>();

    public static PlayerWorldSettingsData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(nbt -> {
            PlayerWorldSettingsData data = new PlayerWorldSettingsData();
            data.load(nbt);
            return data;
        }, PlayerWorldSettingsData::new, ID);
    }

    public PlayerSettings getOrCreate(UUID playerId) {
        return playerSettingsMap.computeIfAbsent(playerId, id -> new PlayerSettings());
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, PlayerSettings> entry : playerSettingsMap.entrySet()) {
            CompoundTag playerTag = new CompoundTag();
            playerTag.putUUID("Player", entry.getKey());
            playerTag.put("Settings", entry.getValue().save());
            list.add(playerTag);
        }
        tag.put("Players", list);
        return tag;
    }

    public void load(CompoundTag tag) {
        playerSettingsMap.clear();
        ListTag list = tag.getList("Players", Tag.TAG_COMPOUND);
        for (Tag t : list) {
            CompoundTag playerTag = (CompoundTag) t;
            UUID player = playerTag.getUUID("Player");
            PlayerSettings settings = new PlayerSettings();
            settings.load(playerTag.getCompound("Settings"));
            playerSettingsMap.put(player, settings);
        }
    }
}

