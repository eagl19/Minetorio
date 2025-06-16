package net.eagl.minetorio.block.custom;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum GeneratorState implements StringRepresentable {
    STABILIZED,
    UNSTABLE;

    @Override
    public @NotNull String getSerializedName() {
        return name().toLowerCase();
    }
}
