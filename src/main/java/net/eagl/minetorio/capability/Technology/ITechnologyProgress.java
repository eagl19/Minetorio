package net.eagl.minetorio.capability.Technology;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Set;

public interface ITechnologyProgress extends INBTSerializable<CompoundTag>  {
    Set<String> getLearnedTechnologies();
    void learnTechnology(String id);
    boolean hasLearned(String id);
    void setLearnedTechnologies(Set<String> techs);
}
