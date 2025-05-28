package net.eagl.minetorio.block.entity;

import net.eagl.minetorio.gui.ResearcherMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResearcherBlockEntity extends BlockEntity implements MenuProvider {

    private final Container container = new SimpleContainer(10);

    public ResearcherBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MinetorioBlockEntities.RESEARCHER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }


    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);

    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);

    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Researcher");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new ResearcherMenu(pContainerId, pPlayerInventory, this);
    }

    public Container getContainer() {
        return container;
    }
}
