package net.eagl.minetorio.util.storage;

import net.eagl.minetorio.item.MinetorioItems;
import net.minecraft.world.item.Item;
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

        Item item = getStackInSlot(slot).getItem();

        if(getStackInSlot(slot).isEmpty()){
            multiplier[slot] = 0.0f;
        }
        else if(item == MinetorioItems.COOLING_CORE.get()){
           multiplier[slot] = 0.15f;
        }
       else if(item == MinetorioItems.HYDRO_CATALYST.get()){
           multiplier[slot] = 0.5f;
       }
       else if(item == MinetorioItems.FILTERED_NOZZLE.get()){
           multiplier[slot] = 0.1f;
       }
       else if(item == MinetorioItems.ENCHANTED_PIPE.get()){
           multiplier[slot] = 0.2f;
       }
       else if(item == MinetorioItems.DEW_COLLECTOR.get()){
           multiplier[slot] = 0.25f;
       }
       else {
           multiplier[slot] = 0.0f;
       }
        if(onChange !=null) {
            this.onChange.run();
        }
    }

    public int getSize(){
        return size;
    }

    public float getMultiplier(){
        float mul = 1;
        for (int i=0; i<size; i++){
            mul = mul * (1 + multiplier[i]);
        }
        return mul;
    }
}
