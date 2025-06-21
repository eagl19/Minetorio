package net.eagl.minetorio.network.server;

import net.eagl.minetorio.block.entity.WaterGeneratorBlockEntity;
import net.eagl.minetorio.network.MinetorioNetwork;
import net.eagl.minetorio.network.client.CachedBlockPosConsumerSyncToClientPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AddConsumersPacket {

    private final BlockPos pos;
    private final List<Integer> index;

    public AddConsumersPacket(BlockPos pos, List<Integer> index) {
        this.pos = pos;
        this.index = index;
    }

    public AddConsumersPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        int size = buf.readInt();
        this.index = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            this.index.add(buf.readInt());
        }
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(index.size());
        for (int i : index) {
            buf.writeInt(i);
        }
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = contextSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player == null) return;

            if (player.level().getBlockEntity(pos) instanceof WaterGeneratorBlockEntity waterGenerator) {
                boolean changed = waterGenerator.getCachedFluidTargets().setConsumersById(index);
                if (changed) {
                    MinetorioNetwork.CHANNEL.send(
                            PacketDistributor.PLAYER.with(() -> player),
                            new CachedBlockPosConsumerSyncToClientPacket(pos, waterGenerator.getCachedFluidTargets().getConsumers()));
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}

