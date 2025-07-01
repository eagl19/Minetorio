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

        blockWithMatrixStatesAndItem(MinetorioBlocks.GLOWING_BEDROCK.get(), GlowingBedrockBlock.STATE, false);

        blockWithMatrixStatesAndItem(MinetorioBlocks.BARRIER.get(), Barrier.STATE, true);

        blockWithMatrixStatesAndCustomSidesAndItem(MinetorioBlocks.PATTERNS_COLLECTOR.get(), PatternsCollector.STATE, true,
                "item/patterns/void",
                "item/patterns/infinity",
                "item/patterns/air",
                "item/patterns/water",
                "item/patterns/fire",
                "item/patterns/earth");


        blockWithCustomSidesAndItem(MinetorioBlocks.RESEARCHER.get(),
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

        portalBlockWithAxisModels(MinetorioBlocks.PORTAL.get());
    }

    private void portalBlockWithAxisModels(Block block) {
        String name = blockName(block);
        ModelFile modelNS = createPortalModel(name + "_ns", Direction.NORTH, Direction.SOUTH, true);

        ModelFile modelEW =createPortalModel(name + "_ew", Direction.EAST, Direction.WEST, false);

        getVariantBuilder(block)
                .partialState().with(PortalBlock.AXIS, Direction.Axis.X).modelForState().modelFile(modelNS).addModel()
                .partialState().with(PortalBlock.AXIS, Direction.Axis.Z).modelForState().modelFile(modelEW).addModel();

        itemModels().getBuilder(name).parent(modelNS);
    }

    private ModelFile createPortalModel(String name, Direction dir1, Direction dir2, boolean isNS) {
        return models().withExistingParent(name, mcLoc("block/block"))
                .texture("portal", modLoc("block/portal"))
                .texture("particle", modLoc("block/portal"))
                .element()
                .from(isNS ? 0 : 6, 0, isNS ? 6 : 0)
                .to(isNS ? 16 : 10, 16, isNS ? 10 : 16)
                .face(dir1).uvs(0, 0, 16, 16).texture("#portal").end()
                .face(dir2).uvs(0, 0, 16, 16).texture("#portal").end()
                .end();
    }

    private void blockWithCustomSidesAndItem(Block block, String pDown, String pUp, String pNorth, String pSouth, String pWest, String pEst) {
        String name = blockName(block);
        ModelFile model = cubeModelWithCustomSides(name, pDown, pUp, pNorth, pSouth, pWest, pEst);
        simpleBlock(block, model);
        customBlockItem(name, model);
    }

    private void blockWithStatesCustomSidesAndItem(Block block, EnumProperty<?> property, String pDown,String pUp,String pNorth,String pSouth,String pWest,String pEst) {

        getVariantBuilder(block).forAllStates(state -> {
            String variant = state.getValue(property).getSerializedName();
            String name = blockName(block) + "_" + variant;
            ModelFile model = variant.equals("stabilized")
                    ? cubeModelWithCustomSides(name, pDown, pUp, pNorth, pSouth, pWest, pEst)
                    : models().cubeAll(name, modLoc("block/" + name));
            return ConfiguredModel.builder().modelFile(model).build();
        });

        customBlockDefaultStateItem(block, property);
    }

    private void blockWithMatrixStatesAndCustomSidesAndItem(Block block, EnumProperty<?> property, boolean invisible, String pDown,String pUp,String pNorth,String pSouth,String pWest,String pEst) {
        blockWithMatrixStates(block, property, invisible);
        customBlockItem(blockName(block), cubeModelWithCustomSides(blockName(block), pDown, pUp, pNorth, pSouth, pWest, pEst));
    }

    private void customBlockItem(String name, ModelFile model){
        itemModels().getBuilder(name).parent(model);
    }

    private void customBlockDefaultStateItem(Block block, EnumProperty<?> property){
        itemModels().getBuilder(blockName(block)).parent(models().getExistingFile(modLoc("block/" + blockName(block) + "_" +
                block.defaultBlockState().getValue(property).getSerializedName())));
    }

    private void customItem(String name, ModelFile model){
        itemModels().getBuilder(name).parent(model).texture("layer0", modLoc("item/" + name));
    }

    private void blockWithMatrixStates(Block block, EnumProperty<?> property, boolean invisible) {
        getVariantBuilder(block).forAllStates(state -> {
            String variant = state.getValue(property).getSerializedName();
            String name = blockName(block) + "_" + variant;
            ModelFile model = variant.equals("unstable")
                    ? animatedState("matrix")
                    : invisible
                        ? models().withExistingParent(name, mcLoc("block/block"))
                        : models().cubeAll(name, modLoc("block/" + name));

            return ConfiguredModel.builder().modelFile(model).build();
        });
    }

    private void blockWithMatrixStatesAndItem(Block block, EnumProperty<?> property, boolean invisible) {
        blockWithMatrixStates(block, property, invisible);
        if (invisible) {
            customItem(blockName(block), models().getExistingFile(mcLoc("item/generated")));
        } else {
            customBlockDefaultStateItem(block, property);
        }
    }

    private void blockWithStatesAndItem(Block block, EnumProperty<?> property) {
        getVariantBuilder(block).forAllStates(state -> {
            String variant = state.getValue(property).getSerializedName();
            String name = blockName(block) + "_" + variant;
            ModelFile model = variant.equals("stabilized")
                    ? animatedState(name)
                    : models().cubeAll(name, modLoc("block/" + name));
            return ConfiguredModel.builder().modelFile(model).build();
        });

        customBlockDefaultStateItem(block, property);
    }

    private ModelFile cubeModelWithCustomSides(String name, String down, String up, String north, String south, String west, String east) {
        return models().withExistingParent(name, mcLoc("block/cube"))
                .texture("down", modLoc(down))
                .texture("up", modLoc(up))
                .texture("north", modLoc(north))
                .texture("south", modLoc(south))
                .texture("west", modLoc(west))
                .texture("east", modLoc(east));
    }

    private ModelFile animatedState(String name){
        return models().withExistingParent(name, mcLoc("block/block"))
                .texture("texture", modLoc("block/" + name))
                .element()
                .from(0, 0, 0).to(16, 16, 16)
                .allFaces((dir, face) -> face.texture("#texture").cullface(dir))
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

