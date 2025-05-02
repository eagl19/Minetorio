package net.eagl.minetorio.datagen;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.block.MinetorioBlocks;
import net.eagl.minetorio.item.MinetorioItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;
import java.util.Objects;


public class MinetorioItemModelProvider extends ItemModelProvider {
    private static final LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();
    static {
        trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F);
        trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);
    }

    public MinetorioItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
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

        handheldItem(MinetorioItems.SAPPHIRE_SWORD);
        handheldItem(MinetorioItems.SAPPHIRE_PICKAXE);
        handheldItem(MinetorioItems.SAPPHIRE_AXE);
        handheldItem(MinetorioItems.SAPPHIRE_SHOVEL);
        handheldItem(MinetorioItems.SAPPHIRE_HOE);

        trimmedArmorItem(MinetorioItems.SAPPHIRE_HELMET);
        trimmedArmorItem(MinetorioItems.SAPPHIRE_CHESTPLATE);
        trimmedArmorItem(MinetorioItems.SAPPHIRE_LEGGINGS);
        trimmedArmorItem(MinetorioItems.SAPPHIRE_BOOTS);
    }

    private void trimmedArmorItem(RegistryObject<Item> itemRegistryObject) {
        final String MOD_ID = Minetorio.MOD_ID;

        if(itemRegistryObject.get() instanceof ArmorItem armorItem) {
            trimMaterials.forEach((trimMaterial, value) -> {

                float trimValue = value;

                String armorType = switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                String armorItemPath = "item/" + armorItem;
                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";
                ResourceLocation armorItemResLoc = ResourceLocation.fromNamespaceAndPath(MOD_ID, armorItemPath);
                ResourceLocation trimResLoc = ResourceLocation.fromNamespaceAndPath("minecraft", trimPath); // minecraft namespace
                ResourceLocation trimNameResLoc = ResourceLocation.fromNamespaceAndPath(MOD_ID, currentTrimName);

                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemResLoc)
                        .texture("layer1", trimResLoc);

                this.withExistingParent(Objects.requireNonNull(itemRegistryObject.getId()).getPath(),
                                mcLoc("item/generated"))
                        .override()
                        .model(new ModelFile.UncheckedModelFile(trimNameResLoc))
                        .predicate(mcLoc("trim_type"), trimValue).end()
                        .texture("layer0",
                                ResourceLocation.fromNamespaceAndPath(MOD_ID,
                                        "item/" + itemRegistryObject.getId().getPath()));
            });
        }
    }


    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(itemPath(item),
                ResourceLocation.fromNamespaceAndPath("minecraft","item/handheld")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(Minetorio.MOD_ID,"item/" + itemPath(item)));
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
