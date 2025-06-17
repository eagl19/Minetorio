package net.eagl.minetorio.util;

import net.eagl.minetorio.util.enums.FlaskColor;
import net.eagl.minetorio.util.storage.FlaskStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class Learner implements INBTSerializable<CompoundTag> {

    private Technology tech;
    private final Runnable onChange;
    private int time;
    private int totalTime;
    private final Map<FlaskColor, Integer> cachedFlaskCounts = new EnumMap<>(FlaskColor.class);
    private final Map<FlaskColor, Integer> cachedFlaskTimes = new EnumMap<>(FlaskColor.class);
    private boolean isLearn = false;
    private boolean canUpdateLearner = false;
    private final FlaskStorage itemHandler;
    private boolean isDone = false;


    public Learner(Technology tech, FlaskStorage itemHandler, Runnable onChange){
        this.tech = Objects.requireNonNullElse(tech, Technologies.EMPTY);
        this.time = 0;
        this.onChange = onChange;
        this.totalTime = tech.getTotalTime();
        this.itemHandler = itemHandler;
        updateCaches();
    }

    public void setTech(Technology tech){
        if (tech == null) tech = Technologies.EMPTY;
        if(!this.tech.equals(tech) && !tech.equals(Technologies.EMPTY)) {
            this.tech = tech;
            this.time = 0;
            this.totalTime = tech.getTotalTime();
            updateCaches();
        }
    }

    private void updateCaches() {
        cachedFlaskCounts.clear();
        cachedFlaskTimes.clear();
        isLearn = false;

        if (tech == null || tech.equals(Technologies.EMPTY)) return;

        int timeTech = tech.getTime();
        for (FlaskColor color : FlaskColor.values()) {
            int count = tech.getCost().getFlaskAmount(color);
            if (count > 0) {
                cachedFlaskCounts.put(color, count);
                cachedFlaskTimes.put(color, timeTech / count);
            }
        }

        recheckIsLearnable();

    }

    public void setDirty(boolean dirty) {
        this.canUpdateLearner = dirty;
    }

    public boolean canLearn(){
        if(isDone) return false;
        if (canUpdateLearner) {
            updateCaches();
        }
        return isLearn;
    }

    public void learn() {
        time++;
        consumeFlasks();

        if(time >= totalTime) {
            isDone = true;
            time = totalTime;
        }
    }

    private void recheckIsLearnable() {
        isLearn = cachedFlaskCounts.isEmpty() || cachedFlaskCounts.entrySet().stream()
                .allMatch(entry -> itemHandler.getFlaskAmount(entry.getKey()) >= 1);
    }

    public void clear(){
        tech = Technologies.EMPTY;
        canUpdateLearner = true;
        isLearn = false;
        time = 0;
        totalTime = 0;
        isDone = false;
    }

    private void consumeFlasks() {
        boolean change = false;

        for (FlaskColor color : FlaskColor.values()) {
            int interval = cachedFlaskTimes.getOrDefault(color, 0);
            if (interval > 0 && time > 0 && time % interval == 0) {
                change |= itemHandler.consumeFlasks(color, 1);
            }
        }

        if (change) {
            recheckIsLearnable();
            onChange.run();
        }
    }

    public int getTotalTime() {
        return totalTime;
    }

    public int getCurrentTime(){
        return time;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Technology", tech.getId());
        tag.putInt("Time", time);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.tech = TechnologyRegistry.get(tag.getString("Technology"));
        this.totalTime = tech.getTotalTime();
        this.time = tag.getInt("Time");
        this.canUpdateLearner = true;
    }

    public boolean isDone() {
        return isDone;
    }
}
