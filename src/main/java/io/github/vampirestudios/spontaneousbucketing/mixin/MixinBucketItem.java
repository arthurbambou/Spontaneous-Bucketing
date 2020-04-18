package io.github.vampirestudios.spontaneousbucketing.mixin;

import io.github.vampirestudios.spontaneousbucketing.impl.BucketMaterial;
import io.github.vampirestudios.spontaneousbucketing.impl.BucketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public class MixinBucketItem {

	@ModifyArg(method = "use", at = @At(value = "INVOKE", target = "net/minecraft/item/BucketItem.getFilledStack(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/Item;)Lnet/minecraft/item/ItemStack;"), index = 2)
	private Item bucketing$getFilledBucket(ItemStack stack, PlayerEntity player, Item filledBucket) {
		BucketMaterial material = BucketRegistry.getMaterialFromBucket(stack.getItem());
		BucketMaterial wrongMaterial = BucketRegistry.getMaterialFromBucket(filledBucket);
		Identifier bucketType = wrongMaterial.getTypeFromBucket(filledBucket);
		return material.getBucketFromType(bucketType);
	}

	@Inject(at = @At("RETURN"), method = "getEmptiedStack", cancellable = true)
	private void bucketing$getEmptyBucket(ItemStack stack, PlayerEntity player, CallbackInfoReturnable<ItemStack> cir) {
		BucketMaterial bucketMaterial = BucketRegistry.getMaterialFromBucket(stack.getItem());
		cir.setReturnValue(!player.abilities.creativeMode ? new ItemStack(bucketMaterial.getBucketFromType(new Identifier("empty"))) : stack);
	}
}
