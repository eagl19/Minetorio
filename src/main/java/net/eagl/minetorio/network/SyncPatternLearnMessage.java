package net.eagl.minetorio.network;

import net.eagl.minetorio.capability.MinetorioCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncPatternLearnMessage {
    private final CompoundTag data;

    // Конструктор для відправки
    public SyncPatternLearnMessage(CompoundTag data) {
        this.data = data;
    }

    // Конструктор для прийому
    public SyncPatternLearnMessage(FriendlyByteBuf buf) {
        this.data = buf.readNbt();
    }

    // Запис у буфер
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(data);
    }

    // Обробник на стороні клієнта
    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                mc.player.getCapability(MinetorioCapabilities.PATTERN_LEARN).ifPresent(cap -> {
                    cap.deserializeNBT(data);
                });
            }
        });
        ctx.get().setPacketHandled(true);
        return true;
    }
}

