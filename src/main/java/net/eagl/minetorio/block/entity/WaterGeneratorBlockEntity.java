package net.eagl.minetorio.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WaterGeneratorBlockEntity extends BlockEntity implements MenuProvider {
    
    public WaterGeneratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MinetorioBlockEntities.WATER_GENERATOR_ENTITY.get(), pPos, pBlockState);

    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.minetorio.water_generator");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return null;
    }

    public void tickClient() {
    }

    public void tickServer() {
    }
}
