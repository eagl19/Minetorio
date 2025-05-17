package net.eagl.minetorio.block.custom;


import net.eagl.minetorio.block.entity.MinetorioBeaconBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class MinetorioBeaconBlock extends Block implements EntityBlock {

    public MinetorioBeaconBlock(Properties pProperties) {
        super(BlockBehaviour.Properties.of()
                .strength(3.5F)
                .lightLevel(state -> 15) // світиться
                .noOcclusion());
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new MinetorioBeaconBlockEntity(pos, state);
    }

    @Override
    public void animateTick(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull RandomSource rand) {
        if (!level.isClientSide) return;

        for (Direction direction : Direction.values()) {
            BlockPos target = pos.relative(direction);
            double dx = direction.getStepX() * 0.5;
            double dy = direction.getStepY() * 0.5;
            double dz = direction.getStepZ() * 0.5;

            level.addParticle(ParticleTypes.END_ROD,
                    pos.getX() + 0.5 + dx,
                    pos.getY() + 0.5 + dy,
                    pos.getZ() + 0.5 + dz,
                    dx * 0.1, dy * 0.1, dz * 0.1);
        }
    }


}
