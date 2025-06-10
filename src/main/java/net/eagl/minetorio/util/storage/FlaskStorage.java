package net.eagl.minetorio.util.storage;

import net.eagl.minetorio.item.MinetorioItems;
import net.eagl.minetorio.util.FlasksField;
import net.eagl.minetorio.util.enums.FlaskColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
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
        return new FlasksField(
                getFlaskAmount(FlaskColor.RED),
                getFlaskAmount(FlaskColor.GREEN),
                getFlaskAmount(FlaskColor.BLACK),
                getFlaskAmount(FlaskColor.PURPLE),
                getFlaskAmount(FlaskColor.PINK),
                getFlaskAmount(FlaskColor.WHITE),
                getFlaskAmount(FlaskColor.BLUE),
                getFlaskAmount(FlaskColor.YELLOW),
                getFlaskAmount(FlaskColor.BROWN),
                getFlaskAmount(FlaskColor.CYAN),
                getFlaskAmount(FlaskColor.ORANGE),
                getFlaskAmount(FlaskColor.GRAY)
        );
    }

}

