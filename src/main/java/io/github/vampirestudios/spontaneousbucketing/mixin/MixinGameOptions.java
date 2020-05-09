package io.github.vampirestudios.spontaneousbucketing.mixin;

import io.github.vampirestudios.spontaneousbucketing.client.BucketColorManager;
import io.github.vampirestudios.spontaneousbucketing.client.BucketTextureManager;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.resource.ClientResourcePackProfile;
import net.minecraft.resource.ResourcePackManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
public class MixinGameOptions {

    @Inject(method = "addResourcePackProfilesToManager", at = @At("HEAD"))
    private void registerResourcePack(ResourcePackManager<ClientResourcePackProfile> manager, CallbackInfo ci) {
        BucketTextureManager.init();
        BucketColorManager.init();
    }
}
