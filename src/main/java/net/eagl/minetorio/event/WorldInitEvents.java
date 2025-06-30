package net.eagl.minetorio.event;

import net.eagl.minetorio.Minetorio;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Minetorio.MOD_ID)
public class WorldInitEvents {

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
    }
}

