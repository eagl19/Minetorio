package net.eagl.minetorio.network.client;

import net.eagl.minetorio.gui.screen.WaterGeneratorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CachedBlockPosConsumerSyncToClientPacket {
    private final BlockPos generatorPos;
    private final List<BlockPos> targetPositions;

    public CachedBlockPosConsumerSyncToClientPacket(BlockPos generatorPos, List<BlockPos> targetPositions) {
        this.generatorPos = generatorPos;
        this.targetPositions = targetPositions;
    }

    public static void encode(CachedBlockPosConsumerSyncToClientPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.generatorPos);
        buf.writeInt(msg.targetPositions.size());
        for (BlockPos pos : msg.targetPositions) {
            buf.writeBlockPos(pos);
        }
    }

    public static CachedBlockPosConsumerSyncToClientPacket decode(FriendlyByteBuf buf) {
        BlockPos generatorPos = buf.readBlockPos();
        int size = buf.readInt();
        List<BlockPos> targets = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            targets.add(buf.readBlockPos());
        }
        return new CachedBlockPosConsumerSyncToClientPacket(generatorPos, targets);
    }

    public static void handle(CachedBlockPosConsumerSyncToClientPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> Minecraft.getInstance().execute(() -> {
            if (Minecraft.getInstance().screen instanceof WaterGeneratorScreen screen) {
                screen.getFluidTargets().setConsumers(msg.targetPositions);
                screen.update();
            }
        }));
        ctx.get().setPacketHandled(true);
    }
}
