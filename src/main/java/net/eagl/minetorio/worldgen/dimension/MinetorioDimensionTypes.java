package net.eagl.minetorio.worldgen.dimension;

import net.eagl.minetorio.Minetorio;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.OptionalLong;

public class MinetorioDimensionTypes {

    public static final ResourceKey<DimensionType> MINETORIO_DIM_TYPE = ResourceKey.create(
            Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath(Minetorio.MOD_ID, "minetorio_dim_type")
    );

    public static final ResourceKey<DimensionType> VOID_DIM_TYPE = ResourceKey.create(
            Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath(Minetorio.MOD_ID, "void_dim_type")
    );

    public static void bootstrap(BootstapContext<DimensionType> context) {
        context.register(MINETORIO_DIM_TYPE, new DimensionType(
                OptionalLong.empty(),
                false,
                false,
                false,
                false,
                1.0,
                true,
                false,
                0,
                256,
                256,
                BlockTags.INFINIBURN_OVERWORLD,
                BuiltinDimensionTypes.OVERWORLD_EFFECTS,
                1.0f,
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0))
        );
        context.register(VOID_DIM_TYPE, new DimensionType(
                OptionalLong.of(12000),
                false,
                false,
                false,
                false,
                1.0,
                false,
                false,
                0,
                256,
                256,
                BlockTags.INFINIBURN_END,
                BuiltinDimensionTypes.END_EFFECTS,
                0.0f,
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0))
        );
    }
}
