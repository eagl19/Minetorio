package net.eagl.minetorio.worldgen.tree.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.eagl.minetorio.worldgen.tree.MinetorioFoliagePlacers;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import org.jetbrains.annotations.NotNull;

public class PineFoliagePlacer extends FoliagePlacer {
    public static final Codec<PineFoliagePlacer> CODEC = RecordCodecBuilder.create(pineFoliagePlacerInstance
            -> foliagePlacerParts(pineFoliagePlacerInstance).and(Codec.intRange(0, 16).fieldOf("height")
            .forGetter(fp -> fp.height)).apply(pineFoliagePlacerInstance, PineFoliagePlacer::new));
    private final int height;

    public PineFoliagePlacer(IntProvider pRadius, IntProvider pOffset, int height) {
        super(pRadius, pOffset);
        this.height = height;
    }

    @Override
    protected @NotNull FoliagePlacerType<?> type() {
        return MinetorioFoliagePlacers.PINE_FOLIAGE_PLACER.get();
    }

    @Override
    protected void createFoliage(@NotNull LevelSimulatedReader pLevel, @NotNull FoliageSetter pBlockSetter, @NotNull RandomSource pRandom, @NotNull TreeConfiguration pConfig,
                                 int pMaxFreeTreeHeight, FoliageAttachment pAttachment, int pFoliageHeight, int pFoliageRadius, int pOffset) {

        BlockPos pos = pAttachment.pos();


        if (pos.getY() >= pMaxFreeTreeHeight + pAttachment.pos().getY() - pFoliageHeight) {
            for (int y = -3; y <= 2; y++) {
                int radius = Math.min(4,Math.abs(3-y));
                placeLeavesRow(pLevel, pBlockSetter, pRandom, pConfig, pos.above(y), radius, radius, pAttachment.doubleTrunk());
            }
            return;
        }

        for (BlockPos leafPos : BlockPos.betweenClosed(pos.offset(-2, 0, -2), pos.offset(2, 2, 2))) {
            if (pos.distManhattan(leafPos) <= 2 && pLevel.isStateAtPosition(leafPos, BlockBehaviour.BlockStateBase::isAir)) {
                pBlockSetter.set(leafPos.immutable(), pConfig.foliageProvider.getState(pRandom, leafPos));
            }
        }

        BlockPos topLeaf = pos.above();
        if (pLevel.isStateAtPosition(topLeaf, BlockBehaviour.BlockStateBase::isAir)) {
            pBlockSetter.set(topLeaf, pConfig.foliageProvider.getState(pRandom, topLeaf));
        }
    }

    @Override
    public int foliageHeight(@NotNull RandomSource pRandom, int pHeight, @NotNull TreeConfiguration pConfig) {
        return this.height;
    }

    @Override
    protected boolean shouldSkipLocation(@NotNull RandomSource pRandom, int pLocalX, int pLocalY, int pLocalZ, int pRange, boolean pLarge) {
        return false;
    }
}
