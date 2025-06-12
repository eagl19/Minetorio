package net.eagl.minetorio.capability.Technology;

import net.minecraft.core.Direction;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TechnologyProgressProvider implements ICapabilityProvider, INBTSerializable<ListTag> {

    public static final Capability<ITechnologyProgress> CAPABILITY =
            CapabilityManager.get(new CapabilityToken<>(){});

    private final TechnologyProgress backend = new TechnologyProgress();
    private final LazyOptional<ITechnologyProgress> optional = LazyOptional.of(() -> backend);

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == CAPABILITY ? optional.cast() : LazyOptional.empty();
    }

    public static ITechnologyProgress get(Player player) {
        return player.getCapability(CAPABILITY).orElseThrow(() ->
                new IllegalStateException("Missing TechnologyProgress capability"));
    }

    @Override
    public ListTag serializeNBT() {
        return backend.serializeNBT();
    }

    @Override
    public void deserializeNBT(ListTag nbt) {
        backend.deserializeNBT(nbt);
    }
}


