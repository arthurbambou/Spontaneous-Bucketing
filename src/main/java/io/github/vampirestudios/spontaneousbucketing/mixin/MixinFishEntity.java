package io.github.vampirestudios.spontaneousbucketing.mixin;

import io.github.vampirestudios.spontaneousbucketing.impl.BucketMaterial;
import io.github.vampirestudios.spontaneousbucketing.impl.BucketRegistry;
import io.github.vampirestudios.spontaneousbucketing.impl.SpecialBucketTypes;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FishEntity.class)
public abstract class MixinFishEntity extends WaterCreatureEntity {

    protected MixinFishEntity(EntityType<? extends WaterCreatureEntity> type, World world) {
        super(type, world);
    }

    private ItemStack interactMobItemStack;


    @ModifyVariable(method = "interactMob", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/player/PlayerEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;"), ordinal = 0, name = "itemStack")
    public ItemStack interactMob$bucketing$replaceItemStack(ItemStack stack) {
        if (stack.getItem() instanceof BucketItem || stack.getItem() instanceof MilkBucketItem) {
            this.interactMobItemStack = stack;
            return new ItemStack(BucketRegistry.BUCKETS.get(new Identifier("iron")).getBucketFromType(BucketRegistry.getTypeFromBucket(stack.getItem())), stack.getCount());
        }
        return stack;
    }

    @ModifyVariable(method = "interactMob", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/passive/FishEntity;getFishBucketItem()Lnet/minecraft/item/ItemStack;"), name = "itemStack2")
    public ItemStack interactMob$bucketing$replaceItemStack2(ItemStack stack) {
        BucketMaterial bucketMaterial = BucketRegistry.BUCKETS.get(BucketRegistry.BUCKETS.getId(BucketRegistry.getMaterialFromBucket(this.interactMobItemStack.getItem())));
        this.interactMobItemStack.decrement(1);
        if (this.getType() == EntityType.COD) stack = new ItemStack(bucketMaterial.getBucketFromType(SpecialBucketTypes.COD));
        else if (this.getType() == EntityType.PUFFERFISH) stack = new ItemStack(bucketMaterial.getBucketFromType(SpecialBucketTypes.PUFFERFISH));
        else if (this.getType() == EntityType.TROPICAL_FISH) stack = new ItemStack(bucketMaterial.getBucketFromType(SpecialBucketTypes.TROPICAL_FISH));
        else if (this.getType() == EntityType.SALMON) stack = new ItemStack(bucketMaterial.getBucketFromType(SpecialBucketTypes.SALMON));
        return stack;
    }

}
