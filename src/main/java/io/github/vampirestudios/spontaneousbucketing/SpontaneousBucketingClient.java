package io.github.vampirestudios.spontaneousbucketing;

import io.github.vampirestudios.spontaneousbucketing.client.BucketColorManager;
import io.github.vampirestudios.spontaneousbucketing.client.BucketTextureManager;
import net.fabricmc.api.ClientModInitializer;

public class SpontaneousBucketingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BucketTextureManager.init();
        BucketColorManager.init();
    }
}
