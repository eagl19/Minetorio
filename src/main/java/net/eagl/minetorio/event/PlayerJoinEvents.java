package net.eagl.minetorio.event;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.custom.GlowingBedrockBlock;
import net.eagl.minetorio.block.custom.GlowingBedrockBlockState;
import net.eagl.minetorio.worldgen.dimension.MinetorioDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Minetorio.MOD_ID)
public class PlayerJoinEvents {

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        // Уникаємо повторної телепортації
        if (player.getPersistentData().getBoolean("minetorio_teleported")) return;

        ServerLevel minetorioLevel = player.server.getLevel(MinetorioDimensions.MINETORIO_DIM_EMPTY_LEVEL_KEY);

        if (minetorioLevel == null) return;

        BlockPos spawnPos = new BlockPos(0, 100, 0);

        player.teleportTo(minetorioLevel,
                spawnPos.getX() + 0.5,
                spawnPos.getY(),
                spawnPos.getZ() + 0.5,
                player.getYRot(),
                player.getXRot());

        Block block = MinetorioBlocks.GLOWING_BEDROCK.get();
        BlockState state = block.defaultBlockState().setValue(GlowingBedrockBlock.STATE, GlowingBedrockBlockState.BEDROCK);

        for (int dx = 3; dx >= -3; dx--) {
            for (int dz = 3; dz >= -3; dz--) {
                minetorioLevel.setBlockAndUpdate(spawnPos.offset(dx, -1, dz), state);
                minetorioLevel.setBlockAndUpdate(spawnPos.offset(dx,  0, dz), Blocks.AIR.defaultBlockState());
                minetorioLevel.setBlockAndUpdate(spawnPos.offset(dx,  1, dz), Blocks.AIR.defaultBlockState());
                minetorioLevel.setBlockAndUpdate(spawnPos.offset(dx,  2, dz), Blocks.AIR.defaultBlockState());
                minetorioLevel.setBlockAndUpdate(spawnPos.offset(dx,  3, dz), Blocks.AIR.defaultBlockState());
                minetorioLevel.setBlockAndUpdate(spawnPos.offset(dx,  4, dz), Blocks.BARRIER.defaultBlockState());
            }
        }
        for(int dx = 3; dx >= -3; dx-- ) {
            minetorioLevel.setBlockAndUpdate(spawnPos.offset(dx,  0, 3), Blocks.BARRIER.defaultBlockState());
            minetorioLevel.setBlockAndUpdate(spawnPos.offset(dx,  1, 3), Blocks.BARRIER.defaultBlockState());
            minetorioLevel.setBlockAndUpdate(spawnPos.offset(dx,  2, 3), Blocks.BARRIER.defaultBlockState());
            minetorioLevel.setBlockAndUpdate(spawnPos.offset(dx,  3, 3), Blocks.BARRIER.defaultBlockState());

            minetorioLevel.setBlockAndUpdate(spawnPos.offset(dx,  0, -3), Blocks.BARRIER.defaultBlockState());
            minetorioLevel.setBlockAndUpdate(spawnPos.offset(dx,  1, -3), Blocks.BARRIER.defaultBlockState());
            minetorioLevel.setBlockAndUpdate(spawnPos.offset(dx,  2, -3), Blocks.BARRIER.defaultBlockState());
            minetorioLevel.setBlockAndUpdate(spawnPos.offset(dx,  3, -3), Blocks.BARRIER.defaultBlockState());

            minetorioLevel.setBlockAndUpdate(spawnPos.offset(3,  0, dx), Blocks.BARRIER.defaultBlockState());
            minetorioLevel.setBlockAndUpdate(spawnPos.offset(3,  1, dx), Blocks.BARRIER.defaultBlockState());
            minetorioLevel.setBlockAndUpdate(spawnPos.offset(3,  2, dx), Blocks.BARRIER.defaultBlockState());
            minetorioLevel.setBlockAndUpdate(spawnPos.offset(3,  3, dx), Blocks.BARRIER.defaultBlockState());

            minetorioLevel.setBlockAndUpdate(spawnPos.offset(-3,  0, dx), Blocks.BARRIER.defaultBlockState());
            minetorioLevel.setBlockAndUpdate(spawnPos.offset(-3,  1, dx), Blocks.BARRIER.defaultBlockState());
            minetorioLevel.setBlockAndUpdate(spawnPos.offset(-3,  2, dx), Blocks.BARRIER.defaultBlockState());
            minetorioLevel.setBlockAndUpdate(spawnPos.offset(-3,  3, dx), Blocks.BARRIER.defaultBlockState());
        }


        // Позначаємо, що телепортація вже виконана
        player.getPersistentData().putBoolean("minetorio_teleported", true);
    }
}

