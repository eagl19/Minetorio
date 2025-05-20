package net.eagl.minetorio.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;

public interface IPatternLearn extends INBTSerializable<CompoundTag> {

    boolean isLearned(String patternId);

    void setLearned(String patternId, boolean learned);

    Map<String, Boolean> getPatterns();
}
