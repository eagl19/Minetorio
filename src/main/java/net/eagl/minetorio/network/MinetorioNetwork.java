package net.eagl.minetorio.network;

import net.eagl.minetorio.capability.MinetorioCapabilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraft.server.level.ServerPlayer;

public class MinetorioNetwork {
    private static int packetId = 0;
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath("minetorio", "main"),
            () -> "1.0",
            s -> true,
            s -> true
    );

    public static void register() {
        CHANNEL.registerMessage(packetId++,
                SyncPatternLearnMessage.class,
                SyncPatternLearnMessage::toBytes,
                SyncPatternLearnMessage::new,
                SyncPatternLearnMessage::handle
        );
    }

    public static void syncPatternLearn(ServerPlayer player) {
        player.getCapability(MinetorioCapabilities.PATTERN_LEARN).ifPresent(cap -> {
            CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SyncPatternLearnMessage(cap.serializeNBT()));
        });
    }

    public static void sendToClient(ServerPlayer player, PatternLearnSyncPacket packet) {
        CHANNEL.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}

