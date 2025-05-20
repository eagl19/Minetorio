package net.eagl.minetorio.network;

import net.eagl.minetorio.client.ClientPatternsData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PatternLearnSyncPacket {
    private final Map<String, Boolean> learnedMap;

    public PatternLearnSyncPacket(Map<String, Boolean> learnedMap) {
        this.learnedMap = learnedMap;
    }

    public PatternLearnSyncPacket(FriendlyByteBuf buf) {
        int size = buf.readInt();
        learnedMap = new HashMap<>();
        for (int i = 0; i < size; i++) {
            String key = buf.readUtf(32767);
            boolean value = buf.readBoolean();
            learnedMap.put(key, value);
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(learnedMap.size());
        for (Map.Entry<String, Boolean> entry : learnedMap.entrySet()) {
            buf.writeUtf(entry.getKey());
            buf.writeBoolean(entry.getValue());
        }
    }

    public static void handle(PatternLearnSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () ->
                () -> ClientPatternsData.setLearnedMap(msg.learnedMap)
        ));
        ctx.get().setPacketHandled(true);
    }
}
