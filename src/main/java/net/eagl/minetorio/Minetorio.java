package net.eagl.minetorio;

import net.eagl.minetorio.entity.client.MinetorioBoatRenderer;
import net.eagl.minetorio.recipe.ModRecipes;
import net.eagl.minetorio.screen.GemPolishingStationScreen;
import com.mojang.logging.LogUtils;
import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.entity.MinetorioBlockEntities;
import net.eagl.minetorio.entity.MinetorioEntities;
import net.eagl.minetorio.item.MinetorioCreativeModTabs;
import net.eagl.minetorio.item.MinetorioItems;
import net.eagl.minetorio.loot.MinetorioLootModifiers;
import net.eagl.minetorio.screen.MinetorioMenuTypes;
import net.eagl.minetorio.sound.MinetorioSounds;
import net.eagl.minetorio.util.MinetorioWoodTypes;
import net.eagl.minetorio.villager.MinetorioVillagers;
import net.eagl.minetorio.entity.client.RhinoRenderer;
import net.eagl.minetorio.worldgen.tree.MinetorioTrunkPlacerTypes;
import net.eagl.minetorio.worldgen.tree.MinetorioFoliagePlacers;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
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
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import org.slf4j.Logger;

import java.util.Objects;

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

        MinetorioItems.register(modEventBus);
        MinetorioBlocks.register(modEventBus);

        MinetorioCreativeModTabs.register(modEventBus);
        MinetorioLootModifiers.register(modEventBus);
        MinetorioVillagers.register(modEventBus);
        MinetorioSounds.register(modEventBus);
        MinetorioEntities.register(modEventBus);
        MinetorioBlockEntities.register(modEventBus);
        MinetorioMenuTypes.register(modEventBus);
        MinetorioTrunkPlacerTypes.register(modEventBus);
        MinetorioFoliagePlacers.register(modEventBus);
        ModRecipes.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

    }

    public static ResourceLocation resourceLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> ((FlowerPotBlock) Blocks.FLOWER_POT)
                .addPlant(Objects.requireNonNull(MinetorioBlocks.CATMINT.getId()), MinetorioBlocks.POTTED_CATMINT));
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event){
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(MinetorioItems.SAPPHIRE);
            event.accept(MinetorioItems.RAW_SAPPHIRE);
        }
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
            Sheets.addWoodType(MinetorioWoodTypes.PINE);

            EntityRenderers.register(MinetorioEntities.RHINO.get(), RhinoRenderer::new);
            EntityRenderers.register(MinetorioEntities.MOD_BOAT.get(), pContext -> new MinetorioBoatRenderer(pContext, false));
            EntityRenderers.register(MinetorioEntities.MOD_CHEST_BOAT.get(), pContext -> new MinetorioBoatRenderer(pContext, true));

            EntityRenderers.register(MinetorioEntities.DICE_PROJECTILE.get(), ThrownItemRenderer::new);

            MenuScreens.register(MinetorioMenuTypes.GEM_POLISHING_MENU.get(), GemPolishingStationScreen::new);
        }
    }


}
