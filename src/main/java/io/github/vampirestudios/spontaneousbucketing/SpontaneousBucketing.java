package io.github.vampirestudios.spontaneousbucketing;

import io.github.vampirestudios.spontaneousbucketing.client.styles.Styles;
import io.github.vampirestudios.spontaneousbucketing.data.BucketDataManager;
import io.github.vampirestudios.spontaneousbucketing.impl.BucketRegistry;
import net.fabricmc.api.ModInitializer;

public class SpontaneousBucketing implements ModInitializer {
	@Override
	public void onInitialize() {
		Styles.init();
		BucketRegistry.init();
		BucketDataManager.init();
	}
}
