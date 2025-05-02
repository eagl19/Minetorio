package net.eagl.minetorio.datagen;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.block.MinetorioBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

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

        stairsBlock(((StairBlock) MinetorioBlocks.SAPPHIRE_STAIRS.get()), blockTexture(MinetorioBlocks.SAPPHIRE_BLOCK.get()));
        slabBlock(((SlabBlock) MinetorioBlocks.SAPPHIRE_SLAB.get()), blockTexture(MinetorioBlocks.SAPPHIRE_BLOCK.get()), blockTexture(MinetorioBlocks.SAPPHIRE_BLOCK.get()));

        buttonBlock(((ButtonBlock) MinetorioBlocks.SAPPHIRE_BUTTON.get()), blockTexture(MinetorioBlocks.SAPPHIRE_BLOCK.get()));
        pressurePlateBlock(((PressurePlateBlock) MinetorioBlocks.SAPPHIRE_PRESSURE_PLATE.get()), blockTexture(MinetorioBlocks.SAPPHIRE_BLOCK.get()));

        fenceBlock(((FenceBlock) MinetorioBlocks.SAPPHIRE_FENCE.get()), blockTexture(MinetorioBlocks.SAPPHIRE_BLOCK.get()));
        fenceGateBlock(((FenceGateBlock) MinetorioBlocks.SAPPHIRE_FENCE_GATE.get()), blockTexture(MinetorioBlocks.SAPPHIRE_BLOCK.get()));
        wallBlock(((WallBlock) MinetorioBlocks.SAPPHIRE_WALL.get()), blockTexture(MinetorioBlocks.SAPPHIRE_BLOCK.get()));

        doorBlockWithRenderType(((DoorBlock) MinetorioBlocks.SAPPHIRE_DOOR.get()), modLoc("block/sapphire_door_bottom"), modLoc("block/sapphire_door_top"), "cutout");
        trapdoorBlockWithRenderType(((TrapDoorBlock) MinetorioBlocks.SAPPHIRE_TRAPDOOR.get()), modLoc("block/sapphire_trapdoor"), true, "cutout");
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
