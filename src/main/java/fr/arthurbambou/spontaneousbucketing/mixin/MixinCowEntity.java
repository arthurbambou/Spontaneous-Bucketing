package fr.arthurbambou.spontaneousbucketing.mixin;

import fr.arthurbambou.spontaneousbucketing.impl.BucketMaterial;
import fr.arthurbambou.spontaneousbucketing.impl.BucketRegistry;
import fr.arthurbambou.spontaneousbucketing.impl.SpecialBucketTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(CowEntity.class)
public abstract class MixinCowEntity extends AnimalEntity {

    protected MixinCowEntity(EntityType<? extends AnimalEntity> type, World world) {
        super(type, world);
    }

    /**
     * @author TODO: Find another way to do this.
     */
    @Overwrite
    public boolean interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.getItem() instanceof BucketItem && !player.abilities.creativeMode && !((CowEntity)(Object)this).isBaby()) {
            BucketMaterial bucketMaterial = BucketRegistry.getMaterialFromBucket(itemStack.getItem());
            player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
            itemStack.decrement(1);
            if (itemStack.isEmpty()) {
                player.setStackInHand(hand, new ItemStack(bucketMaterial.getBucketFromType(SpecialBucketTypes.MILK)));
            } else if (!player.inventory.insertStack(new ItemStack(bucketMaterial.getBucketFromType(SpecialBucketTypes.MILK)))) {
                player.dropItem(new ItemStack(bucketMaterial.getBucketFromType(SpecialBucketTypes.MILK)), false);
            }

            return true;
        } else {
            return super.interactMob(player, hand);
        }
    }
}
