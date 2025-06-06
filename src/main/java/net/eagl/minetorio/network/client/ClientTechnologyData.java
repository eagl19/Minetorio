package net.eagl.minetorio.network.client;

import net.eagl.minetorio.util.Technology;

import java.util.ArrayList;
import java.util.List;

public class ClientTechnologyData {
    private static List<Technology> list = new ArrayList<>();

    public static void set(List<Technology> techList) {
        list = techList;
        // GUI update, if needed
    }

    public static List<Technology> get() {
        return list;
    }
}

