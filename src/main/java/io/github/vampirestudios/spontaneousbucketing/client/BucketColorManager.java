package io.github.vampirestudios.spontaneousbucketing.client;

import io.github.vampirestudios.spontaneousbucketing.impl.BucketMaterial;
import io.github.vampirestudios.spontaneousbucketing.impl.BucketRegistry;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.impl.client.rendering.ColorProviderRegistryImpl;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;

public class BucketColorManager {

    public static void init() {
        for (BucketMaterial material : BucketRegistry.BUCKETS) {
            int color = material.getColor();
            if (color == -1) continue;
            for (Map.Entry<Identifier, Identifier> entry : material.getEntrySet()) {
                ColorProviderRegistryImpl.ITEM.register((stack, tintIndex) -> {
                    if (tintIndex == 0 || entry.getKey().toString().equals(new Identifier("empty").toString())) return color;
                    else return -1;
                }, Registry.ITEM.get(entry.getValue()));
            }
        }

        RegistryEntryAddedCallback.event(BucketRegistry.BUCKETS).register((i, identifier, material) -> {
            int color = material.getColor();
            if (color != -1) {
                for (Map.Entry<Identifier, Identifier> entry : material.getEntrySet()) {
                    ColorProviderRegistryImpl.ITEM.register((stack, tintIndex) -> {
                        if (tintIndex == 0 || entry.getKey().toString().equals(new Identifier("empty").toString()))
                            return color;
                        else return -1;
                    }, Registry.ITEM.get(entry.getValue()));
                }
            }
        });
    }

    public static void newBucketType(Identifier type) {
        for (BucketMaterial material : BucketRegistry.BUCKETS) {
            int color = material.getColor();
            if (color != -1) {
                ColorProviderRegistryImpl.ITEM.register((stack, tintIndex) -> {
                    if (tintIndex == 0)
                        return color;
                    else return -1;
                }, material.getBucketFromType(type));
            }
        }
    }
}
