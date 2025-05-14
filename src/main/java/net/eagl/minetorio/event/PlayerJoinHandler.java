package net.eagl.minetorio.event;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.custom.GlowingBedrockBlock;
import net.eagl.minetorio.worldgen.dimension.MinetorioDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Minetorio.MOD_ID)
public class PlayerJoinHandler {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        // Уникаємо повторної телепортації
        if (player.getPersistentData().getBoolean("minetorio_teleported")) return;

        // Отримуємо кастомний вимір
        ServerLevel minetorioLevel = player.server.getLevel(MinetorioDimensions.MINETORIO_DIM_LEVEL_KEY);
        if (minetorioLevel == null) return;

        // Спавн позиція (можеш змінити на власну логіку або генерацію структури)
        BlockPos spawnPos = new BlockPos(0, 100, 0);

        // Телепортуємо гравця
        player.teleportTo(minetorioLevel,
                spawnPos.getX() + 0.5,
                spawnPos.getY(),
                spawnPos.getZ() + 0.5,
                player.getYRot(),
                player.getXRot());

        // Генерація платформи
        int[] states = {0, 3, 6, 9, 12, 15};
        Block block = MinetorioBlocks.GLOWING_BEDROCK.get();
        int index = 0;

        for (int dx = 2; dx >= -3; dx--) {
            int numberValue = states[index % states.length];
            for (int dz = 2; dz >= -3; dz--) {
                BlockPos pos = spawnPos.offset(dx, -1, dz);
                BlockState state = block.defaultBlockState().setValue(GlowingBedrockBlock.NUMBER, numberValue);
                minetorioLevel.setBlockAndUpdate(pos, state);
            }
            index++;
        }

        // Позначаємо, що телепортація вже виконана
        player.getPersistentData().putBoolean("minetorio_teleported", true);
    }
}

