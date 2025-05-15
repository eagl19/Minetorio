package net.eagl.minetorio.datagen;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.custom.GlowingBedrockBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.data.PackOutput;


public class MinetorioBlockStateProvider extends BlockStateProvider {

    public MinetorioBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Minetorio.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        blockWithStatesAndItem(MinetorioBlocks.GLOWING_BEDROCK.get(),GlowingBedrockBlock.NUMBER);
    }

    private void blockWithStatesAndItem(Block pBlock, IntegerProperty pValue) {
        getVariantBuilder(pBlock).forAllStates(state -> {
            int number = state.getValue(pValue);
            String name = blockName(pBlock) + "_" + number;

            return ConfiguredModel.builder()
                    .modelFile(models().cubeAll(name, modLoc("block/" + name)))
                    .build();
        });
        itemModels().getBuilder(blockName(pBlock))
                .parent(models().getExistingFile(modLoc("block/" + blockName(pBlock) + "_0")));
    }

    private String blockName(Block block) {
        return key(block).getPath();
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }


    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

}

