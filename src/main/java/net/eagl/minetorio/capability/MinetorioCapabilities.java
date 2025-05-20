package net.eagl.minetorio.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "minetorio", bus = Mod.EventBusSubscriber.Bus.MOD)
public class MinetorioCapabilities {

    public static final Capability<IPatternLearn> PATTERN_LEARN = CapabilityManager.get(new CapabilityToken<IPatternLearn>() {});

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IPatternLearn.class);
    }
}
