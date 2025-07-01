package net.eagl.minetorio.network.server;

import net.eagl.minetorio.block.entity.PatternsCollectorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PatternCollectorTeleport {

    private final BlockPos pos;


    public PatternCollectorTeleport(BlockPos pos) {
        this.pos = pos;
    }

    public PatternCollectorTeleport(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = contextSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player == null) return;
            if (player.level().getBlockEntity(pos) instanceof PatternsCollectorBlockEntity teleporter) {
                teleporter.teleport();
            }
        });
        ctx.setPacketHandled(true);
    }
}
