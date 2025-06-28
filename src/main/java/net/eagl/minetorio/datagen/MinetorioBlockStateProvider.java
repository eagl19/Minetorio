package net.eagl.minetorio.datagen;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.custom.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
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

        blockWithMatrixStatesAndItem(MinetorioBlocks.GLOWING_BEDROCK.get(),GlowingBedrockBlock.STATE, false);

        blockWithMatrixStatesAndItem(MinetorioBlocks.BARRIER.get(), Barrier.STATE, true);

        blockWithCustomSides(MinetorioBlocks.PATTERNS_COLLECTOR.get(),
                "item/patterns/void",
                "item/patterns/infinity",
                "item/patterns/air",
                "item/patterns/fire",
                "item/patterns/earth",
                "item/patterns/water");


        blockWithCustomSides(MinetorioBlocks.RESEARCHER.get(),
                "item/patterns/void",
                "item/patterns/sun",
                "item/patterns/research_book",
                "item/patterns/minetorio",
                "item/patterns/water",
                "item/patterns/fire");

        blockWithStatesAndItem(MinetorioBlocks.WATER_GENERATOR.get(), WaterGenerator.STATE);
        blockWithStatesAndItem(MinetorioBlocks.LAVA_GENERATOR.get(), LavaGenerator.STATE);
        blockWithStatesCustomSidesAndItem(MinetorioBlocks.ENERGY_GENERATOR.get(), EnergyGenerator.STATE,
                "item/patterns/void",
                "block/solar_panel",
                "item/patterns/sun",
                "item/patterns/sun",
                "item/patterns/sun",
                "item/patterns/sun");

        blockWithItem(MinetorioBlocks.PORTAL);
    }

    private void blockWithStatesCustomSidesAndItem(Block block, EnumProperty<?> property, String pDown,String pUp,String pNorth,String pSouth,String pWest,String pEst) {

        getVariantBuilder(block).forAllStates(state -> {
            String variant = state.getValue(property).getSerializedName();
            String name = blockName(block) + "_" + variant;
            ModelFile modelFile;

            if (variant.equals("stabilized")) {
                modelFile = models()
                        .withExistingParent(name, mcLoc("block/cube"))
                        .texture("down", modLoc(pDown))
                        .texture("up", modLoc(pUp))
                        .texture("north", modLoc(pNorth))
                        .texture("south", modLoc(pSouth))
                        .texture("west", modLoc(pWest))
                        .texture("east", modLoc(pEst));
            } else {
                modelFile = models().cubeAll(name, modLoc("block/" + name));
            }

            return ConfiguredModel.builder()
                    .modelFile(modelFile)
                    .build();
        });

        String defaultState = block.defaultBlockState().getValue(property).getSerializedName();
        itemModels().getBuilder(blockName(block))
                .parent(models().getExistingFile(modLoc("block/" + blockName(block) + "_" + defaultState)));

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

    private void blockWithMatrixStatesAndItem(Block block, EnumProperty<?> property, boolean invisible) {
        getVariantBuilder(block).forAllStates(state -> {
            String variant = state.getValue(property).getSerializedName();
            String name = blockName(block) + "_" + variant;

            ModelFile modelFile;
            if (variant.equals("unstable")) {
                modelFile = animatedState("matrix");
            } else {
                if(invisible) {
                    modelFile = models().withExistingParent(name, mcLoc("block/block"));
                }else{
                    modelFile = models().cubeAll(name, modLoc("block/" + name));
                }
            }

            return ConfiguredModel.builder()
                    .modelFile(modelFile)
                    .build();
        });

        if(invisible) {
            itemModels().getBuilder(blockName(block))
                    .parent(models().getExistingFile(mcLoc("item/generated")))
                    .texture("layer0", modLoc("item/" + blockName(block)));
        }else {
            String defaultState = block.defaultBlockState().getValue(property).getSerializedName();
            itemModels().getBuilder(blockName(block))
                    .parent(models().getExistingFile(modLoc("block/" + blockName(block) + "_" + defaultState)));
        }

    }

    private void blockWithStatesAndItem(Block block, EnumProperty<?> property) {
        getVariantBuilder(block).forAllStates(state -> {
            String variant = state.getValue(property).getSerializedName();
            String name = blockName(block) + "_" + variant;

            ModelFile modelFile;
            if (variant.equals("stabilized")) {
                modelFile = animatedState(name);
            } else {
                modelFile = models().cubeAll(name, modLoc("block/" + name));
            }

            return ConfiguredModel.builder()
                    .modelFile(modelFile)
                    .build();
        });

        String defaultState = block.defaultBlockState().getValue(property).getSerializedName();
        itemModels().getBuilder(blockName(block))
                .parent(models().getExistingFile(modLoc("block/" + blockName(block) + "_" + defaultState)));

    }

    private ModelFile animatedState(String name){
        return models()
                .withExistingParent(name, mcLoc("block/block"))
                .texture("texture", modLoc("block/" + name))
                .element()
                .from(0, 0, 0)
                .to(16, 16, 16)
                .face(Direction.DOWN).texture("#texture").cullface(Direction.DOWN).end()
                .face(Direction.UP).texture("#texture").cullface(Direction.UP).end()
                .face(Direction.NORTH).texture("#texture").cullface(Direction.NORTH).end()
                .face(Direction.SOUTH).texture("#texture").cullface(Direction.SOUTH).end()
                .face(Direction.WEST).texture("#texture").cullface(Direction.WEST).end()
                .face(Direction.EAST).texture("#texture").cullface(Direction.EAST).end()
                .end();
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

