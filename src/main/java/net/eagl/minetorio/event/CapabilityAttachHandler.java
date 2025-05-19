package net.eagl.minetorio.event;

import net.eagl.minetorio.capability.MinetorioCapabilities;
import net.eagl.minetorio.capability.PatternLearnProvider;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraft.resources.ResourceLocation;

@Mod.EventBusSubscriber(modid = "minetorio")
public class CapabilityAttachHandler {

    private static final ResourceLocation PATTERN_LEARN_CAP = ResourceLocation.fromNamespaceAndPath("minetorio", "pattern_learn");

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Player> event) {
        if (!event.getObject().getCapability(MinetorioCapabilities.PATTERN_LEARN).isPresent()) {
            event.addCapability(PATTERN_LEARN_CAP, new PatternLearnProvider());
        }
    }
}
