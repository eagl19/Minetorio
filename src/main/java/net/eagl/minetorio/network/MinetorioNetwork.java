package net.eagl.minetorio.network;

import net.eagl.minetorio.network.client.CachedBlockPosConsumerSyncToClientPacket;
import net.eagl.minetorio.network.client.CachedBlockPosListPosSyncToClientPacket;
import net.eagl.minetorio.network.client.ResearchListSyncToClientPacket;
import net.eagl.minetorio.network.client.SyncTechnologyProgressPacket;
import net.eagl.minetorio.network.server.AddResearcherPlanPacket;
import net.eagl.minetorio.network.server.RemoveResearcherPlanPacket;
import net.eagl.minetorio.network.server.ResearcherButtonPacket;
import net.eagl.minetorio.network.server.WaterGeneratorInitializePacket;
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
        CHANNEL.registerMessage(
                packetId++,
                ResearchListSyncToClientPacket.class,
                ResearchListSyncToClientPacket::encode,
                ResearchListSyncToClientPacket::decode,
                ResearchListSyncToClientPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
            CHANNEL.registerMessage(packetId++,
                    ResearcherButtonPacket.class,
                    ResearcherButtonPacket::encode,
                    ResearcherButtonPacket::decode,
                    ResearcherButtonPacket::handle,
                    Optional.of(NetworkDirection.PLAY_TO_SERVER)
            );
        CHANNEL.registerMessage(packetId++,
                RemoveResearcherPlanPacket.class,
                RemoveResearcherPlanPacket::toBytes,
                RemoveResearcherPlanPacket::new,
                RemoveResearcherPlanPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );
        CHANNEL.registerMessage(packetId++,
                AddResearcherPlanPacket.class,
                AddResearcherPlanPacket::encode,
                AddResearcherPlanPacket::new,
                AddResearcherPlanPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );
        CHANNEL.registerMessage(packetId++,
                SyncTechnologyProgressPacket.class,
                SyncTechnologyProgressPacket::encode,
                SyncTechnologyProgressPacket::decode,
                SyncTechnologyProgressPacket::handle
        );
        CHANNEL.registerMessage(packetId++,
                CachedBlockPosListPosSyncToClientPacket.class,
                CachedBlockPosListPosSyncToClientPacket::encode,
                CachedBlockPosListPosSyncToClientPacket::decode,
                CachedBlockPosListPosSyncToClientPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
        CHANNEL.registerMessage(packetId++,
                CachedBlockPosConsumerSyncToClientPacket.class,
                CachedBlockPosConsumerSyncToClientPacket::encode,
                CachedBlockPosConsumerSyncToClientPacket::decode,
                CachedBlockPosConsumerSyncToClientPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
        CHANNEL.registerMessage(packetId++,
                WaterGeneratorInitializePacket.class,
                WaterGeneratorInitializePacket::encode,
                WaterGeneratorInitializePacket::decode,
                WaterGeneratorInitializePacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );

    }


    public static void sendToClient(ServerPlayer player, PatternLearnSyncPacket packet) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

}


