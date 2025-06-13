package net.eagl.minetorio.network.server;

import net.eagl.minetorio.block.entity.ResearcherBlockEntity;
import net.eagl.minetorio.network.MinetorioNetwork;
import net.eagl.minetorio.network.client.ResearchListSyncToClientPacket;
import net.eagl.minetorio.util.ResearchPlan;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class RemoveResearcherPlanPacket {

    private final BlockPos blockPos;
    private final int index;

    public RemoveResearcherPlanPacket(BlockPos blockPos, int index) {
        this.blockPos = blockPos;
        this.index = index;
    }

    public RemoveResearcherPlanPacket(FriendlyByteBuf buf) {
        this.blockPos = buf.readBlockPos();
        this.index = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeInt(index);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            Level level = player.level();
            if (!level.isLoaded(blockPos)) return;

            if (level.getBlockEntity(blockPos) instanceof ResearcherBlockEntity researcher) {
                ResearchPlan plan = researcher.getResearchPlan();
                boolean changed = plan.remove(index);
                if (changed) {
                    MinetorioNetwork.CHANNEL.send(
                            PacketDistributor.PLAYER.with(() -> player),
                            new ResearchListSyncToClientPacket(blockPos, plan.getPlan()));
                }
            }
        });
        context.setPacketHandled(true);
    }
}
