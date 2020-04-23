package io.github.vampirestudios.spontaneousbucketing.mixin;

import io.github.vampirestudios.spontaneousbucketing.impl.BucketMaterial;
import io.github.vampirestudios.spontaneousbucketing.impl.BucketRegistry;
import net.minecraft.block.CauldronBlock;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CauldronBlock.class)
public class MixinCauldronBlock {

    private ItemStack onUseItem;

    @ModifyVariable(method = "onUse", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"), ordinal = 0, name = "item")
    private Item onUse$bucketing$replaceItem(Item item) {
        if (item instanceof BucketItem || item instanceof MilkBucketItem) {
            this.onUseItem = new ItemStack(Registry.ITEM.get(Registry.ITEM.getId(item))).copy();
            return BucketRegistry.BUCKETS.get(new Identifier("iron")).getBucketFromType(BucketRegistry.getTypeFromBucket(item));
        }
        return item;
    }

    @ModifyArg(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setStackInHand(Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;)V"), index = 1)
    private ItemStack onUse$bucketing$setStackInHand(ItemStack stack) {
        if (stack.getItem() == Items.BUCKET || stack.getItem() == Items.WATER_BUCKET) {
            BucketMaterial material = BucketRegistry.getMaterialFromBucket(this.onUseItem.getItem());
            return new ItemStack(material.getBucketFromType(BucketRegistry.getTypeFromBucket(stack.getItem())));
        }
        return stack;
    }

    @ModifyArg(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;insertStack(Lnet/minecraft/item/ItemStack;)Z"), index = 0)
    private ItemStack onUse$bucketing$insertStack(ItemStack stack) {
        if (stack.getItem() == Items.WATER_BUCKET) {
            BucketMaterial material = BucketRegistry.getMaterialFromBucket(this.onUseItem.getItem());
            return new ItemStack(material.getBucketFromType(BucketRegistry.getTypeFromBucket(stack.getItem())));
        }
        return stack;
    }

    @ModifyArg(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;dropItem(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/ItemEntity;"), index = 0)
    private ItemStack onUse$bucketing$dropStack(ItemStack stack) {
        if (stack.getItem() == Items.WATER_BUCKET) {
            BucketMaterial material = BucketRegistry.getMaterialFromBucket(this.onUseItem.getItem());
            return new ItemStack(material.getBucketFromType(BucketRegistry.getTypeFromBucket(stack.getItem())));
        }
        return stack;
    }
}
