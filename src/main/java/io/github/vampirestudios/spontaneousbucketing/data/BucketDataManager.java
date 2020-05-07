package io.github.vampirestudios.spontaneousbucketing.data;

import com.swordglowsblue.artifice.api.Artifice;
import io.github.vampirestudios.spontaneousbucketing.impl.BucketMaterial;
import io.github.vampirestudios.spontaneousbucketing.impl.BucketRegistry;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BucketDataManager {

    public static void init() {
        Artifice.registerData(new Identifier("spontaneousbucketing","artifice_datapack"), serverResourcePackBuilder -> {
            for (BucketMaterial bucketMaterial : BucketRegistry.BUCKETS) {
                if (bucketMaterial.getID().toString().equals(new Identifier("iron").toString())) continue;
                Identifier materialItem = Registry.ITEM.getId(bucketMaterial.getMaterial());
                Identifier bucket = Registry.ITEM.getId(bucketMaterial.getBucketFromType(new Identifier("empty")));
                serverResourcePackBuilder.addShapedRecipe(bucket, shapedRecipeBuilder -> {
                    shapedRecipeBuilder.pattern("M M"," M ");
                    shapedRecipeBuilder.ingredientItem('M', materialItem);
                    shapedRecipeBuilder.result(bucket, 1);
                });
            }
        });
    }
}
