package net.eagl.minetorio.event;

import net.eagl.minetorio.capability.IPatternLearn;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventHandlers {
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        // Реєструємо інтерфейс IPatternLearn як новий тип capability
        event.register(IPatternLearn.class);
    }
}
