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

    public List<Technology> getPlan() {
        return techList;
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
}
