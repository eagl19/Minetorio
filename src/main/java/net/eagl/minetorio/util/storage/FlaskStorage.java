package net.eagl.minetorio.util.storage;

import net.eagl.minetorio.util.FlasksField;
import net.eagl.minetorio.util.enums.FlaskColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class FlaskStorage extends ItemStackHandler {

    private static final int SIZE = 12;
    private final Runnable onChange;
    public FlaskStorage(Runnable onChange) {
        super(SIZE);
        this.onChange = onChange;
    }

    @Override
    public CompoundTag serializeNBT() {
        return super.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
    }

    @Override
    protected void onContentsChanged(int slot) {
        this.onChange.run();
    }

    public int getFlaskAmount(FlaskColor color) {
        int slot = color.ordinal();
        return getStackInSlot(slot).getCount();
    }

    public void setFlaskAmount(FlaskColor color, int amount) {
        int slot = color.ordinal();
        ItemStack flaskStack = FlasksField.getFlask(color);
        flaskStack.setCount(amount);
        setStackInSlot(slot, flaskStack);
    }

    public void loadFromFlasksField(FlasksField flasksField) {
        for (FlaskColor color : FlaskColor.values()) {
            setFlaskAmount(color, flasksField.getFlaskAmount(color));
        }
    }

    public FlasksField toFlasksField() {
        return new FlasksField.Builder()
                .set(FlaskColor.RED, getFlaskAmount(FlaskColor.RED))
                .set(FlaskColor.GREEN, getFlaskAmount(FlaskColor.GREEN))
                .set(FlaskColor.BLACK, getFlaskAmount(FlaskColor.BLACK))
                .set(FlaskColor.PURPLE, getFlaskAmount(FlaskColor.PURPLE))
                .set(FlaskColor.PINK, getFlaskAmount(FlaskColor.PINK))
                .set(FlaskColor.WHITE, getFlaskAmount(FlaskColor.WHITE))
                .set(FlaskColor.BLUE, getFlaskAmount(FlaskColor.BLUE))
                .set(FlaskColor.YELLOW, getFlaskAmount(FlaskColor.YELLOW))
                .set(FlaskColor.BROWN, getFlaskAmount(FlaskColor.BROWN))
                .set(FlaskColor.CYAN, getFlaskAmount(FlaskColor.CYAN))
                .set(FlaskColor.ORANGE, getFlaskAmount(FlaskColor.ORANGE))
                .set(FlaskColor.GRAY, getFlaskAmount(FlaskColor.GRAY))
                .build();
    }

}

