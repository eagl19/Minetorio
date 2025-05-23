package net.eagl.minetorio.datagen;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.custom.GlowingBedrockBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.EnumProperty;
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

        blockWithStatesAndItem(MinetorioBlocks.GLOWING_BEDROCK.get(),GlowingBedrockBlock.STATE);

        blockWithCustomSides(MinetorioBlocks.PATTERNS_COLLECTOR.get(),
                "item/patterns/void",
                "item/patterns/infinity",
                "item/patterns/air",
                "item/patterns/fire",
                "item/patterns/earth",
                "item/patterns/water");


        blockWithItem(MinetorioBlocks.PORTAL);
    }

    private void blockWithCustomSides(Block block, String pDown,String pUp,String pNorth,String pSouth,String pWest,String pEst) {
        String name = blockName(block);
        models().withExistingParent(name, mcLoc("block/cube"))
                .texture("down", modLoc(pDown))
                .texture("up", modLoc(pUp))
                .texture("north", modLoc(pNorth))
                .texture("south", modLoc(pSouth))
                .texture("west", modLoc(pWest))
                .texture("east", modLoc(pEst));

        simpleBlock(block, models().getExistingFile(modLoc("block/" + name)));

        itemModels().getBuilder(name)
                .parent(models().getExistingFile(modLoc("block/" + name)));
    }

    private void blockWithStatesAndItem(Block pBlock, EnumProperty<?> pValue) {
        getVariantBuilder(pBlock).forAllStates(state -> {
            String blockState = state.getValue(pValue).getSerializedName();
            String name = blockName(pBlock) + "_" + blockState;

            return ConfiguredModel.builder()
                    .modelFile(models().cubeAll(name, modLoc("block/" + name)))
                    .build();
        });
        itemModels().getBuilder(blockName(pBlock))
                .parent(models().getExistingFile(modLoc("block/" + blockName(pBlock) + "_bedrock")));
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

