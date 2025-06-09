package net.eagl.minetorio.network;

import net.eagl.minetorio.network.client.ResearchListSyncToClientPacket;
import net.eagl.minetorio.network.server.ResearchListSyncToServerPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class MinetorioNetwork {
    private static int packetId = 0;

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath("minetorio", "main"),
            () -> "1.0",
            s -> true,
            s -> true
    );

    public static void register() {
        CHANNEL.registerMessage(
                packetId++,
                PatternLearnSyncPacket.class,
                PatternLearnSyncPacket::toBytes,
                PatternLearnSyncPacket::new,
                PatternLearnSyncPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
        CHANNEL.registerMessage(packetId++, ResearchListSyncToServerPacket.class,
                ResearchListSyncToServerPacket::encode,
                ResearchListSyncToServerPacket::decode,
                ResearchListSyncToServerPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(
                packetId++,
                ResearchListSyncToClientPacket.class,
                ResearchListSyncToClientPacket::encode,
                ResearchListSyncToClientPacket::decode,
                ResearchListSyncToClientPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
    }


    public static void sendToClient(ServerPlayer player, PatternLearnSyncPacket packet) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

}


