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
        ItemStack flaskStack = getFlaskStack(color);
        flaskStack.setCount(amount);
        setStackInSlot(slot, flaskStack);
    }

    private ItemStack getFlaskStack(FlaskColor color) {
        Item item;
        switch (color) {
            case RED -> item = MinetorioItems.FLASK_RED.get();
            case GREEN -> item = MinetorioItems.FLASK_GREEN.get();
            case BLACK -> item = MinetorioItems.FLASK_BLACK.get();
            case PURPLE -> item = MinetorioItems.FLASK_PURPLE.get();
            case PINK -> item = MinetorioItems.FLASK_PINK.get();
            case WHITE -> item = MinetorioItems.FLASK_WHITE.get();
            case BLUE -> item = MinetorioItems.FLASK_BLUE.get();
            case YELLOW -> item = MinetorioItems.FLASK_YELLOW.get();
            case BROWN -> item = MinetorioItems.FLASK_BROWN.get();
            case CYAN -> item = MinetorioItems.FLASK_CYAN.get();
            case ORANGE -> item = MinetorioItems.FLASK_ORANGE.get();
            case GRAY -> item = MinetorioItems.FLASK_GRAY.get();
            default -> throw new IllegalArgumentException("Unknown flask color: " + color);
        }
        return new ItemStack(item, 0);
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

