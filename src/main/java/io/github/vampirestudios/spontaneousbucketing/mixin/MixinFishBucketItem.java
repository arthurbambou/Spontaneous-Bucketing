package io.github.vampirestudios.spontaneousbucketing.mixin;

import io.github.vampirestudios.spontaneousbucketing.impl.FishBucketItemAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.item.FishBucketItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FishBucketItem.class)
public class MixinFishBucketItem implements FishBucketItemAccessor {

    @Shadow @Final private EntityType<?> fishType;

    @Override
    public EntityType<?> getEntityType() {
        return this.fishType;
    }
}
