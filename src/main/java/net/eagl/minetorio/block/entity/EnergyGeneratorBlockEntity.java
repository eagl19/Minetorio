package net.eagl.minetorio.block.entity;

import net.eagl.minetorio.block.custom.EnergyGenerator;
import net.eagl.minetorio.block.custom.MinetorioBlockState;
import net.eagl.minetorio.gui.menu.EnergyGeneratorMenu;
import net.eagl.minetorio.util.CachedBlockPos;
import net.eagl.minetorio.util.enums.ResourceType;
import net.eagl.minetorio.util.storage.MinetorioEnergyStorage;
import net.eagl.minetorio.util.storage.UpgradeStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

public class EnergyGeneratorBlockEntity extends BlockEntity implements MenuProvider, IGeneratorBlockEntity {

    public static final int ENERGY = 0;
    public static final int MAX_ENERGY = 1;
    public static final int PRODUCE = 2;
    public static final int MAX_PRODUCE = 3;
    public static final int MAX_CONSUMERS = 4;
    private final ContainerData containerData = new SimpleContainerData(5);

    public static final int MAX_ENERGY_STORAGE = 1000000;

    public static final int MAX_RECEIVE_ENERGY = 1000000;
    public static final int MAX_EXTRACT_ENERGY = 1000000;

    public static final int START_ENERGY_STORAGE = 250000;

    private static final int MAX_TRANSFER_AMOUNT = 1000;
    private static final int TRANSFER_TIME = 100;

    private static final int BASE_GENERATE_AMOUNT = 1000;

    private final MinetorioEnergyStorage energyStorage = new MinetorioEnergyStorage(MAX_ENERGY_STORAGE,
            MAX_RECEIVE_ENERGY, MAX_EXTRACT_ENERGY, START_ENERGY_STORAGE, this::setChanged);
    private final LazyOptional<IEnergyStorage> optionalEnergy = LazyOptional.of(() -> energyStorage);

    private final CachedBlockPos cachedEnergyTargets = new CachedBlockPos();

    private int currentTime;
    private int currentTransfer;
    private final int timeInterval = 100;
    private boolean permanentlyStabilized;
    private int generateAmount;

    private final UpgradeStorage upgrades = new UpgradeStorage(5, this::onUpdateChange);

    public EnergyGeneratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MinetorioBlockEntities.ENERGY_GENERATOR_ENTITY.get(), pPos, pBlockState);
        this.currentTime = timeInterval;
        this.currentTransfer = timeInterval;
        this.permanentlyStabilized = false;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.minetorio.energy_generator");
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        optionalEnergy.invalidate();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        initializedTargets();
        if (cachedEnergyTargets.getConsumers().isEmpty() && !cachedEnergyTargets.getListPos().isEmpty()) {
            cachedEnergyTargets.getConsumers().add(cachedEnergyTargets.getListPos().get(0));
        }
    }

    private void onUpdateChange(){
        generateAmount = Math.round(BASE_GENERATE_AMOUNT * upgrades.getMultiplier());
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new EnergyGeneratorMenu(pContainerId, pPlayerInventory, this);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return optionalEnergy.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);

        tag.putBoolean("PermanentlyStabilized", permanentlyStabilized);
        tag.putInt("currentTime", currentTime);
        tag.putInt("currentTransfer", currentTransfer);

        tag.put("Energy", energyStorage.serializeNBT());
        tag.put("cachedEnergyTargets", cachedEnergyTargets.serializeNBT());
        tag.put("upgrades", upgrades.serializeNBT());
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        if(tag.contains("PermanentlyStabilized")) {
            permanentlyStabilized = tag.getBoolean("PermanentlyStabilized");
        }

        if(tag.contains("currentTime")) {
            currentTime = tag.getInt("currentTime");
        }

        if(tag.contains("currentTransfer")) {
            currentTransfer = tag.getInt("currentTransfer");
        }

        if (tag.contains("Energy")) {
            energyStorage.deserializeNBT(tag.get("Energy"));
        }

        if (tag.contains("cachedEnergyTargets")){
            cachedEnergyTargets.deserializeNBT(tag.getCompound("cachedEnergyTargets"));
        }

        if (tag.contains("upgrades")){
            upgrades.deserializeNBT(tag.getCompound("upgrades"));
            onUpdateChange();
        }
    }

    public UpgradeStorage getUpgrades(){
        return upgrades;
    }

    public ContainerData getContainerData() {
        return containerData;
    }

    public void updateContainerData() {
        containerData.set(ENERGY, energyStorage.getEnergyStored());
        containerData.set(MAX_ENERGY, energyStorage.getMaxEnergyStored());
        containerData.set(PRODUCE, currentTime);
        containerData.set(MAX_PRODUCE, timeInterval);
    }

    public void tickClient() {
    }

    public void tickServer() {
        boolean changed = false;
        if(getBlockState().getValue(EnergyGenerator.STATE) == MinetorioBlockState.STABILIZED) {
            this.currentTime--;
            this.currentTransfer--;
            int energy = energyStorage.receiveEnergy(generateAmount, true);
            if (energy > 0 && currentTime < 1) {
                energyStorage.receiveEnergy(energy, false);
                this.currentTime = timeInterval;
                changed = true;
            }
            updateContainerData();

            if(currentTransfer < 1){
                Level level = getLevel();
                if (!permanentlyStabilized && level != null) {
                    level.setBlock(getBlockPos(), getBlockState().setValue(EnergyGenerator.STATE, MinetorioBlockState.UNSTABLE), 2);
                    currentTime = timeInterval;
                }
                if (transferEnergyToTargets()) {
                    this.currentTransfer = TRANSFER_TIME;
                    changed = true;
                }
            }
        }
        if(changed){
            setChanged();
        }
    }

    public void initializedTargets() {
        cachedEnergyTargets.initialize(level, getBlockPos(), ResourceType.ENERGY);
    }

    private boolean transferEnergyToTargets() {
        if (level == null) return false;
        AtomicBoolean transferred = new AtomicBoolean(false);
        LazyOptional<IEnergyStorage> optionalFrom = this.getCapability(ForgeCapabilities.ENERGY, null);
        optionalFrom.ifPresent(from -> {

            for (BlockPos pos : cachedEnergyTargets.getConsumers()) {
                BlockEntity targetBE = level.getBlockEntity(pos);
                if (targetBE == null) continue;

                LazyOptional<IEnergyStorage> optionalTo = targetBE.getCapability(ForgeCapabilities.ENERGY, null);
                optionalTo.ifPresent(to -> {
                    int toReceive = to.receiveEnergy(MAX_TRANSFER_AMOUNT, true);
                    if (toReceive > 0) {
                        int extracted = from.extractEnergy(toReceive, false);
                        to.receiveEnergy(extracted, false);
                        if (extracted > 0) {
                            transferred.set(true);
                        }
                    }
                });

            }
        });
        return transferred.get();
    }

    public boolean getPermanentlyStabilized(){
        return permanentlyStabilized;
    }
     public void setPermanentlyStabilized(boolean stabilized){
        permanentlyStabilized = stabilized;
     }

    public CachedBlockPos getCachedEnergyTargets() {
        return cachedEnergyTargets;
    }

    @Override
    public CachedBlockPos getCachedTargets() {
        return cachedEnergyTargets;
    }
}
