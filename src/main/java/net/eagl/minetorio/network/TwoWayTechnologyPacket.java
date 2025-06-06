package net.eagl.minetorio.network;

import net.eagl.minetorio.network.client.ClientTechnologyData;
import net.eagl.minetorio.network.server.ServerTechnologyHandler;
import net.eagl.minetorio.util.Technology;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TwoWayTechnologyPacket {
    private final List<Technology> techList;

    public TwoWayTechnologyPacket(List<Technology> techList) {
        this.techList = techList;
    }

    public TwoWayTechnologyPacket(FriendlyByteBuf buf) {
        int size = buf.readInt();
        this.techList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            CompoundTag tag = buf.readNbt();
            Technology tech = Technology.fromNBT(tag);
            this.techList.add(tech != null ? tech : Technology.EMPTY);
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(techList.size());
        for (Technology tech : techList) {
            buf.writeNbt(tech.serializeNBT());
        }
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = contextSupplier.get();
        ctx.enqueueWork(() -> {
            if (ctx.getDirection().getReceptionSide().isClient()) {
                // Отримано на клієнті
                ClientTechnologyData.set(techList);
            } else {
                // Отримано на сервері
                ServerTechnologyHandler.process(ctx.getSender(), techList);
            }
        });
        ctx.setPacketHandled(true);
    }
}

