package net.eagl.minetorio.network;

import net.eagl.minetorio.gui.screen.TechnologyTreeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenTechnologyTreeScreenPacket {
    public OpenTechnologyTreeScreenPacket() {}

    public OpenTechnologyTreeScreenPacket(FriendlyByteBuf buf) {
        // Якщо потрібно, можна передавати дані
    }

    public void encode(FriendlyByteBuf buf) {
        // Нічого не пишемо, якщо пакет без даних
    }

    public static OpenTechnologyTreeScreenPacket decode(FriendlyByteBuf buf) {
        return new OpenTechnologyTreeScreenPacket();
    }

    public static void handle(OpenTechnologyTreeScreenPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Відкриваємо екран на клієнті
            Minecraft.getInstance().setScreen(new TechnologyTreeScreen());
        });
        ctx.get().setPacketHandled(true);
    }
}

