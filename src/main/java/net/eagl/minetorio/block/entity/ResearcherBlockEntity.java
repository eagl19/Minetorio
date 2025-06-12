package net.eagl.minetorio.block.entity;

import net.eagl.minetorio.capability.MinetorioCapabilities;
import net.eagl.minetorio.gui.ResearcherMenu;
import net.eagl.minetorio.network.MinetorioNetwork;
import net.eagl.minetorio.network.client.ResearchListSyncToClientPacket;
import net.eagl.minetorio.util.Learner;
import net.eagl.minetorio.util.ResearchPlan;
import net.eagl.minetorio.util.Technology;
import net.eagl.minetorio.util.enums.FluidType;
import net.eagl.minetorio.util.storage.FlaskStorage;
import net.eagl.minetorio.util.storage.MinetorioFluidStorage;
import net.eagl.minetorio.util.storage.MinetorioEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResearcherBlockEntity extends BlockEntity implements MenuProvider {

    public static final int ENERGY = 0;
    public static final int MAX_ENERGY = 1;
    public static final int WATER = 2;
    public static final int MAX_WATER = 3;
    public static final int LAVA = 4;
    public static final int MAX_LAVA = 5;
    public static final int LEARN = 6;
    public static final int MAX_LEARN = 7;

    public static final int MAX_ENERGY_STORAGE = 1000000;
    public static final int MAX_WATER_STORAGE = 1000000;
    public static final int MAX_LAVA_STORAGE = 1000000;

    public static final int MAX_RECEIVE_ENERGY = 100;
    public static final int MAX_EXTRACT_ENERGY = 100;

    public static final int START_ENERGY_STORAGE = 5000;
    public static final int START_WATER_STORAGE = 5000;
    public static final int START_LAVA_STORAGE = 5000;

    private final ResearchPlan researchPlan = new ResearchPlan(this::onResearcherPlanChange);
    private final FlaskStorage itemHandler = new FlaskStorage(this::onFlaskFieldChange);
    private final LazyOptional<IItemHandler> optionalHandler = LazyOptional.of(() -> itemHandler);

    private final ContainerData containerData = new SimpleContainerData(8);

   private final Learner learnTechnology = new Learner(Technology.EMPTY, itemHandler, this::setChanged);

    private final MinetorioEnergyStorage energyStorage = new MinetorioEnergyStorage(MAX_ENERGY_STORAGE,
            MAX_RECEIVE_ENERGY, MAX_EXTRACT_ENERGY, START_ENERGY_STORAGE, this::setChanged);
    private final LazyOptional<IEnergyStorage> optionalEnergy = LazyOptional.of(() -> energyStorage);

    private final MinetorioFluidStorage fluidStorage = new MinetorioFluidStorage(2,
            new int[]{MAX_WATER_STORAGE, MAX_LAVA_STORAGE}, new FluidType[]{FluidType.WATER, FluidType.LAVA},
            new int[]{START_WATER_STORAGE, START_LAVA_STORAGE}, this::setChanged);
    private final LazyOptional<IFluidHandler> optionalFluid = LazyOptional.of(() -> fluidStorage);

    public ResearcherBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MinetorioBlockEntities.RESEARCHER_BLOCK_ENTITY.get(), pPos, pBlockState);
        updateContainerData();
    }

    public ContainerData getContainerData() {
        return containerData;
    }

    public void updateContainerData() {
        containerData.set(ENERGY, energyStorage.getEnergyStored());
        containerData.set(MAX_ENERGY, energyStorage.getMaxEnergyStored());
        containerData.set(WATER, fluidStorage.getFluidInTank(FluidType.WATER).getAmount());
        containerData.set(MAX_WATER, fluidStorage.getTankCapacity(FluidType.WATER));
        containerData.set(LAVA, fluidStorage.getFluidInTank(FluidType.LAVA).getAmount());
        containerData.set(MAX_LAVA, fluidStorage.getTankCapacity(FluidType.LAVA));
        containerData.set(LEARN, learnTechnology.getCurrentTime());
        containerData.set(MAX_LEARN, learnTechnology.getTotalTime());
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return optionalHandler.cast();
        }
        if (cap == ForgeCapabilities.ENERGY) {
            return optionalEnergy.cast();
        }
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return optionalFluid.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        optionalHandler.invalidate();
        optionalEnergy.invalidate();
        optionalFluid.invalidate();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);

        tag.put("Items", itemHandler.serializeNBT());

        tag.put("Energy", energyStorage.serializeNBT());

        tag.put("Fluid", fluidStorage.serializeNBT());

        tag.put("Learn", learnTechnology.serializeNBT());

        tag.put("TechList", researchPlan.serializeNBT());

    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        if (tag.contains("Items", Tag.TAG_COMPOUND)) {
            itemHandler.deserializeNBT(tag.getCompound("Items"));
        }

        if (tag.contains("Energy")) {
            energyStorage.deserializeNBT(tag.get("Energy"));
        }

        if (tag.contains("Fluid", Tag.TAG_COMPOUND)) {
            fluidStorage.deserializeNBT(tag.getCompound("Fluid"));
        }
        if (tag.contains("Learn", Tag.TAG_COMPOUND)) {
            learnTechnology.deserializeNBT(tag.getCompound("Learn"));
        }

        if (tag.contains("TechList", Tag.TAG_COMPOUND)) {
            researchPlan.deserializeNBT(tag.getCompound("TechList"));
        }
        updateContainerData();
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.minetorio.researcher");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new ResearcherMenu(pContainerId, pPlayerInventory, this);
    }

    private void onResearcherPlanChange(){
        var plan = researchPlan.getPlan();
        System.out.println(plan.get(0).getId());
        learnTechnology.setTech(plan.isEmpty() ? Technology.EMPTY : plan.get(0));
        setChanged();
    }
    public void researchTechnologyDone(Player player) {
        if(learnTechnology.isDone()) {
            Technology learnedTechnology = researchPlan.nextTechnology();
            if (learnedTechnology != Technology.EMPTY) {
                player.getCapability(MinetorioCapabilities.TECHNOLOGY_PROGRESS).ifPresent(progress -> {
                    progress.learnTechnology(learnedTechnology.getId());
                });
            }
            if (player instanceof ServerPlayer serverPlayer) {
                MinetorioNetwork.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> serverPlayer),
                        new ResearchListSyncToClientPacket(this.getBlockPos(), this.getResearchPlan().getPlan())
                );
            }
            learnTechnology.clear();
            setChanged();
        }
    }

    public  void  onFlaskFieldChange(){
        learnTechnology.setDirty(true);
        setChanged();
    }

    public FlaskStorage getItemStackHandler() {
        return itemHandler;
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }

    public ResearchPlan getResearchPlan() {
        return this.researchPlan;
    }

    public void tickClient() {

    }

    public void tickServer() {
        int energy = energyStorage.receiveEnergy(1,false);
        int water = fluidStorage.fill(FluidType.WATER, 1, IFluidHandler.FluidAction.EXECUTE);
        int lava = fluidStorage.fill(FluidType.LAVA ,1, IFluidHandler.FluidAction.EXECUTE);
        boolean learn = learnTechnology.learn();

        if (water > 0 || energy > 0 || lava > 0 || learn) {
            setChanged();
            updateContainerData();
        }

    }
}
