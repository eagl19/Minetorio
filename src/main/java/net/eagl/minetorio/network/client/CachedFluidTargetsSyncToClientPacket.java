package net.eagl.minetorio.network.client;

import net.eagl.minetorio.block.entity.AbstractFluidGeneratorBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CachedFluidTargetsSyncToClientPacket {
    private final BlockPos generatorPos;
    private final List<BlockPos> targetPositions;

    public CachedFluidTargetsSyncToClientPacket(BlockPos generatorPos, List<BlockPos> targetPositions) {
        this.generatorPos = generatorPos;
        this.targetPositions = targetPositions;
    }

    public static void encode(CachedFluidTargetsSyncToClientPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.generatorPos);
        buf.writeInt(msg.targetPositions.size());
        for (BlockPos pos : msg.targetPositions) {
            buf.writeBlockPos(pos);
        }
    }

    public static CachedFluidTargetsSyncToClientPacket decode(FriendlyByteBuf buf) {
        BlockPos generatorPos = buf.readBlockPos();
        int size = buf.readInt();
        List<BlockPos> targets = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            targets.add(buf.readBlockPos());
        }
        return new CachedFluidTargetsSyncToClientPacket(generatorPos, targets);
    }

    public static void handle(CachedFluidTargetsSyncToClientPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            if (level != null && level.getBlockEntity(msg.generatorPos) instanceof AbstractFluidGeneratorBlockEntity be) {
                be.setCachedFluidTargets(msg.targetPositions);


            }
        });
        ctx.get().setPacketHandled(true);
    }
}
