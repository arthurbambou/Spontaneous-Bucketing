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

    public static void init() {
        Artifice.registerAssets(new Identifier("spontaneousbucketing","artifice_resourcepack"), clientResourcePackBuilder -> {
            for (BucketMaterial bucketMaterial : BUCKETS) {
                if (bucketMaterial.getID().toString().equals(new Identifier("iron").toString())) continue;
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
    }

}
