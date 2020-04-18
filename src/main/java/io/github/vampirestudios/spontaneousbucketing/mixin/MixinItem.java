package io.github.vampirestudios.spontaneousbucketing.mixin;

import io.github.vampirestudios.spontaneousbucketing.impl.BucketMaterial;
import io.github.vampirestudios.spontaneousbucketing.impl.BucketRegistry;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class MixinItem {

    @Inject(method = "getName(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/text/Text;", at = @At("RETURN"), cancellable = true)
    private void bucketing$getBucketName(ItemStack stack, CallbackInfoReturnable<Text> cir) {
        if (stack.getItem() instanceof BucketItem || stack.getItem() instanceof MilkBucketItem) {
            BucketMaterial material = BucketRegistry.getMaterialFromBucket(stack.getItem());
            Text materialText = new TranslatableText("item." + material.getID().getNamespace() + ".bucket." + material.getID().getPath());
            Identifier bucketType = material.getTypeFromBucket(stack.getItem());
            if (bucketType.toString().equals(new Identifier("empty").toString())) {
                cir.setReturnValue(new TranslatableText("item.spontaneous_bucketing.bucket.empty", materialText));
            } else if (Registry.FLUID.getId(Registry.FLUID.get(bucketType)).toString().equals(bucketType.toString())) {
                cir.setReturnValue(new TranslatableText("item.spontaneous_bucketing.bucket.full",
                        materialText, new TranslatableText("block." + bucketType.toString().replace(":","."))));
            } else {
                cir.setReturnValue(new TranslatableText("item.spontaneous_bucketing.bucket.full",
                        materialText, new TranslatableText("bucket_type." + bucketType.toString().replace(":", "."))));
            }
        } else {
            cir.setReturnValue(new TranslatableText(((Item)(Object)this).getTranslationKey(stack), new Object[0]));
        }
    }
}
