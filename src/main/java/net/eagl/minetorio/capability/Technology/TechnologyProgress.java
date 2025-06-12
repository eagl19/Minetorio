package net.eagl.minetorio.capability.Technology;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashSet;
import java.util.Set;

public class TechnologyProgress implements ITechnologyProgress, INBTSerializable<ListTag> {
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
    public ListTag serializeNBT() {
        ListTag tag = new ListTag();
        for (String id : learned) {
            tag.add(StringTag.valueOf(id));
        }
        return tag;
    }

    @Override
    public void deserializeNBT(ListTag nbt) {
        learned.clear();
        for (Tag tag : nbt) {
            learned.add(tag.getAsString());
        }
    }
}


