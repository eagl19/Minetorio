package net.eagl.minetorio.capability.Technology;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashSet;
import java.util.Set;

public class TechnologyProgress implements ITechnologyProgress, INBTSerializable<CompoundTag> {
    private final Set<String> learned = new HashSet<>();

    @Override
    public Set<String> getLearnedTechnologies() {
        return learned;
    }

    @Override
    public void learnTechnology(String id) {
        learned.add(id);
    }

    @Override
    public void setLearnedTechnologies(Set<String> techs) {
        learned.clear();
        learned.addAll(techs);
    }

    @Override
    public boolean hasLearned(String techId) {
        return learned.contains(techId);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (String id : learned) {
            list.add(StringTag.valueOf(id));
        }
        tag.put("Learned", list);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        learned.clear();
        if (nbt.contains("Learned", Tag.TAG_LIST)) {
            ListTag list = nbt.getList("Learned", Tag.TAG_STRING);
            for (Tag tag : list) {
                learned.add(tag.getAsString());
            }
        }
    }
}


