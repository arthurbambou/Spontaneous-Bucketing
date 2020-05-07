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
            if (material.getID().toString().equals(new Identifier("iron").toString())) continue;
            int color = material.getColor();
            if (color == -1) continue;
            for (Map.Entry<Identifier, Identifier> entry : material.getEntrySet()) {
                ColorProviderRegistryImpl.ITEM.register((stack, tintIndex) -> {
                    if (tintIndex == 0 || entry.getKey().toString().equals(new Identifier("empty").toString())) return color;
                    else return -1;
                }, Registry.ITEM.get(entry.getValue()));
            }
        }
    }
}
