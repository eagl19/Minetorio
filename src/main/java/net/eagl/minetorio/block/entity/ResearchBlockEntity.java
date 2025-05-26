package net.eagl.minetorio.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ResearchBlockEntity extends BlockEntity {

    public ResearchBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MinetorioBlockEntities.RESEARCH_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);

    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);

    }
}
