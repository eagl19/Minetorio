package net.eagl.minetorio.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.eagl.minetorio.item.MinetorioItems;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
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
    private final boolean hidden;
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
            true,
            0,
            0
    );

    public static final Codec<Technology> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("id").forGetter(Technology::getId),
            ForgeRegistries.ITEMS.getCodec().fieldOf("icon").forGetter(Technology::getDisplayIcon),
            Codec.STRING.listOf().fieldOf("prerequisites").forGetter(Technology::getPrerequisites),
            ItemStack.CODEC.listOf().fieldOf("cost").forGetter(Technology::getCost),
            Codec.INT.fieldOf("time").forGetter(Technology::getTime),
            Codec.INT.fieldOf("count").forGetter(Technology::getCount),
            Codec.BOOL.fieldOf("hidden").forGetter(Technology::isHidden),
            Codec.INT.fieldOf("x").forGetter(Technology::getX),
            Codec.INT.fieldOf("y").forGetter(Technology::getY)
    ).apply(instance, Technology::new));

    public Technology(String id, Item displayIcon, List<String> prerequisites, List<ItemStack> cost, int time, int count, boolean hidden, int x, int y) {
        this.id = id;
        this.displayIcon = displayIcon;
        this.prerequisites = prerequisites;
        this.cost = cost;
        this.time = time;
        this.count = count;
        this.hidden = hidden;
        this.x = x;
        this.y = y;
        this.learn = false;
    }

    public static Technology fromNBT(CompoundTag tag) {
        return CODEC.parse(NbtOps.INSTANCE, tag)
                .resultOrPartial(System.err::println)
                .orElse(Technology.EMPTY);
    }

    public CompoundTag serializeNBT() {
        return (CompoundTag) CODEC.encodeStart(NbtOps.INSTANCE, this)
                .resultOrPartial(System.err::println)
                .orElse(new CompoundTag());
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

    public boolean canLearnWithout(int techIndex, List<Technology> techList){
        List<Technology> list = new ArrayList<>(techList);
        for (int i = techIndex; i < list.size(); i++) {
            list.set(i, Technology.EMPTY);
        }
        return canLearn(list);
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

    public boolean isHidden() {
        return hidden;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
