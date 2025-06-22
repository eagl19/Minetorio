package net.eagl.minetorio.network.server;

import net.eagl.minetorio.block.entity.AbstractFluidGeneratorBlockEntity;
import net.eagl.minetorio.network.MinetorioNetwork;
import net.eagl.minetorio.network.client.CachedBlockPosConsumerSyncToClientPacket;
import net.eagl.minetorio.network.client.CachedBlockPosListPosSyncToClientPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class GeneratorInitializePacket {
    private final BlockPos pos;

    public GeneratorInitializePacket(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(GeneratorInitializePacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static GeneratorInitializePacket decode(FriendlyByteBuf buf) {
        return new GeneratorInitializePacket(buf.readBlockPos());
    }

    public static void handle(GeneratorInitializePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();

            if (player != null) {
                BlockEntity be =  player.level().getBlockEntity(msg.pos);
                if (be instanceof AbstractFluidGeneratorBlockEntity blockEntity) {
                    blockEntity.initializedTargets();

                    MinetorioNetwork.CHANNEL.send(
                            PacketDistributor.PLAYER.with(() -> player),
                            new CachedBlockPosListPosSyncToClientPacket(msg.pos, blockEntity.getCachedFluidTargets().getListPos())
                    );
                    MinetorioNetwork.CHANNEL.send(
                            PacketDistributor.PLAYER.with(() -> player),
                            new CachedBlockPosConsumerSyncToClientPacket(msg.pos, blockEntity.getCachedFluidTargets().getConsumers())
                    );
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
