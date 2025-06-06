package net.eagl.minetorio.network.server;

import net.eagl.minetorio.util.Technology;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class ServerTechnologyHandler {
    public static void process(ServerPlayer player, List<Technology> techList) {
        // Запис у BlockEntity, перевірка, логіка
        System.out.println("Гравець " + player.getName().getString() + " надіслав " + techList.size() + " технологій.");
    }
}

