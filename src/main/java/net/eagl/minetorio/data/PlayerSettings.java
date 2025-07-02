package net.eagl.minetorio.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class PlayerSettings {
    private final List<EntityType<?>> allowedMobs = new ArrayList<>();
    private boolean initialized = false;

    public void load(CompoundTag tag) {
        allowedMobs.clear();
        ListTag mobList = tag.getList("AllowedMobs", Tag.TAG_STRING);
        for (Tag mobTag : mobList) {
            String idString = mobTag.getAsString();
            ResourceLocation id = ResourceLocation.tryParse(idString);
            if (id != null) {
                EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(id);
                if (type != null) {
                    allowedMobs.add(type);
                }
            }
        }
        initialized = tag.getBoolean("Initialized");
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        ListTag mobList = new ListTag();
        for (EntityType<?> type : allowedMobs) {
            mobList.add(StringTag.valueOf(EntityType.getKey(type).toString()));
        }
        tag.put("AllowedMobs", mobList);
        tag.putBoolean("Initialized", initialized);
        return tag;
    }

    public List<EntityType<?>> getAllowedMobs() {
        return allowedMobs;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean value) {
        this.initialized = value;
    }
}
