package net.eagl.minetorio.event;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.capability.MinetorioCapabilities;
import net.eagl.minetorio.capability.PatternLearnProvider;
import net.eagl.minetorio.capability.Technology.TechnologyProgressProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Minetorio.MOD_ID)
public class CapabilityAttachHandler {

    private static final ResourceLocation PATTERN_LEARN_CAP = ResourceLocation.fromNamespaceAndPath(Minetorio.MOD_ID, "pattern_learn");
    private static final ResourceLocation TECH_PROGRESS_ID = ResourceLocation.fromNamespaceAndPath("minetorio", "tech_progress");

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(PATTERN_LEARN_CAP, new PatternLearnProvider());
            event.addCapability(TECH_PROGRESS_ID, new TechnologyProgressProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        event.getOriginal().getCapability(MinetorioCapabilities.TECHNOLOGY_PROGRESS).ifPresent(oldCap ->
                event.getEntity().getCapability(MinetorioCapabilities.TECHNOLOGY_PROGRESS).ifPresent(newCap ->
                        newCap.setLearnedTechnologies(oldCap.getLearnedTechnologies())));
    }
}
