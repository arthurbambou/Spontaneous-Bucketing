package io.github.vampirestudios.spontaneousbucketing.mixin;

import io.github.vampirestudios.spontaneousbucketing.impl.BucketMaterial;
import io.github.vampirestudios.spontaneousbucketing.impl.BucketRegistry;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CowEntity.class)
public class MixinCowEntity {

    private ItemStack interactMobItemStack;


    @ModifyVariable(method = "interactMob", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/player/PlayerEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;"), ordinal = 0, name = "itemStack")
    public ItemStack interactMob$bucketing$replaceItemStack(ItemStack stack) {
        if (stack.getItem() instanceof BucketItem || stack.getItem() instanceof MilkBucketItem) {
            this.interactMobItemStack = stack.copy();
            return new ItemStack(BucketRegistry.BUCKETS.get(new Identifier("iron")).getBucketFromType(BucketRegistry.getTypeFromBucket(stack.getItem())), stack.getCount());
        }
        return stack;
    }

    @ModifyArg(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setStackInHand(Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;)V"), index = 1)
    private ItemStack interactMob$bucketing$setStackInHand(ItemStack stack) {
        BucketMaterial material = BucketRegistry.getMaterialFromBucket(this.interactMobItemStack.getItem());
        return new ItemStack(material.getBucketFromType(BucketRegistry.getTypeFromBucket(stack.getItem())));
    }

    @ModifyArg(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;insertStack(Lnet/minecraft/item/ItemStack;)Z"), index = 0)
    private ItemStack interactMob$bucketing$insertStack(ItemStack stack) {
        BucketMaterial material = BucketRegistry.getMaterialFromBucket(this.interactMobItemStack.getItem());
        return new ItemStack(material.getBucketFromType(BucketRegistry.getTypeFromBucket(stack.getItem())));
    }

    @ModifyArg(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;dropItem(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/ItemEntity;"), index = 0)
    private ItemStack interactMob$bucketing$dropStack(ItemStack stack) {
        BucketMaterial material = BucketRegistry.getMaterialFromBucket(this.interactMobItemStack.getItem());
        return new ItemStack(material.getBucketFromType(BucketRegistry.getTypeFromBucket(stack.getItem())));
    }
}
