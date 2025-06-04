package net.eagl.minetorio.datagen;

import net.eagl.minetorio.Minetorio;
import net.eagl.minetorio.item.MinetorioItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;


public class MinetorioItemModelProvider extends ItemModelProvider {

    public MinetorioItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Minetorio.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        simpleItem(MinetorioItems.SAPPHIRE);

        simplePatternItem(MinetorioItems.PATTERN_AIR);
        simplePatternItem(MinetorioItems.PATTERN_BATTERY);
        simplePatternItem(MinetorioItems.PATTERN_CLOUD);
        simplePatternItem(MinetorioItems.PATTERN_EARTH);
        simplePatternItem(MinetorioItems.PATTERN_FIRE);
        simplePatternItem(MinetorioItems.PATTERN_INFINITY);
        simplePatternItem(MinetorioItems.PATTERN_LIGHTNING);
        simplePatternItem(MinetorioItems.PATTERN_MINETORIO);
        simplePatternItem(MinetorioItems.PATTERN_RAIN);
        simplePatternItem(MinetorioItems.PATTERN_RESEARCH_BOOK);
        simplePatternItem(MinetorioItems.PATTERN_SNOW);
        simplePatternItem(MinetorioItems.PATTERN_SNOWFLAKE);
        simplePatternItem(MinetorioItems.PATTERN_SUN);
        simplePatternItem(MinetorioItems.PATTERN_VOID);
        simplePatternItem(MinetorioItems.PATTERN_WATER);

        simpleFlaskItem(MinetorioItems.FLASK_BLACK);
        simpleFlaskItem(MinetorioItems.FLASK_BLUE);
        simpleFlaskItem(MinetorioItems.FLASK_BROWN);
        simpleFlaskItem(MinetorioItems.FLASK_CYAN);
        simpleFlaskItem(MinetorioItems.FLASK_GREEN);
        simpleFlaskItem(MinetorioItems.FLASK_PINK);
        simpleFlaskItem(MinetorioItems.FLASK_PURPLE);
        simpleFlaskItem(MinetorioItems.FLASK_RED);
        simpleFlaskItem(MinetorioItems.FLASK_WHITE);
        simpleFlaskItem(MinetorioItems.FLASK_YELLOW);
    }

    private void simpleFlaskItem(RegistryObject<Item> item) {
        withExistingParent(itemPath(item),
                ResourceLocation.fromNamespaceAndPath("minecraft", "item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(Minetorio.MOD_ID, "item/flasks/" + itemPath(item).replaceFirst("flask_","")));
    }

    private void simplePatternItem(RegistryObject<Item> item) {
        withExistingParent(itemPath(item),
                ResourceLocation.fromNamespaceAndPath("minecraft", "item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(Minetorio.MOD_ID, "item/patterns/" + itemPath(item).replaceFirst("pattern_","")));
    }


    private void simpleItem(RegistryObject<Item> item) {
        withExistingParent(itemPath(item),
                ResourceLocation.fromNamespaceAndPath("minecraft", "item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(Minetorio.MOD_ID, "item/" + itemPath(item)));
    }

    private String itemPath(RegistryObject<?> item){
        return Objects.requireNonNull(item.getId()).getPath();
    }




}
