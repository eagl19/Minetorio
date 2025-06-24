package net.eagl.minetorio.util.storage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.energy.EnergyStorage;

public class MinetorioEnergyStorage extends EnergyStorage {

    private final Runnable onEnergyChanged;

    public MinetorioEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy, Runnable onEnergyChanged) {
        super(capacity, maxReceive, maxExtract, energy);
        this.onEnergyChanged = onEnergyChanged;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int received = super.receiveEnergy(maxReceive, simulate);
        if (received > 0 && !simulate && onEnergyChanged != null) {
            onEnergyChanged.run();
        }
        return received;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extracted = super.extractEnergy(maxExtract, simulate);
        if (extracted > 0 && !simulate && onEnergyChanged != null) {
            onEnergyChanged.run();
        }
        return extracted;
    }

    @Override
    public Tag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Energy", energy);
        return tag;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        if (nbt instanceof CompoundTag tag) {
            this.energy = tag.getInt("Energy");
        }
        if (onEnergyChanged != null) {
            onEnergyChanged.run();
        }
    }

}
