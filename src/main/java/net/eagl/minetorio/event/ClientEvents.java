package net.eagl.minetorio.event;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.block.entity.MinetorioBlockEntities;
import net.eagl.minetorio.block.renderer.PatternCollectorBlockRenderer;
import net.eagl.minetorio.block.renderer.ResearcherBlockRenderer;
import net.eagl.minetorio.gui.MinetorioMenus;
import net.eagl.minetorio.gui.screen.PatternsCollectorScreen;
import net.eagl.minetorio.gui.screen.ResearcherScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Minetorio.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {

        event.registerBlockEntityRenderer(
                MinetorioBlockEntities.PATTERNS_COLLECTOR.get(),
                PatternCollectorBlockRenderer::new
        );

        event.registerBlockEntityRenderer(
                MinetorioBlockEntities.RESEARCHER_BLOCK_ENTITY.get(),
                ResearcherBlockRenderer::new
        );
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
                    MenuScreens.register(MinetorioMenus.PATTERNS_COLLECTOR_MENU.get(), PatternsCollectorScreen::new);
                    MenuScreens.register(MinetorioMenus.RESEARCHER_MENU.get(), ResearcherScreen::new);
                }
        );

    }
}
