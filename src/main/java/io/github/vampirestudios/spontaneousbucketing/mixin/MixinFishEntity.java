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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FishEntity.class)
public abstract class MixinFishEntity extends WaterCreatureEntity {

    @Shadow protected abstract void copyDataToStack(ItemStack stack);

    protected MixinFishEntity(EntityType<? extends WaterCreatureEntity> type, World world) {
        super(type, world);
    }

    /**
     * @author CatCore
     *
     * TODO: Find another way to implement this.
     */
    @Overwrite
    public boolean interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.getItem() instanceof BucketItem) {
            BucketMaterial bucketMaterial = BucketRegistry.getMaterialFromBucket(itemStack.getItem());
            if (bucketMaterial.getTypeFromBucket(itemStack.getItem()).toString().equals(new Identifier("water").toString()) && this.isAlive()) {
                this.playSound(SoundEvents.ITEM_BUCKET_FILL_FISH, 1.0F, 1.0F);
                itemStack.decrement(1);
                ItemStack itemStack2;
                if (this.getType() == EntityType.COD) itemStack2 = new ItemStack(bucketMaterial.getBucketFromType(SpecialBucketTypes.COD));
                else if (this.getType() == EntityType.PUFFERFISH) itemStack2 = new ItemStack(bucketMaterial.getBucketFromType(SpecialBucketTypes.PUFFERFISH));
                else if (this.getType() == EntityType.TROPICAL_FISH) itemStack2 = new ItemStack(bucketMaterial.getBucketFromType(SpecialBucketTypes.TROPICAL_FISH));
                else if (this.getType() == EntityType.SALMON) itemStack2 = new ItemStack(bucketMaterial.getBucketFromType(SpecialBucketTypes.SALMON));
                else itemStack2 = new ItemStack(Items.BUCKET);
                this.copyDataToStack(itemStack2);
                if (!this.world.isClient) {
                    Criteria.FILLED_BUCKET.trigger((ServerPlayerEntity) player, itemStack2);
                }

                if (itemStack.isEmpty()) {
                    player.setStackInHand(hand, itemStack2);
                } else if (!player.inventory.insertStack(itemStack2)) {
                    player.dropItem(itemStack2, false);
                }

                this.remove();
                return true;
            } else {
                return super.interactMob(player, hand);
            }
        } else {
            return super.interactMob(player, hand);
        }
    }

}
