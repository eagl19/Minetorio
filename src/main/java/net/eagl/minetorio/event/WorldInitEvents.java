package net.eagl.minetorio.event;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.data.MinetorioDimensionSavedData;
import net.eagl.minetorio.worldgen.dimension.MinetorioDimensions;
import net.eagl.minetorio.worldgen.structure.Rooms3x3;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Minetorio.MOD_ID)
public class WorldInitEvents {

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        if (!level.dimension().equals(MinetorioDimensions.MINETORIO_DIM_EMPTY_LEVEL_KEY)) return;

        MinetorioDimensionSavedData data = MinetorioDimensionSavedData.get(level);

        if (!data.isInitialized()) {

            Rooms3x3.create(level, new BlockPos(0,100,0));

            data.markInitialized();
        }
    }
}

