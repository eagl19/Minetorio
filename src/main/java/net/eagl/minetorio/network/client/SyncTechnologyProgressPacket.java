package net.eagl.minetorio.network.client;

import net.eagl.minetorio.capability.MinetorioCapabilities;
import net.eagl.minetorio.capability.Technology.TechnologyProgress;
import net.eagl.minetorio.gui.screen.TechnologyTreeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncTechnologyProgressPacket {
    private final CompoundTag tag;

    public SyncTechnologyProgressPacket(CompoundTag tag) {
        this.tag = tag;
    }

    public static void encode(SyncTechnologyProgressPacket msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.tag);
    }

    public static SyncTechnologyProgressPacket decode(FriendlyByteBuf buf) {
        return new SyncTechnologyProgressPacket(buf.readNbt());
    }

    public static void handle(SyncTechnologyProgressPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            if (player != null) {
                player.getCapability(MinetorioCapabilities.TECHNOLOGY_PROGRESS).ifPresent(progress -> {
                    if (progress instanceof TechnologyProgress tp) {
                        tp.deserializeNBT(msg.tag);
                    }
                });
                if (mc.screen instanceof TechnologyTreeScreen gui) {
                    gui.updateTechnologyProgress();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

