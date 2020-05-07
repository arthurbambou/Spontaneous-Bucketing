package io.github.vampirestudios.spontaneousbucketing.mixin;

import io.github.vampirestudios.spontaneousbucketing.client.BucketColorManager;
import io.github.vampirestudios.spontaneousbucketing.client.BucketTextureManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.tutorial.TutorialManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TutorialManager.class)
public class MixinTutorialManager {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void beforeResourcePackLoad(MinecraftClient minecraftClient, CallbackInfo ci) {
        BucketTextureManager.init();
        BucketColorManager.init();
    }
}
