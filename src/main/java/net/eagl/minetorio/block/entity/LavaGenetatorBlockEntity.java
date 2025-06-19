package net.eagl.minetorio.block.entity;

import net.eagl.minetorio.block.custom.GeneratorState;
import net.eagl.minetorio.block.custom.LavaGenerator;
import net.eagl.minetorio.gui.LavaGeneratorMenu;
import net.eagl.minetorio.util.enums.FluidType;
import net.eagl.minetorio.util.storage.MinetorioEnergyStorage;
import net.eagl.minetorio.util.storage.MinetorioFluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LavaGenetatorBlockEntity extends BlockEntity implements MenuProvider {

    public static final int ENERGY = 0;
    public static final int MAX_ENERGY = 1;
    public static final int LAVA = 2;
    public static final int MAX_LAVA = 3;
    public static final int PRODUCE = 4;
    public static final int MAX_PRODUCE = 5;

    private static final int GENERATE_INTERVAL = 100;
    private static final int GENERATE_AMOUNT = 2000;

    private static final int MAX_TRANSFER_AMOUNT = 1000;
    private static final int TRANSFER_TIME = 100;

    public static final int MAX_ENERGY_STORAGE = 1000000;
    public static final int MAX_LAVA_STORAGE = 1000000;

    public static final int MAX_RECEIVE_ENERGY = 100;
    public static final int MAX_EXTRACT_ENERGY = 100;

    public static final int START_ENERGY_STORAGE = 250000;
    public static final int START_LAVA_STORAGE = 250000;

    private final MinetorioFluidStorage fluidStorage = new MinetorioFluidStorage(1,
            new int[]{MAX_LAVA_STORAGE}, new FluidType[]{FluidType.LAVA},
            new int[]{START_LAVA_STORAGE}, this::setChanged);

    private final LazyOptional<IFluidHandler> optionalFluid = LazyOptional.of(() -> fluidStorage);

    private final MinetorioEnergyStorage energyStorage = new MinetorioEnergyStorage(MAX_ENERGY_STORAGE,
            MAX_RECEIVE_ENERGY, MAX_EXTRACT_ENERGY, START_ENERGY_STORAGE, this::setChanged);

    private final LazyOptional<IEnergyStorage> optionalEnergy = LazyOptional.of(() -> energyStorage);

    private final ContainerData containerData = new SimpleContainerData(6);

    private int currentTime;
    private int currentTransfer;
    private boolean permanentlyStabilized = false;

    public LavaGenetatorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MinetorioBlockEntities.LAVA_GENERATOR_ENTITY.get(), pPos, pBlockState);
        this.currentTime = GENERATE_INTERVAL;
        this.currentTransfer = TRANSFER_TIME;
    }

    public ContainerData getContainerData() {
        return containerData;
    }

    public void updateContainerData() {
        containerData.set(ENERGY, energyStorage.getEnergyStored());
        containerData.set(MAX_ENERGY, energyStorage.getMaxEnergyStored());
        containerData.set(LAVA, fluidStorage.getFluidInTank(FluidType.LAVA).getAmount());
        containerData.set(MAX_LAVA, fluidStorage.getTankCapacity(FluidType.LAVA));
        containerData.set(PRODUCE, currentTime);
        containerData.set(MAX_PRODUCE, GENERATE_INTERVAL);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.minetorio.lava_generator");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new LavaGeneratorMenu(pContainerId, pPlayerInventory, this);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);

        tag.putBoolean("PermanentlyStabilized", this.permanentlyStabilized);
        tag.putInt("currentTime", this.currentTime);
        tag.putInt("currentTransfer", this.currentTransfer);

        tag.put("Energy", energyStorage.serializeNBT());

        tag.put("Fluid", fluidStorage.serializeNBT());
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        if(tag.contains("PermanentlyStabilized")) {
            this.permanentlyStabilized = tag.getBoolean("PermanentlyStabilized");
        }

        if(tag.contains("currentTime")) {
            this.currentTime = tag.getInt("currentTime");
        }

        if(tag.contains("currentTransfer")) {
            this.currentTransfer = tag.getInt("currentTransfer");
        }

        if (tag.contains("Energy")) {
            energyStorage.deserializeNBT(tag.get("Energy"));
        }

        if (tag.contains("Fluid", Tag.TAG_COMPOUND)) {
            fluidStorage.deserializeNBT(tag.getCompound("Fluid"));
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        optionalFluid.invalidate();
        optionalEnergy.invalidate();
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return optionalFluid.cast();
        }
        if (cap == ForgeCapabilities.ENERGY) {
            return optionalEnergy.cast();
        }
        return super.getCapability(cap, side);
    }

    public void tickClient() {
    }

    public void tickServer() {
        if (this.getBlockState().getValue(LavaGenerator.STATE) == GeneratorState.STABILIZED) {

            this.currentTransfer--;
            this.currentTime--;
            boolean canEnergy = energyStorage.extractEnergy(1, true) >= 1;
            boolean canLava = fluidStorage.fill(FluidType.LAVA, 1, IFluidHandler.FluidAction.SIMULATE) >= 1;
            if (canEnergy && canLava && currentTime < 1) {
                fluidStorage.fill(FluidType.LAVA, GENERATE_AMOUNT, IFluidHandler.FluidAction.EXECUTE);
                energyStorage.extractEnergy(1, false);
                this.currentTime = GENERATE_INTERVAL;
            }
            updateContainerData();
            if (this.currentTransfer < 1) {
                Level level = this.getLevel();
                if (level != null) {
                    BlockPos targetPos = worldPosition.relative(Direction.WEST);
                    BlockEntity targetBE = level.getBlockEntity(targetPos);

                    if (targetBE instanceof ResearcherBlockEntity researcher) {
                        LazyOptional<IFluidHandler> optionalFrom = this.getCapability(ForgeCapabilities.FLUID_HANDLER, Direction.WEST);
                        LazyOptional<IFluidHandler> optionalTo = researcher.getCapability(ForgeCapabilities.FLUID_HANDLER, Direction.EAST);

                        optionalFrom.ifPresent(from -> optionalTo.ifPresent(to ->
                                FluidUtil.tryFluidTransfer(to, from, MAX_TRANSFER_AMOUNT, true).getAmount()));

                    }

                    if (!permanentlyStabilized) {
                        level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(LavaGenerator.STATE, GeneratorState.UNSTABLE));
                    }
                    this.currentTransfer = TRANSFER_TIME;
                }
            }
        }
    }

    public void setPermanentlyStabilized(boolean value) {
        this.permanentlyStabilized = value;
    }

    public boolean getPermanentlyStabilized(){
        return this.permanentlyStabilized;
    }
}
