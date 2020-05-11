package io.github.vampirestudios.spontaneousbucketing.impl;

import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.FishBucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.MilkBucketItem;

public class SpecialBucketTypes {

    public static final BucketType MILK = new BucketType("milk") {
        @Override
        public Item createItem() {
            return new MilkBucketItem(new Item.Settings().group(ItemGroup.MISC).maxCount(1));
        }

        @Override
        public void dispenseBehavior(BucketMaterial bucketMaterial) {

        }
    };
    public static final BucketType PUFFERFISH = new BucketType("pufferfish") {
        @Override
        public Item createItem() {
            return new FishBucketItem(EntityType.PUFFERFISH, Fluids.WATER, new Item.Settings().group(ItemGroup.MISC).maxCount(1));
        }

        @Override
        public void dispenseBehavior(BucketMaterial bucketMaterial) {
            DispenserBlock.registerBehavior(bucketMaterial.getBucketFromType(this.getId()), CustomDispenserBehaviors.EMPTY);
        }
    };
    public static final BucketType SALMON = new BucketType("salmon") {
        @Override
        public Item createItem() {
            return new FishBucketItem(EntityType.SALMON, Fluids.WATER, new Item.Settings().group(ItemGroup.MISC).maxCount(1));
        }

        @Override
        public void dispenseBehavior(BucketMaterial bucketMaterial) {
            DispenserBlock.registerBehavior(bucketMaterial.getBucketFromType(this.getId()), CustomDispenserBehaviors.EMPTY);
        }
    };
    public static final BucketType COD = new BucketType("cod") {
        @Override
        public Item createItem() {
            return new FishBucketItem(EntityType.COD, Fluids.WATER, new Item.Settings().group(ItemGroup.MISC).maxCount(1));
        }

        @Override
        public void dispenseBehavior(BucketMaterial bucketMaterial) {
            DispenserBlock.registerBehavior(bucketMaterial.getBucketFromType(this.getId()), CustomDispenserBehaviors.EMPTY);
        }
    };
    public static final BucketType TROPICAL_FISH = new BucketType("tropical_fish") {
        @Override
        public Item createItem() {
            return new FishBucketItem(EntityType.TROPICAL_FISH, Fluids.WATER, new Item.Settings().group(ItemGroup.MISC).maxCount(1));
        }

        @Override
        public void dispenseBehavior(BucketMaterial bucketMaterial) {
            DispenserBlock.registerBehavior(bucketMaterial.getBucketFromType(this.getId()), CustomDispenserBehaviors.EMPTY);
        }
    };

}
