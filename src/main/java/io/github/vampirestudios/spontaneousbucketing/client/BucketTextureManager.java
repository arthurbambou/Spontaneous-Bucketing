package io.github.vampirestudios.spontaneousbucketing.client;

import com.swordglowsblue.artifice.api.Artifice;
import io.github.vampirestudios.spontaneousbucketing.client.styles.Style;
import io.github.vampirestudios.spontaneousbucketing.client.styles.Styles;
import io.github.vampirestudios.spontaneousbucketing.impl.BucketMaterial;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;

import static io.github.vampirestudios.spontaneousbucketing.impl.BucketRegistry.BUCKETS;

public class BucketTextureManager {

    public static int resource_pack_number;

    public static void init() {
        resource_pack_number = 0;
        resource_pack_number++;
        Artifice.registerAssets(new Identifier("spontaneousbucketing","artifice_resourcepack_" +resource_pack_number), clientResourcePackBuilder -> {
            for (BucketMaterial bucketMaterial : BUCKETS) {
                Style style = bucketMaterial.getStyle();
                String texturesLocation = "item/buckets/" + style.getName() + "/";
                Identifier bucketTextureLocation = new Identifier(texturesLocation + "empty");
                if (!style.hasCustomLiquidTextures()) texturesLocation = texturesLocation.replace(style.getName(), Styles.VANILLA.getName());
                for (Map.Entry<Identifier, Identifier> entry : bucketMaterial.getEntrySet()) {
                    if (entry.getKey().toString().equals(new Identifier("empty").toString())) {
                        clientResourcePackBuilder.addItemModel(entry.getValue(), modelBuilder -> {
                            modelBuilder.parent(new Identifier("item/generated"));
                            modelBuilder.texture("layer0", bucketTextureLocation);
                            modelBuilder.texture("layer1", bucketTextureLocation);
                        });
                    } else {
                        Identifier contentTexture = new Identifier(entry.getKey().getNamespace(), texturesLocation + entry.getKey().getPath());
                        clientResourcePackBuilder.addItemModel(entry.getValue(), modelBuilder -> {
                            modelBuilder.parent(new Identifier("item/generated"));
                            modelBuilder.texture("layer0", bucketTextureLocation);
                            modelBuilder.texture("layer1", contentTexture);
                        });
                    }
                }
            }
        });


        RegistryEntryAddedCallback.event(BUCKETS).register((i, identifier, bucketMaterial) -> {
            resource_pack_number++;
            Artifice.registerAssets(new Identifier("spontaneousbucketing","artifice_resourcepack_" +resource_pack_number), clientResourcePackBuilder -> {
                Style style = bucketMaterial.getStyle();
                String texturesLocation = "item/buckets/" + style.getName() + "/";
                Identifier bucketTextureLocation = new Identifier(texturesLocation + "empty");
                if (!style.hasCustomLiquidTextures()) texturesLocation = texturesLocation.replace(style.getName(), Styles.VANILLA.getName());
                for (Map.Entry<Identifier, Identifier> entry : bucketMaterial.getEntrySet()) {
                    if (entry.getKey().toString().equals(new Identifier("empty").toString())) {
                        clientResourcePackBuilder.addItemModel(entry.getValue(), modelBuilder -> {
                            modelBuilder.parent(new Identifier("item/generated"));
                            modelBuilder.texture("layer0", bucketTextureLocation);
                            modelBuilder.texture("layer1", bucketTextureLocation);
                        });
                    } else {
                        Identifier contentTexture = new Identifier(entry.getKey().getNamespace(), texturesLocation + entry.getKey().getPath());
                        clientResourcePackBuilder.addItemModel(entry.getValue(), modelBuilder -> {
                            modelBuilder.parent(new Identifier("item/generated"));
                            modelBuilder.texture("layer0", bucketTextureLocation);
                            modelBuilder.texture("layer1", contentTexture);
                        });
                    }
                }
            });
        });
    }


    public static void newBucketType(Identifier type) {
        resource_pack_number++;
        Artifice.registerAssets(new Identifier("spontaneousbucketing","artifice_resourcepack_" +resource_pack_number), clientResourcePackBuilder -> {
            for (BucketMaterial material : BUCKETS) {
                Style style = material.getStyle();
                String texturesLocation = "item/buckets/" + style.getName() + "/";
                Identifier bucketTextureLocation = new Identifier(texturesLocation + "empty");
                if (!style.hasCustomLiquidTextures()) texturesLocation = texturesLocation.replace(style.getName(), Styles.VANILLA.getName());
                Identifier item = Registry.ITEM.getId(material.getBucketFromType(type));
                Identifier contentTexture = new Identifier(type.getNamespace(), texturesLocation + type.getPath());
                clientResourcePackBuilder.addItemModel(item, modelBuilder -> {
                    modelBuilder.parent(new Identifier("item/generated"));
                    modelBuilder.texture("layer0", bucketTextureLocation);
                    modelBuilder.texture("layer1", contentTexture);
                });
            }
        });
    }

}
