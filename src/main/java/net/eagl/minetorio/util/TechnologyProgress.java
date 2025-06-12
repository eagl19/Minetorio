package net.eagl.minetorio.util;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;
import java.util.Set;

public class TechnologyProgress {

    private static final String NBT_TAG = "MinetorioLearnedTechnologies";


    public static void learnTechnology(Player player, String techId) {
        Set<String> current = getLearnedTechnologies(player);
        if (current.add(techId)) {
            saveLearnedTechnologies(player, current);
        }
    }

    public static boolean hasLearned(Player player, String techId) {
        return getLearnedTechnologies(player).contains(techId);
    }

    public static Set<String> getLearnedTechnologies(Player player) {
        Set<String> result = new HashSet<>();
        ListTag list = player.getPersistentData().getList(NBT_TAG, Tag.TAG_STRING);
        for (Tag tag : list) {
            result.add(tag.getAsString());
        }
        return result;
    }

    private static void saveLearnedTechnologies(Player player, Set<String> techs) {
        ListTag list = new ListTag();
        for (String id : techs) {
            list.add(StringTag.valueOf(id));
        }
        player.getPersistentData().put(NBT_TAG, list);
    }
}

