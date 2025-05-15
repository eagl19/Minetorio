package net.eagl.minetorio.block.custom;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;

public class GlowingBedrockBlock extends Block {

    public static IntegerProperty NUMBER = IntegerProperty.create("number",0,1);

    public GlowingBedrockBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .strength(-1.0F, 3600000.0F)
                .noLootTable()
                .lightLevel(state -> 15)
                .noOcclusion()
        );
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NUMBER);
    }


}
