package net.eagl.minetorio.util;

import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class TechnologyBuilder {
    private String id;
    private Item patternItem;
    private List<String> requirements = new ArrayList<>();
    private FlasksField flasks;
    private int duration;
    private int tier;
    private int posX;
    private int posY;

    public static TechnologyBuilder create(String id) {
        TechnologyBuilder builder = new TechnologyBuilder();
        builder.id = id;
        return builder;
    }

    public TechnologyBuilder pattern(Item patternItem) {
        this.patternItem = patternItem;
        return this;
    }

    public TechnologyBuilder requires(String... dependencies) {
        this.requirements = List.of(dependencies);
        return this;
    }

    public TechnologyBuilder flasks(FlasksField flasks) {
        this.flasks = flasks;
        return this;
    }

    public TechnologyBuilder duration(int duration) {
        this.duration = duration;
        return this;
    }

    public TechnologyBuilder tier(int tier) {
        this.tier = tier;
        return this;
    }

    public TechnologyBuilder pos(int x, int y) {
        this.posX = x;
        this.posY = y;
        return this;
    }

    public Technology build() {
        if (id == null || patternItem == null || flasks == null) {
            throw new IllegalStateException("Missing required TechnologyBuilder parameters");
        }
        return new Technology(id, patternItem, requirements, flasks, duration, tier, posX, posY);
    }
}

