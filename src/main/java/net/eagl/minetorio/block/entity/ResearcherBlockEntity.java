package net.eagl.minetorio.block.entity;

import net.eagl.minetorio.gui.ResearcherMenu;
import net.eagl.minetorio.util.Technology;
import net.eagl.minetorio.util.TechnologyRegistry;
import net.eagl.minetorio.util.storage.FlaskStorage;
import net.eagl.minetorio.util.storage.MinetorioFluidStorage;
import net.eagl.minetorio.util.storage.MinetorioEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    private final List<Technology> techList = new ArrayList<>(Collections.nCopies(10, Technology.EMPTY));
    private final FlaskStorage itemHandler = new FlaskStorage(this::setChanged);
    private final LazyOptional<IItemHandler> optionalHandler = LazyOptional.of(() -> itemHandler);

    private final ContainerData containerData = new SimpleContainerData(8);

    private int learn;
    private int maxLearn;

    private final MinetorioEnergyStorage energyStorage = new MinetorioEnergyStorage(MAX_ENERGY_STORAGE, MAX_RECEIVE_ENERGY, MAX_EXTRACT_ENERGY, START_ENERGY_STORAGE, this::setChanged);
    private final LazyOptional<IEnergyStorage> optionalEnergy = LazyOptional.of(() -> energyStorage);

    private final MinetorioFluidStorage waterStorage = new MinetorioFluidStorage(MAX_WATER_STORAGE, this::setChanged);
    private final LazyOptional<IFluidHandler> optionalWater = LazyOptional.of(() -> waterStorage);

    private final MinetorioFluidStorage lavaStorage = new MinetorioFluidStorage(MAX_LAVA_STORAGE, this::setChanged);
    private final LazyOptional<IFluidHandler> optionalLava = LazyOptional.of(() -> lavaStorage);

    public ResearcherBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MinetorioBlockEntities.RESEARCHER_BLOCK_ENTITY.get(), pPos, pBlockState);

        waterStorage.fill(new FluidStack(Fluids.WATER, START_WATER_STORAGE), IFluidHandler.FluidAction.EXECUTE);
        lavaStorage.fill(new FluidStack(Fluids.LAVA, START_LAVA_STORAGE), IFluidHandler.FluidAction.EXECUTE);
        this.learn = 0;
        updateContainerData();
    }

    public ContainerData getContainerData() {
        return containerData;
    }

    public void updateContainerData() {
        containerData.set(ENERGY, energyStorage.getEnergyStored());
        containerData.set(MAX_ENERGY, energyStorage.getMaxEnergyStored());
        containerData.set(WATER, waterStorage.getFluidInTank(0).getAmount());
        containerData.set(MAX_WATER, waterStorage.getTankCapacity(0));
        containerData.set(LAVA, lavaStorage.getFluidInTank(0).getAmount());
        containerData.set(MAX_LAVA, lavaStorage.getTankCapacity(0));
        containerData.set(LEARN, learn);
        containerData.set(MAX_LEARN, maxLearn);
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
            return optionalWater.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        optionalHandler.invalidate();
        optionalEnergy.invalidate();
        optionalWater.invalidate();
        optionalLava.invalidate();
    }




    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Items", itemHandler.serializeNBT());

        tag.put("Energy", energyStorage.serializeNBT());

        tag.put("Water", waterStorage.serializeNBT());
        tag.put("Lava", lavaStorage.serializeNBT());
        tag.putInt("Learn", learn);

        var idList = new ListTag();
        for (Technology tech : techList) {
            CompoundTag techTag = new CompoundTag();
            techTag.putString("Id", tech.getId());
            idList.add(techTag);
        }
        tag.put("TechListIds", idList);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Items")) {
            itemHandler.deserializeNBT(tag.getCompound("Items"));
        }

        if (tag.contains("Energy")) {
            energyStorage.deserializeNBT(tag.get("Energy"));
        }

        if (tag.contains("Water")) {
            waterStorage.deserializeNBT(tag.getCompound("Water"));
        }
        if (tag.contains("Lava")) {
            lavaStorage.deserializeNBT(tag.getCompound("Lava"));
        }
        if (tag.contains("Learn", CompoundTag.TAG_INT)) {
            learn = tag.getInt("Learn");
        }

        if (tag.contains("TechListIds")) {
            techList.clear();
            var idList = tag.getList("TechListIds", CompoundTag.TAG_COMPOUND);
            for (int i = 0; i < idList.size(); i++) {
                CompoundTag techTag = idList.getCompound(i);
                String id = techTag.getString("Id");
                Technology tech = TechnologyRegistry.get(id);
                techList.add(tech != null ? tech : Technology.EMPTY);
            }
        }
        if(!techList.get(0).equals(Technology.EMPTY)) {
            maxLearn = techList.get(0).getTime();
        }
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Researcher");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new ResearcherMenu(pContainerId, pPlayerInventory, this);
    }

    public FlaskStorage getItemStackHandler() {
        return itemHandler;
    }

    public void setTechList(List<Technology> pList){
        this.techList.clear();
        this.techList.addAll(pList);
        setChanged();
        maxLearn = techList.get(0).getTime();
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }

    public List<Technology> getTechList() {
        return techList;
    }

    public void tickClient() {

    }

    public void tickServer() {
        boolean change = false;
        int energy = energyStorage.receiveEnergy(1,false);
        int water = waterStorage.fill(new FluidStack(Fluids.WATER, 1), IFluidHandler.FluidAction.EXECUTE);
        int lava = lavaStorage.fill(new FluidStack(Fluids.LAVA,1), IFluidHandler.FluidAction.EXECUTE);
        if(learn<maxLearn){
            learn++;
            change = true;
        }
        if (water > 0 || energy > 0 || lava > 0 || change) {
            setChanged();
            updateContainerData();
        }

    }
}
