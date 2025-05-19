package net.eagl.minetorio.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;

public interface IPatternLearn {
    CompoundTag serializeNBT();
    void deserializeNBT(CompoundTag nbt);

    // методи, наприклад
    boolean isLearned(String patternId);
    void setLearned(ServerPlayer player, String patternId, boolean learned);
    Map<String, Boolean> getLearnedPatterns();


}
