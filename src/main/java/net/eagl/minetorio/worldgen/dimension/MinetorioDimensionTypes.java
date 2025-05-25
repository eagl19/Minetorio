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

    public static void bootstrap(BootstapContext<DimensionType> context) {
        context.register(MINETORIO_DIM_TYPE, new DimensionType(
                OptionalLong.of(12000),  // fixedTime
                false, // hasSkylight
                false, // hasCeiling
                false, // ultraWarm
                false, // natural
                1.0,   // coordinateScale
                true,  // bedWorks
                false, // respawnAnchorWorks
                0,     // minY
                256,   // height
                256,   // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD,
                BuiltinDimensionTypes.OVERWORLD_EFFECTS, // effectsLocation
                1.0f,  // ambientLight
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0))
        );
    }
}
