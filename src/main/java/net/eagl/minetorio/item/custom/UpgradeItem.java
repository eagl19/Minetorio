package net.eagl.minetorio.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UpgradeItem extends Item {

    private final float multiplier;

    public UpgradeItem(float multiplier) {
        super(new Properties()
                .stacksTo(1));
        this.multiplier = multiplier;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.minetorio.upgrade.multiplier", String.format("%.0f", multiplier * 100))
                .withStyle(ChatFormatting.GREEN));
    }

    public float getMultiplier(){
        return multiplier;
    }
}
