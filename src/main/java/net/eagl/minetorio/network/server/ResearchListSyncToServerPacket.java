package net.eagl.minetorio.network.server;

import net.eagl.minetorio.block.entity.ResearcherBlockEntity;
import net.eagl.minetorio.util.Technology;
import net.eagl.minetorio.util.TechnologyRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ResearchListSyncToServerPacket {
    private final BlockPos pos;
    private final List<Technology> techList;

    public ResearchListSyncToServerPacket(BlockPos pos, List<Technology> techList) {
        this.pos = pos;
        this.techList = techList;
    }

    public static void encode(ResearchListSyncToServerPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeInt(msg.techList.size());
        for (Technology tech : msg.techList) {
            buf.writeUtf(tech.getId());
        }
    }

    public static ResearchListSyncToServerPacket decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        int size = buf.readInt();
        List<Technology> techList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Technology tech = TechnologyRegistry.get(buf.readUtf());
            techList.add(tech != null ? tech : Technology.EMPTY);
        }
        return new ResearchListSyncToServerPacket(pos, techList);
    }

    public static void handle(ResearchListSyncToServerPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            Level level = player.level();
            if (level.getBlockEntity(msg.pos) instanceof ResearcherBlockEntity be) {
                be.getResearchPlan().setPlan(msg.techList);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

