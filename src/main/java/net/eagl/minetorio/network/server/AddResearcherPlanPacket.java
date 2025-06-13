package net.eagl.minetorio.network.server;

import net.eagl.minetorio.block.entity.ResearcherBlockEntity;
import net.eagl.minetorio.network.MinetorioNetwork;
import net.eagl.minetorio.network.client.ResearchListSyncToClientPacket;
import net.eagl.minetorio.util.Technology;
import net.eagl.minetorio.util.TechnologyRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class AddResearcherPlanPacket {

    private final BlockPos pos;
    private final String techId;
    private final int index;

    public AddResearcherPlanPacket(BlockPos pos, String techId, int index) {
        this.pos = pos;
        this.techId = techId;
        this.index = index;
    }

    public AddResearcherPlanPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.techId = buf.readUtf();
        this.index = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeUtf(techId);
        buf.writeInt(index);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = contextSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player == null) return;

            if (player.level().getBlockEntity(pos) instanceof ResearcherBlockEntity researcher) {
                Technology tech = TechnologyRegistry.get(techId);
                boolean changed = researcher.getResearchPlan().setPlan(tech, index);
                if (changed) {
                    MinetorioNetwork.CHANNEL.send(
                            PacketDistributor.PLAYER.with(() -> player),
                            new ResearchListSyncToClientPacket(pos, researcher.getResearchPlan().getPlan()));
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}

