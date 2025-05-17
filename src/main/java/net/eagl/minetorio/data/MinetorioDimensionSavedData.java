package net.eagl.minetorio.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class MinetorioDimensionSavedData extends SavedData {

    private boolean initialized;

    public MinetorioDimensionSavedData() {
        this.initialized = false;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void markInitialized() {
        this.initialized = true;
        this.setDirty();
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag) {
        tag.putBoolean("Initialized", initialized);
        return tag;
    }

    public static MinetorioDimensionSavedData load(CompoundTag tag) {
        MinetorioDimensionSavedData data = new MinetorioDimensionSavedData();
        data.initialized = tag.getBoolean("Initialized");
        return data;
    }

    public static MinetorioDimensionSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                MinetorioDimensionSavedData::load,
                MinetorioDimensionSavedData::new,
                "minetorio_dimension_data"
        );
    }
}
