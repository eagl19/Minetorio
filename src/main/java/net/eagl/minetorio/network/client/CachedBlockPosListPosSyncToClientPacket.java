package net.eagl.minetorio.network.client;

import net.eagl.minetorio.block.entity.AbstractFluidGeneratorBlockEntity;
import net.eagl.minetorio.gui.screen.WaterGeneratorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CachedBlockPosListPosSyncToClientPacket {
    private final BlockPos generatorPos;
    private final List<BlockPos> targetPositions;

    public CachedBlockPosListPosSyncToClientPacket(BlockPos generatorPos, List<BlockPos> targetPositions) {
        this.generatorPos = generatorPos;
        this.targetPositions = targetPositions;
    }

    public static void encode(CachedBlockPosListPosSyncToClientPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.generatorPos);
        buf.writeInt(msg.targetPositions.size());
        for (BlockPos pos : msg.targetPositions) {
            buf.writeBlockPos(pos);
        }
    }

    public static CachedBlockPosListPosSyncToClientPacket decode(FriendlyByteBuf buf) {
        BlockPos generatorPos = buf.readBlockPos();
        int size = buf.readInt();
        List<BlockPos> targets = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            targets.add(buf.readBlockPos());
        }
        return new CachedBlockPosListPosSyncToClientPacket(generatorPos, targets);
    }

    public static void handle(CachedBlockPosListPosSyncToClientPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            if (level != null && level.getBlockEntity(msg.generatorPos) instanceof AbstractFluidGeneratorBlockEntity be) {
                be.getCachedFluidTargets().setPos(msg.targetPositions);
            }

            Minecraft.getInstance().execute(() -> {
                if (Minecraft.getInstance().screen instanceof WaterGeneratorScreen screen) {
                    screen.update();
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
