package net.eagl.minetorio.worldgen.tree.custom;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.eagl.minetorio.worldgen.tree.MinetorioTrunkPlacerTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class PineTrunkPlacer extends TrunkPlacer {
    public static final Codec<PineTrunkPlacer> CODEC = RecordCodecBuilder.create(pineTrunkPlacerInstance ->
            trunkPlacerParts(pineTrunkPlacerInstance).apply(pineTrunkPlacerInstance, PineTrunkPlacer::new));

    public PineTrunkPlacer(int pBaseHeight, int pHeightRandA, int pHeightRandB) {
        super(pBaseHeight, pHeightRandA, pHeightRandB);
    }

    @Override
    protected @NotNull TrunkPlacerType<?> type() {
        return MinetorioTrunkPlacerTypes.PINE_TRUNK_PLACER.get();
    }

    @Override
    public @NotNull List<FoliagePlacer.FoliageAttachment> placeTrunk(@NotNull LevelSimulatedReader pLevel, @NotNull BiConsumer<BlockPos, BlockState> pBlockSetter,
                                                                     @NotNull RandomSource pRandom, int pFreeTreeHeight, BlockPos pPos, @NotNull TreeConfiguration pConfig) {

        setDirtAt(pLevel, pBlockSetter, pRandom, pPos.below(), pConfig);

        int height = pFreeTreeHeight + pRandom.nextInt(heightRandA, heightRandA + 3) +
                pRandom.nextInt(Math.max(heightRandB, 1), heightRandB + 2);

        for (int i = 0; i < height; i++) {
            BlockPos trunkPos = pPos.above(i);
            placeLog(pLevel, pBlockSetter, pRandom, trunkPos, pConfig);

            if (i % 2 == 0 && pRandom.nextBoolean()) {

                List<Direction> directions = new ArrayList<>(List.of(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST));
                Random javaRandom = new Random(pRandom.nextLong());
                Collections.shuffle(directions, javaRandom);

                int branchesThisLevel = pRandom.nextInt(1, 3);

                for (int j = 0; j < branchesThisLevel; j++) {
                    Direction dir = directions.get(j);
                    int branchLength = pRandom.nextInt(1, 4); // Довжина 1–3 блоки
                    int verticalOffset = pRandom.nextBoolean() ? 0 : (pRandom.nextBoolean() ? -1 : 1);

                    Direction.Axis axis = (dir.getAxis() == Direction.Axis.X) ? Direction.Axis.X : Direction.Axis.Z;
                    BlockState branchState = pConfig.trunkProvider.getState(pRandom, pPos).setValue(RotatedPillarBlock.AXIS, axis);

                    for (int x = 1; x <= branchLength; x++) {
                        BlockPos branchPos = trunkPos.above(verticalOffset).relative(dir, x);
                        pBlockSetter.accept(branchPos, branchState);
                    }
                }
            }
        }

        return ImmutableList.of(new FoliagePlacer.FoliageAttachment(pPos.above(height), 0, false));
    }
}
