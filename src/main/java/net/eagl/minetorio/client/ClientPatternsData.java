package net.eagl.minetorio.client;

import java.util.HashMap;
import java.util.Map;

public class ClientPatternsData {
    private static final Map<String, Boolean> learnedMap = new HashMap<>();

    public static synchronized void setLearnedMap(Map<String, Boolean> map) {
        learnedMap.clear();
        learnedMap.putAll(map);
    }

    public static Map<String, Boolean> getPatterns() {
        return learnedMap;
    }

    public static synchronized boolean isLearned(String key) {
        return learnedMap.getOrDefault(key, false);
    }

    public static synchronized void clear() {
        learnedMap.clear();
    }
}
