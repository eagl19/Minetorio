package net.eagl.minetorio.util;

import net.eagl.minetorio.util.enums.FlaskColor;
import net.eagl.minetorio.util.storage.FlaskStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;

public class Leaner implements INBTSerializable<CompoundTag> {

    private Technology tech;
    private final Runnable onChange;
    private int time;
    private int totalTime;


    public Leaner(Technology tech, Runnable onChange){
        if (tech == null) tech = Technology.EMPTY;
        this.tech = tech;
        this.time = 0;
        this.onChange = onChange;
        this.totalTime = tech.getTotalTime();
    }

    public void setTech(Technology tech){
        if (tech == null) tech = Technology.EMPTY;
        if(!this.tech.equals(tech) && !tech.equals(Technology.EMPTY)) {
            this.tech = tech;
            this.time = 0;
            this.totalTime = tech.getTotalTime();
        }

    }

    private int getTechTime() {
        return tech.getTime();
    }

    public int getFlaskCount(FlaskColor color) {
        return tech.getCost().getFlaskAmount(color);
    }

    public int getFlaskTime(FlaskColor color) {
        int flaskCount = getFlaskCount(color);
        if (flaskCount == 0) return 0;
        return getTechTime() / flaskCount;
    }

    public boolean canLearn(FlaskStorage itemHandler) {
        if(tech==null || tech.equals(Technology.EMPTY)) return false;
        for (Map.Entry<FlaskColor, Integer> entry : tech.getCost().getAll().entrySet()) {
            FlaskColor color = entry.getKey();
            if (itemHandler.getFlaskAmount(color) < (getFlaskCount(color) > 0 ? 1 : 0)) {
                return false;
            }
        }
        return true;
    }

    public boolean consumeFlasks(FlaskStorage itemHandler){
        boolean change = false;
        this.tick();
        for (FlaskColor color : FlaskColor.values()) {
            int timeColor = getFlaskTime(color);
            if(timeColor > 0) {
                if (this.time % timeColor == 0) {
                   change |= itemHandler.consumeFlasks(color, 1);
                }
            }
        }
        if(change) {
            onChange.run();
        }
        return change;
    }

    private void tick(){
        this.time++;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public int getCurrentTime(){
        return this.time;
    }

    public static Leaner fromNBT(CompoundTag tag, Runnable onChange) {
        String techId = tag.getString("Technology");
        Technology tech = TechnologyRegistry.get(techId);
        Leaner leaner = new Leaner(tech, onChange);
        leaner.deserializeNBT(tag);
        return leaner;
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
        this.time = tag.getInt("Time");
    }
}
