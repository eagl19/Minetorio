package net.eagl.minetorio.block.entity;

import net.eagl.minetorio.gui.ResearcherMenu;
import net.eagl.minetorio.util.Technology;
import net.eagl.minetorio.util.TechnologyRegistry;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResearcherBlockEntity extends BlockEntity implements MenuProvider {


    private final List<Technology> techList = new ArrayList<>(Collections.nCopies(10, Technology.EMPTY));
    private final ItemStackHandler itemHandler = createItemHandler();
    private final LazyOptional<IItemHandler> optionalHandler = LazyOptional.of(() -> itemHandler);

    private final ContainerData containerData = new SimpleContainerData(8);

    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(12) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    private EnergyStorage energyStorage = createEnergyStorage();
    private int waterStorage;
    private final int maxWaterStorage = 100000;

    private int lavaStorage;
    private final int maxLavaStorage = 100000;

    private final LazyOptional<IEnergyStorage> optionalEnergy = LazyOptional.of(() -> energyStorage);

    private EnergyStorage createEnergyStorage() {
        return new EnergyStorage(10000, 100, 100, 5000) {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                int received = super.receiveEnergy(maxReceive, simulate);
                if (received > 0 && !simulate) {
                    setChanged();
                }
                return received;
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                int extracted = super.extractEnergy(maxExtract, simulate);
                if (extracted > 0 && !simulate) {
                    setChanged();
                }
                return extracted;
            }
        };
    }


    public ResearcherBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MinetorioBlockEntities.RESEARCHER_BLOCK_ENTITY.get(), pPos, pBlockState);

        this.waterStorage = 5000;
        this.lavaStorage = 5000;

        updateContainerData();
    }

    public ContainerData getContainerData() {
        return containerData;
    }

    public void updateContainerData() {
        containerData.set(0, energyStorage.getEnergyStored());
        containerData.set(1, energyStorage.getMaxEnergyStored());
        containerData.set(2, waterStorage);
        containerData.set(3, maxWaterStorage);
        containerData.set(4, lavaStorage);
        containerData.set(5, maxLavaStorage);

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
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        optionalHandler.invalidate();
        optionalEnergy.invalidate();
    }




    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Items", itemHandler.serializeNBT());

        tag.putInt("Energy", energyStorage.getEnergyStored());

        tag.putInt("Water", waterStorage);
        tag.putInt("Lava", lavaStorage);

        var idList = new net.minecraft.nbt.ListTag();
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

        int energy = 5000;
        if (tag.contains("Energy", CompoundTag.TAG_INT)) {
            energy = tag.getInt("Energy");
        }

        energyStorage = new EnergyStorage(100000, 100, 100, energy);

        if (tag.contains("Water", CompoundTag.TAG_INT)) {
            waterStorage = tag.getInt("Water");
        }
        if (tag.contains("Lava", CompoundTag.TAG_INT)) {
            lavaStorage = tag.getInt("Lava");
        }
        System.out.println("energy="+energy+",water="+waterStorage+", lava="+lavaStorage);

        if (tag.contains("TechListIds")) {
            techList.clear();
            var idList = tag.getList("TechListIds", CompoundTag.TAG_COMPOUND);
            for (int i = 0; i < idList.size(); i++) {
                CompoundTag techTag = idList.getCompound(i);
                String id = techTag.getString("Id");
                Technology tech = TechnologyRegistry.get(id);
                System.out.println(id);
                techList.add(tech != null ? tech : Technology.EMPTY);
            }
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

    public ItemStackHandler getItemStackHandler() {
        return itemHandler;
    }

    public void setTechList(List<Technology> pList){
        this.techList.clear();
        this.techList.addAll(pList);
        setChanged();
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
        energyStorage.receiveEnergy(1,false);
        if(waterStorage < maxWaterStorage){
            waterStorage++;
            setChanged();
        }
        if (lavaStorage < maxLavaStorage) {
            lavaStorage++;
            setChanged();
        }
        updateContainerData();
    }

}
