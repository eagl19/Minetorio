package net.eagl.minetorio.util.storage;

import net.eagl.minetorio.item.custom.UpgradeItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Arrays;

public class UpgradeStorage extends ItemStackHandler {

    private final Runnable onChange;
    private final float[] multiplier;
    private final int size;
    public UpgradeStorage(int size, Runnable change){
        super(size);
        this.size = size;
        multiplier = new float[this.size];
        Arrays.fill(multiplier, 0.0f);
        this.onChange = change;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);

        if (getStackInSlot(slot).getItem() instanceof UpgradeItem item) {
            multiplier[slot] = item.getMultiplier();
        }else {
            multiplier[slot] = 0.0f;
        }
        if (onChange != null) {
            this.onChange.run();
        }
    }

    public int getSize(){
        return size;
    }

    public float getMultiplier(){
        float mul = 1f;
        for (int i=0; i<size; i++){
            mul = mul * (1f + multiplier[i]);
        }
        return mul;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        for(int i = 0; i < this.getSlots(); i++){
            if(getStackInSlot(i).getItem() instanceof UpgradeItem item) {
                multiplier[i] = item.getMultiplier();
            }
        }
    }
}
