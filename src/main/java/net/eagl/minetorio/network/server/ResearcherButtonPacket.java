package net.eagl.minetorio.network.server;

import net.eagl.minetorio.gui.ResearcherMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ResearcherButtonPacket {
    private final int buttonId;

    public ResearcherButtonPacket(int buttonId) {
        this.buttonId = buttonId;
    }

    public static void encode(ResearcherButtonPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.buttonId);
    }

    public static ResearcherButtonPacket decode(FriendlyByteBuf buf) {
        return new ResearcherButtonPacket(buf.readInt());
    }

    public static void handle(ResearcherButtonPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if(player != null) {
                if (player.containerMenu instanceof ResearcherMenu menu) {
                    menu.handleButtonPress(msg.buttonId, player);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
