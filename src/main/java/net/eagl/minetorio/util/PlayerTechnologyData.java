package net.eagl.minetorio.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.HashSet;
import java.util.Set;

public class PlayerTechnologyData {
    private final Set<String> researched = new HashSet<>();

    public boolean has(String id) {
        return researched.contains(id);
    }

    public void unlock(String id) {
        researched.add(id);
    }

    public Set<String> getAll() {
        return researched;
    }

    public void saveNBT(CompoundTag tag) {
        ListTag list = new ListTag();
        for (String tech : researched) {
            list.add(StringTag.valueOf(tech));
        }
        tag.put("Technologies", list);
    }

    public void loadNBT(CompoundTag tag) {
        researched.clear();
        ListTag list = tag.getList("Technologies", Tag.TAG_STRING);
        for (Tag t : list) {
            researched.add(t.getAsString());
        }
    }
}

