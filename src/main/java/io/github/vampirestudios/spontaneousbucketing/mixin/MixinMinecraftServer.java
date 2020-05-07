package io.github.vampirestudios.spontaneousbucketing.mixin;

import io.github.vampirestudios.spontaneousbucketing.data.BucketDataManager;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    @Inject(
            method = {"<init>"},
            at = {@At("RETURN")}
    )
    private void registerBucketDataPack(CallbackInfo ci) {
        BucketDataManager.init();
    }
}
