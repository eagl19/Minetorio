package net.eagl.minetorio.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PatternLearnProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    // Вказуємо інтерфейс, а не реалізацію
    private final IPatternLearn instance = new PatternLearn();
    private final LazyOptional<IPatternLearn> optional = LazyOptional.of(() -> instance);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        // Типи будуть сумісні
        return cap == MinetorioCapabilities.PATTERN_LEARN ? optional.cast() : LazyOptional.empty();
    }

    public void invalidate() {
        optional.invalidate();
    }

    @Override
    public CompoundTag serializeNBT() {
        // Cast до PatternLearn для доступу до реалізації
        if (instance instanceof PatternLearn patternLearnImpl) {
            return patternLearnImpl.serializeNBT();
        }
        return new CompoundTag(); // fallback
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (instance instanceof PatternLearn patternLearnImpl) {
            patternLearnImpl.deserializeNBT(nbt);
        }
    }
}
