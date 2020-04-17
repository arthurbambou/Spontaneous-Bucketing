package fr.arthurbambou.spontaneousbucketing;

import fr.arthurbambou.spontaneousbucketing.impl.BucketRegistry;
import net.fabricmc.api.ModInitializer;

public class SpontaneousBucketing implements ModInitializer {
	@Override
	public void onInitialize() {
		BucketRegistry.init();
	}
}
