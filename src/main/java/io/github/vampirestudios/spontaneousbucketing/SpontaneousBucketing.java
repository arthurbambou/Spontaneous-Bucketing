package io.github.vampirestudios.spontaneousbucketing;

import io.github.vampirestudios.spontaneousbucketing.client.styles.Styles;
import io.github.vampirestudios.spontaneousbucketing.data.BucketDataManager;
import io.github.vampirestudios.spontaneousbucketing.impl.BucketRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpontaneousBucketing implements ModInitializer {

	public static final String MODID = "spontaneousbucketing";

	@Override
	public void onInitialize() {
		Styles.init();
		BucketRegistry.init();
		BucketDataManager.init();
	}
}
