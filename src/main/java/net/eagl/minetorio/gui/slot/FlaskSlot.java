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
        return stack.getItem() == flask;
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return false;
    }

    public boolean isVisible() {
        return !getItem().isEmpty();
    }

    public Item getFlask(){
        return flask;
    }
}
