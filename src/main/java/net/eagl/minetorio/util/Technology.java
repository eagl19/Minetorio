package net.eagl.minetorio.util;

import net.eagl.minetorio.item.MinetorioItems;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Technology {

    private final String id;
    private final Item displayIcon;
    private final List<String> prerequisites;
    private final List<ItemStack> cost;
    private final int time;
    private final int count;
    private final int x;
    private final int y;
    private  boolean learn;

    public static final Technology EMPTY = new Technology(
            "empty",
            MinetorioItems.PATTERN_EMPTY.get(),
            Collections.emptyList(),
            Collections.emptyList(),
            0,
            0,
            0,
            0
    );



    public void saveToNBT(CompoundTag tag) {
        tag.putString("Id", this.id);
    }

    public static Technology fromNBT(CompoundTag tag) {
        String id = tag.getString("Id");
        System.out.println(id);
        return TechnologyRegistry.get(id) != null ? TechnologyRegistry.get(id) : Technology.EMPTY;
    }


    public Technology(String id, Item displayIcon, List<String> prerequisites, List<ItemStack> cost, int time, int count, int x, int y) {
        this.id = id;
        this.displayIcon = displayIcon;
        this.prerequisites = prerequisites;
        this.cost = cost;
        this.time = time;
        this.count = count;
        this.x = x;
        this.y = y;
        this.learn = false;
    }

    public MutableComponent getBenefit() {
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(displayIcon);
        if (key == null) {
            return Component.empty();
        }
        return Component.translatable("pattern.minetorio." + key.getPath() + ".benefit").withStyle(ChatFormatting.BLUE);
    }

    public MutableComponent getTotalTimeAsString() {
        int totalSeconds = time * count / 20;
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        return Component.translatable("tooltip.minetorio.learn_time")
                .append(": ").withStyle(ChatFormatting.DARK_BLUE)
                .append(Component.translatable(String.format("%d:%02d:%02d", hours, minutes, seconds))
                        .withStyle(ChatFormatting.BLUE)
                );
    }

    public void learn(boolean pLearn){
        this.learn = pLearn;
    }

    public boolean isLearn() {
        return learn;
    }

    public boolean canLearn(List<Technology> techList) {
        if (prerequisites.isEmpty()) {
            return true;
        }

        Set<String> learnedIds = techList.stream()
                .map(Technology::getId)
                .collect(Collectors.toSet());

        for (String parentId : prerequisites) {
            Technology parent = TechnologyRegistry.get(parentId);
            if (parent == null || (!parent.isLearn() && !learnedIds.contains(parentId))) {
                return false;
            }
        }

        return true;
    }

    public Component getDisplayName() {
        return displayIcon.getDescription();
    }

    public Component getFormattedCost() {
        return Component.literal(
                cost.stream()
                        .map(s -> s.getCount() + "x " + s.getDisplayName().getString())
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("Free")
        );
    }


    public String getId() {
        return id;
    }

    public Item getDisplayIcon() {
        return displayIcon;
    }

    public List<String> getPrerequisites() {
        return prerequisites;
    }

    public List<ItemStack> getCost() {
        return cost;
    }

    public int getTime() {
        return time;
    }

    public int getCount() {
        return count;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
