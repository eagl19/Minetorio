package net.eagl.minetorio.network.client;

import net.eagl.minetorio.block.entity.ResearcherBlockEntity;
import net.eagl.minetorio.gui.screen.ResearcherScreen;
import net.eagl.minetorio.util.Technology;
import net.eagl.minetorio.util.TechnologyRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ResearchListSyncToClientPacket {
    private final BlockPos pos;
    private final List<Technology> techList;

    public ResearchListSyncToClientPacket(BlockPos pos, List<Technology> techList) {
        this.pos = pos;
        this.techList = techList;
    }

    public static void encode(ResearchListSyncToClientPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeInt(msg.techList.size());
        for (Technology tech : msg.techList) {
            buf.writeUtf(tech.getId());
        }
    }

    public static ResearchListSyncToClientPacket decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        int size = buf.readInt();
        List<Technology> techList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Technology tech = TechnologyRegistry.get(buf.readUtf());
            techList.add(tech != null ? tech : Technology.EMPTY);
        }
        return new ResearchListSyncToClientPacket(pos, techList);
    }

    public static void handle(ResearchListSyncToClientPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            if (level != null && level.getBlockEntity(msg.pos) instanceof ResearcherBlockEntity be) {
                be.getResearchPlan().setPlan(msg.techList);

                Minecraft.getInstance().execute(() -> {
                    if (Minecraft.getInstance().screen instanceof ResearcherScreen screen) {
                        screen.updateTechnologies();
                    }
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

