package net.eagl.minetorio.block.entity;

import net.eagl.minetorio.block.custom.GeneratorState;
import net.eagl.minetorio.block.custom.WaterGenerator;
import net.eagl.minetorio.util.enums.FluidType;
import net.eagl.minetorio.util.storage.MinetorioFluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WaterGeneratorBlockEntity extends BlockEntity implements MenuProvider {

    private static final int GENERATE_INTERVAL = 100;

    private final MinetorioFluidStorage fluidStorage = new MinetorioFluidStorage(1,
            new int[]{100000}, new FluidType[]{FluidType.WATER},
            new int[]{100000}, this::setChanged);

    private final LazyOptional<IFluidHandler> optionalFluid = LazyOptional.of(() -> fluidStorage);


    private int currentTime;
    private boolean permanentlyStabilized = false;

    public WaterGeneratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MinetorioBlockEntities.WATER_GENERATOR_ENTITY.get(), pPos, pBlockState);
        this.currentTime = GENERATE_INTERVAL;

    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.minetorio.water_generator");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return null;
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("PermanentlyStabilized", this.permanentlyStabilized);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        this.permanentlyStabilized = tag.getBoolean("PermanentlyStabilized");
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        optionalFluid.invalidate();
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return optionalFluid.cast();
        }
        return super.getCapability(cap, side);
    }

    public void tickClient() {
    }

    public void tickServer() {
        if (this.getBlockState().getValue(WaterGenerator.STATE) == GeneratorState.STABILIZED) {

            this.currentTime--;
            if (this.currentTime < 1) {
                Level level = this.getLevel();
                if (level != null) {
                    BlockPos targetPos = worldPosition.relative(Direction.EAST);
                    BlockEntity targetBE = level.getBlockEntity(targetPos);

                    if (targetBE instanceof ResearcherBlockEntity researcher) {
                        LazyOptional<IFluidHandler> optionalFrom = this.getCapability(ForgeCapabilities.FLUID_HANDLER, Direction.EAST);
                        LazyOptional<IFluidHandler> optionalTo = researcher.getCapability(ForgeCapabilities.FLUID_HANDLER, Direction.WEST);

                        optionalFrom.ifPresent(from -> optionalTo.ifPresent(to -> {
                            int amountTransferred = FluidUtil.tryFluidTransfer(to, from, 1000, true).getAmount();
                        }));
                    }

                    if (!permanentlyStabilized) {
                        level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(WaterGenerator.STATE, GeneratorState.UNSTABLE));
                    }
                    this.currentTime = GENERATE_INTERVAL;
                }
            }
        }
    }

    public void setPermanentlyStabilized(boolean value) {
        this.permanentlyStabilized = value;
    }
}
