package net.eagl.minetorio.datagen;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.item.MinetorioItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Minetorio.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(MinetorioItems.SAPPHIRE);
        simpleItem(MinetorioItems.RAW_SAPPHIRE);
        simpleItem(MinetorioItems.METAL_DETECTOR);
        simpleItem(MinetorioItems.PINE_CONE);
        simpleItem(MinetorioItems.STRAWBERRY);

        simpleBlockItem(MinetorioBlocks.SAPPHIRE_DOOR);

        fenceItem(MinetorioBlocks.SAPPHIRE_FENCE, MinetorioBlocks.SAPPHIRE_BLOCK);
        buttonItem(MinetorioBlocks.SAPPHIRE_BUTTON, MinetorioBlocks.SAPPHIRE_BLOCK);
        wallItem(MinetorioBlocks.SAPPHIRE_WALL, MinetorioBlocks.SAPPHIRE_BLOCK);

        evenSimplerBlockItem(MinetorioBlocks.SAPPHIRE_STAIRS);
        evenSimplerBlockItem(MinetorioBlocks.SAPPHIRE_SLAB);
        evenSimplerBlockItem(MinetorioBlocks.SAPPHIRE_PRESSURE_PLATE);
        evenSimplerBlockItem(MinetorioBlocks.SAPPHIRE_FENCE_GATE);

        trapdoorItem(MinetorioBlocks.SAPPHIRE_TRAPDOOR);
    }



    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(itemPath(item),
                ResourceLocation.fromNamespaceAndPath("minecraft", "item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(Minetorio.MOD_ID,"item/" + itemPath(item)));
    }
    public void evenSimplerBlockItem(RegistryObject<Block> block) {
        this.withExistingParent(Minetorio.MOD_ID + ":" + blockPath(block),
                modLoc("block/" + blockPath(block)));
    }

    public void trapdoorItem(RegistryObject<Block> block) {
        this.withExistingParent(blockPath(block),
                modLoc("block/" + blockPath(block) + "_bottom"));
    }

    public void fenceItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(blockPath(block), mcLoc("block/fence_inventory"))
                .texture("texture",  ResourceLocation.fromNamespaceAndPath(Minetorio.MOD_ID, "block/" + blockPath(baseBlock)));
    }

    public void buttonItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
       this.withExistingParent(blockPath(block), mcLoc("block/button_inventory"))
                .texture("texture",  ResourceLocation.fromNamespaceAndPath(Minetorio.MOD_ID, "block/" + blockPath(baseBlock)));
    }

    public void wallItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(blockPath(block), mcLoc("block/wall_inventory"))
                .texture("wall",  ResourceLocation.fromNamespaceAndPath(Minetorio.MOD_ID, "block/" + blockPath(baseBlock)));
    }

    private String blockPath(RegistryObject<Block> block){
      return Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath();
    }

    private String itemPath(RegistryObject<?> item){
        return Objects.requireNonNull(item.getId()).getPath();
    }

    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> item) {
        return withExistingParent(itemPath(item),
                ResourceLocation.fromNamespaceAndPath("minecraft","item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(Minetorio.MOD_ID,"item/" + itemPath(item)));
    }
}
