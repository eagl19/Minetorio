package net.eagl.minetorio.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResearchPlan implements INBTSerializable<CompoundTag> {

    private final List<Technology> techList = new ArrayList<>(Collections.nCopies(10, Technology.EMPTY));
    private final Runnable onChange;

    public ResearchPlan(Runnable onChange){
        this.onChange = onChange;
    }

    public void setPlan(List<Technology> pList){
        this.techList.clear();
        this.techList.addAll(pList);
        this.onChange.run();
    }

    public boolean setPlan(Technology tech, int index){
        if (index >= 0 && index < techList.size()) {
            this.techList.set(index, tech);
            sortEmpty();
            this.onChange.run();
            return true;
        }
        return false;
    }

    public List<Technology> getPlan() {
        return techList;
    }

    public Technology getFirst(){
         Technology tech = techList.get(0);
         if(tech == null) return  Technology.EMPTY;
         return  tech;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag idList = new ListTag();

        for (Technology tech : techList) {
            CompoundTag techTag = new CompoundTag();
            techTag.putString("Id", tech.getId());
            idList.add(techTag);
        }

        tag.put("TechListIds", idList);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        techList.clear();
        ListTag idList = nbt.getList("TechListIds", Tag.TAG_COMPOUND);

        for (Tag tagElement : idList) {
            CompoundTag techTag = (CompoundTag) tagElement;
            String id = techTag.getString("Id");
            Technology tech = TechnologyRegistry.get(id);
            techList.add(tech);
        }
        onChange.run();
    }

    public Technology nextTechnology() {
        Technology removed = techList.get(0);
        for (int i = 0; i < techList.size() - 1; i++) {
            techList.set(i, techList.get(i + 1));
        }
        techList.set(techList.size() - 1, Technology.EMPTY);
        onChange.run();
        return removed;
    }

    public boolean remove(int index) {
        if (!techList.get(index).equals(Technology.EMPTY)) {
            techList.set(index, Technology.EMPTY);
            for (int i = index + 1; i < techList.size(); i++) {
                Technology tech = techList.get(i);
                if (!tech.equals(Technology.EMPTY) && !tech.canLearn(techList)) {
                    techList.set(i, Technology.EMPTY);
                }
            }

            sortEmpty();
            onChange.run();
            return true;
        }
        return false;
    }

    public void sortEmpty(){
        techList.sort((a, b) -> {
            if (a == Technology.EMPTY && b != Technology.EMPTY) return 1;
            if (a != Technology.EMPTY && b == Technology.EMPTY) return -1;
            return 0;
        });
    }
}
