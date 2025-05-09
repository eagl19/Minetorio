package net.eagl.minetorio.worldgen.tree;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.worldgen.tree.custom.PineTrunkPlacer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MinetorioTrunkPlacerTypes {
    public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACER =
            DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, Minetorio.MOD_ID);

    public static final RegistryObject<TrunkPlacerType<PineTrunkPlacer>> PINE_TRUNK_PLACER =
            TRUNK_PLACER.register("pine_trunk_placer", () -> new TrunkPlacerType<>(PineTrunkPlacer.CODEC));

    public static void register(IEventBus eventBus) {
        TRUNK_PLACER.register(eventBus);
    }
}
