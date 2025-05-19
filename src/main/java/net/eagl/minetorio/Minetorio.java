package net.eagl.minetorio;


import com.mojang.logging.LogUtils;
import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.entity.MinetorioBlockEntities;
import net.eagl.minetorio.event.PlayerClickEvents;
import net.eagl.minetorio.event.PlayerLoginEvents;
import net.eagl.minetorio.gui.MinetorioMenus;
import net.eagl.minetorio.item.MinetorioCreativeModTabs;
import net.eagl.minetorio.item.MinetorioItems;
import net.eagl.minetorio.network.MinetorioNetwork;
import net.eagl.minetorio.worldgen.biome.MinetorioTerrablender;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(Minetorio.MOD_ID)
public class Minetorio
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "minetorio";
    // Directly reference a slf4j logger
    @SuppressWarnings("unused")
    private static final Logger LOGGER = LogUtils.getLogger();



    public Minetorio(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        MinetorioBlocks.register(modEventBus);
        MinetorioItems.register(modEventBus);
        MinetorioCreativeModTabs.register(modEventBus);
        MinetorioBlockEntities.register(modEventBus);
        MinetorioMenus.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(PlayerLoginEvents.class);
        MinecraftForge.EVENT_BUS.register(PlayerClickEvents.class);

        MinetorioNetwork.register();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);

    }


    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            MinetorioTerrablender.registerBiomes();
            //SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MOD_ID, MinetorioSurfaceRules.makeRules());
        });
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event){

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @SuppressWarnings("unused")
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {


        }
    }


}
