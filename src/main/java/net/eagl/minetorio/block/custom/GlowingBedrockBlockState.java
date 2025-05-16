package net.eagl.minetorio.block.custom;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum GlowingBedrockBlockState implements StringRepresentable {
    BEDROCK,
    INFINITY,
    VOID;

    @Override
    public @NotNull String getSerializedName() {
        return name().toLowerCase();
    }
}