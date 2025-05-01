package net.eagl.minetorio.datagen;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.block.MinetorioBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
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
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
