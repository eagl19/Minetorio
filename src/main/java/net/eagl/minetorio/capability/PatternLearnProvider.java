package net.eagl.minetorio.capability;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PatternLearnProvider implements ICapabilityProvider {

    private final PatternLearn instance = new PatternLearn();
    private final LazyOptional<PatternLearn> optional = LazyOptional.of(() -> instance);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == MinetorioCapabilities.PATTERN_LEARN) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    public void invalidate() {
        optional.invalidate();
    }
}
