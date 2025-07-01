package net.eagl.minetorio.event;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.worldgen.dimension.custom.DimensionManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Minetorio.MOD_ID)
public class PlayerLoginEvents {

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {

        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        if (!player.getPersistentData().getBoolean("minetorio_teleported")) {

            DimensionManager.teleportToPlayerDimension(player);

            player.getPersistentData().putBoolean("minetorio_teleported", true);
        }
    }
}

