package net.eagl.minetorio.capability;

import net.minecraft.nbt.CompoundTag;

public interface IPatternLearn {
    CompoundTag serializeNBT();
    void deserializeNBT(CompoundTag nbt);

    // методи, наприклад
    boolean isLearned(String patternId);
    void setLearned(String patternId, boolean learned);
}
