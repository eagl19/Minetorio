package net.eagl.minetorio.gui.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class FlaskSlot extends SlotItemHandler {

    private final Item flask;

    public FlaskSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Item pFlask) {
        super(itemHandler, index, xPosition, yPosition);
        this.flask = pFlask;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        if (stack.getItem() != flask) return false;
        ItemStack current = getItem();
        return current.isEmpty() || ItemStack.isSameItemSameTags(current, stack);
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return true;
    }

    public boolean isVisible() {
        return !getItem().isEmpty();
    }

    public Item getFlask(){
        return flask;
    }
}
