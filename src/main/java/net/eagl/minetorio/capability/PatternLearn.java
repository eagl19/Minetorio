package net.eagl.minetorio.capability;

import net.minecraft.nbt.CompoundTag;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PatternLearn implements IPatternLearn {

    private final Map<String, Boolean> learnedPatterns = new HashMap<>();

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        for (Map.Entry<String, Boolean> entry : learnedPatterns.entrySet()) {
            tag.putBoolean(entry.getKey(), entry.getValue());
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        learnedPatterns.clear();
        for (String key : nbt.getAllKeys()) {
            learnedPatterns.put(key, nbt.getBoolean(key));
        }
    }

    @Override
    public boolean isLearned(String patternId) {
        return learnedPatterns.getOrDefault(patternId, false);
    }

    @Override
    public void setLearned(String patternId, boolean learned) {
        learnedPatterns.put(patternId, learned);
    }

    @Override
    public Map<String, Boolean> getPatterns() {
        return new HashMap<>(learnedPatterns);
    }
}
