package net.eagl.minetorio.datagen;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.block.custom.CornCropBlock;
import net.eagl.minetorio.block.custom.StrawberryCropBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;
import java.util.function.Function;

public class MinetorioBlockStateProvider extends BlockStateProvider {
    public MinetorioBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Minetorio.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(MinetorioBlocks.SAPPHIRE_BLOCK);
        blockWithItem(MinetorioBlocks.RAW_SAPPHIRE_BLOCK);

        blockWithItem(MinetorioBlocks.SAPPHIRE_ORE);
        blockWithItem(MinetorioBlocks.DEEPSLATE_SAPPHIRE_ORE);
        blockWithItem(MinetorioBlocks.END_STONE_SAPPHIRE_ORE);
        blockWithItem(MinetorioBlocks.NETHER_SAPPHIRE_ORE);

        blockWithItem(MinetorioBlocks.DUPLICATOR_BLOCK);
        blockWithItem(MinetorioBlocks.SOUND_BLOCK);

        stairsBlock(((StairBlock) MinetorioBlocks.SAPPHIRE_STAIRS.get()), blockTexture(MinetorioBlocks.SAPPHIRE_BLOCK.get()));
        slabBlock(((SlabBlock) MinetorioBlocks.SAPPHIRE_SLAB.get()), blockTexture(MinetorioBlocks.SAPPHIRE_BLOCK.get()), blockTexture(MinetorioBlocks.SAPPHIRE_BLOCK.get()));

        buttonBlock(((ButtonBlock) MinetorioBlocks.SAPPHIRE_BUTTON.get()), blockTexture(MinetorioBlocks.SAPPHIRE_BLOCK.get()));
        pressurePlateBlock(((PressurePlateBlock) MinetorioBlocks.SAPPHIRE_PRESSURE_PLATE.get()), blockTexture(MinetorioBlocks.SAPPHIRE_BLOCK.get()));

        fenceBlock(((FenceBlock) MinetorioBlocks.SAPPHIRE_FENCE.get()), blockTexture(MinetorioBlocks.SAPPHIRE_BLOCK.get()));
        fenceGateBlock(((FenceGateBlock) MinetorioBlocks.SAPPHIRE_FENCE_GATE.get()), blockTexture(MinetorioBlocks.SAPPHIRE_BLOCK.get()));
        wallBlock(((WallBlock) MinetorioBlocks.SAPPHIRE_WALL.get()), blockTexture(MinetorioBlocks.SAPPHIRE_BLOCK.get()));

        doorBlockWithRenderType(((DoorBlock) MinetorioBlocks.SAPPHIRE_DOOR.get()), modLoc("block/sapphire_door_bottom"), modLoc("block/sapphire_door_top"), "cutout");
        trapdoorBlockWithRenderType(((TrapDoorBlock) MinetorioBlocks.SAPPHIRE_TRAPDOOR.get()), modLoc("block/sapphire_trapdoor"), true, "cutout");

        makeStrawberryCrop((CropBlock) MinetorioBlocks.STRAWBERRY_CROP.get(), "strawberry_stage", "strawberry_stage");

        makeCornCrop(((CropBlock) MinetorioBlocks.CORN_CROP.get()), "corn_stage_", "corn_stage_");

        simpleBlockWithItem(MinetorioBlocks.CATMINT.get(), models().cross(blockTexture(MinetorioBlocks.CATMINT.get()).getPath(),
                blockTexture(MinetorioBlocks.CATMINT.get())).renderType("cutout"));
        simpleBlockWithItem(MinetorioBlocks.POTTED_CATMINT.get(), models().singleTexture("potted_catmint", ResourceLocation.fromNamespaceAndPath("minecraft","flower_pot_cross"), "plant",
                blockTexture(MinetorioBlocks.CATMINT.get())).renderType("cutout"));

        simpleBlockWithItem(MinetorioBlocks.GEM_POLISHING_STATION.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/gem_polishing_station")));

        logBlock(((RotatedPillarBlock) MinetorioBlocks.PINE_LOG.get()));
        axisBlock(((RotatedPillarBlock) MinetorioBlocks.PINE_WOOD.get()), blockTexture(MinetorioBlocks.PINE_LOG.get()), blockTexture(MinetorioBlocks.PINE_LOG.get()));

        axisBlock(((RotatedPillarBlock) MinetorioBlocks.STRIPPED_PINE_LOG.get()), blockTexture(MinetorioBlocks.STRIPPED_PINE_LOG.get()),
                Minetorio.resourceLocation("block/stripped_pine_log_top"));
        axisBlock(((RotatedPillarBlock) MinetorioBlocks.STRIPPED_PINE_WOOD.get()), blockTexture(MinetorioBlocks.STRIPPED_PINE_LOG.get()),
                blockTexture(MinetorioBlocks.STRIPPED_PINE_LOG.get()));

        blockItem(MinetorioBlocks.PINE_LOG);
        blockItem(MinetorioBlocks.PINE_WOOD);
        blockItem(MinetorioBlocks.STRIPPED_PINE_LOG);
        blockItem(MinetorioBlocks.STRIPPED_PINE_WOOD);

        blockWithItem(MinetorioBlocks.PINE_PLANKS);

        leavesBlock(MinetorioBlocks.PINE_LEAVES);

        signBlock(((StandingSignBlock) MinetorioBlocks.PINE_SIGN.get()), ((WallSignBlock) MinetorioBlocks.PINE_WALL_SIGN.get()),
                blockTexture(MinetorioBlocks.PINE_PLANKS.get()));

        hangingSignBlock(MinetorioBlocks.PINE_HANGING_SIGN.get(), MinetorioBlocks.PINE_WALL_HANGING_SIGN.get(), blockTexture(MinetorioBlocks.PINE_PLANKS.get()));

        saplingBlock(MinetorioBlocks.PINE_SAPLING);
    }

    private void saplingBlock(RegistryObject<Block> blockRegistryObject) {
        simpleBlock(blockRegistryObject.get(),
                models().cross(ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath(), blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    public void hangingSignBlock(Block signBlock, Block wallSignBlock, ResourceLocation texture) {
        ModelFile sign = models().sign(name(signBlock), texture);
        hangingSignBlock(signBlock, wallSignBlock, sign);
    }

    public void hangingSignBlock(Block signBlock, Block wallSignBlock, ModelFile sign) {
        simpleBlock(signBlock, sign);
        simpleBlock(wallSignBlock, sign);
    }

    private String name(Block block) {
        return key(block).getPath();
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    private void leavesBlock(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(),
                models().singleTexture(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get())).getPath(), ResourceLocation.fromNamespaceAndPath("minecraft","block/leaves"),
                        "all", blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    private void blockItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile(Minetorio.MOD_ID +
                ":block/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get())).getPath()));
    }

    public void makeStrawberryCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> strawberryStates(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] strawberryStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((StrawberryCropBlock) block).getAgeProperty()),
               Minetorio.resourceLocation("block/" + textureName + state.getValue(((StrawberryCropBlock) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }

    public void makeCornCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> cornStates(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] cornStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((CornCropBlock) block).getAgeProperty()),
                Minetorio.resourceLocation("block/" + textureName + state.getValue(((CornCropBlock) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
