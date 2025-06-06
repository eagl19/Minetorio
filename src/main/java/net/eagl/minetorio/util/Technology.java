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

import java.util.Collections;
import java.util.List;

public record Technology(String id, Item displayIcon, List<String> prerequisites, List<ItemStack> cost, int time,
                         int count, boolean hidden, int x, int y) {

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
            Codec.STRING.fieldOf("id").forGetter(Technology::id),
            ForgeRegistries.ITEMS.getCodec().fieldOf("icon").forGetter(Technology::displayIcon),
            Codec.STRING.listOf().fieldOf("prerequisites").forGetter(Technology::prerequisites),
            ItemStack.CODEC.listOf().fieldOf("cost").forGetter(Technology::cost),
            Codec.INT.fieldOf("time").forGetter(Technology::time),
            Codec.INT.fieldOf("count").forGetter(Technology::count),
            Codec.BOOL.fieldOf("hidden").forGetter(Technology::hidden),
            Codec.INT.fieldOf("x").forGetter(Technology::x),
            Codec.INT.fieldOf("y").forGetter(Technology::y)
    ).apply(instance, Technology::new));

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
        int totalSeconds = time * this.count() / 20;
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        return Component.translatable("tooltip.minetorio.learn_time")
                .append(": ").withStyle(ChatFormatting.DARK_BLUE)
                .append(Component.translatable(String.format("%d:%02d:%02d", hours, minutes, seconds))
                        .withStyle(ChatFormatting.BLUE)
                );
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
}

