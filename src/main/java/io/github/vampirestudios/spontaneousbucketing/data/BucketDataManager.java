package io.github.vampirestudios.spontaneousbucketing.data;

import com.swordglowsblue.artifice.api.Artifice;
import io.github.vampirestudios.spontaneousbucketing.impl.BucketMaterial;
import io.github.vampirestudios.spontaneousbucketing.impl.BucketRegistry;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;

public class BucketDataManager {

    public static int datapack_number;

    public static void init() {
        datapack_number = 0;
        datapack_number++;
        Artifice.registerData(new Identifier("spontaneousbucketing","artifice_datapack_" +datapack_number), serverResourcePackBuilder -> {
            for (BucketMaterial bucketMaterial : BucketRegistry.BUCKETS) {
                Identifier materialItem = Registry.ITEM.getId(bucketMaterial.getMaterial());
                Identifier bucket = Registry.ITEM.getId(bucketMaterial.getBucketFromType(new Identifier("empty")));
                serverResourcePackBuilder.addShapedRecipe(bucket, shapedRecipeBuilder -> {
                    shapedRecipeBuilder.pattern("M M"," M ");
                    shapedRecipeBuilder.ingredientItem('M', materialItem);
                    shapedRecipeBuilder.result(bucket, 1);
                });
            }
        });

        RegistryEntryAddedCallback.event(BucketRegistry.BUCKETS).register((i, identifier, material) -> {
            datapack_number++;
            Artifice.registerData(new Identifier("spontaneousbucketing","artifice_datapack_" +datapack_number), serverResourcePackBuilder -> {
                Identifier materialItem = Registry.ITEM.getId(material.getMaterial());
                Identifier bucket = Registry.ITEM.getId(material.getBucketFromType(new Identifier("empty")));
                serverResourcePackBuilder.addShapedRecipe(bucket, shapedRecipeBuilder -> {
                    shapedRecipeBuilder.pattern("M M"," M ");
                    shapedRecipeBuilder.ingredientItem('M', materialItem);
                    shapedRecipeBuilder.result(bucket, 1);
                });
            });
        });
    }
}
