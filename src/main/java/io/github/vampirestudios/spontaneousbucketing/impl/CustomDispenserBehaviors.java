package io.github.vampirestudios.spontaneousbucketing.impl;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class CustomDispenserBehaviors {

    public static final DispenserBehavior EMPTY = new ItemDispenserBehavior() {
        private final ItemDispenserBehavior field_13367 = new ItemDispenserBehavior();

        public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            BucketItem bucketItem = (BucketItem)stack.getItem();
            BucketMaterial bucketMaterial = BucketRegistry.getMaterialFromBucket(bucketItem);
            BlockPos blockPos = pointer.getBlockPos().offset((Direction)pointer.getBlockState().get(DispenserBlock.FACING));
            World world = pointer.getWorld();
            if (bucketItem.placeFluid((PlayerEntity)null, world, blockPos, (BlockHitResult)null)) {
                bucketItem.onEmptied(world, stack, blockPos);
                return new ItemStack(bucketMaterial.getBucketFromType(new Identifier("empty")));
            } else {
                return this.field_13367.dispense(pointer, stack);
            }
        }
    };

    public static final DispenserBehavior FILL = new ItemDispenserBehavior() {
        private final ItemDispenserBehavior field_13368 = new ItemDispenserBehavior();

        public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            BucketMaterial bucketMaterial = BucketRegistry.getMaterialFromBucket(stack.getItem());
            IWorld iWorld = pointer.getWorld();
            BlockPos blockPos = pointer.getBlockPos().offset((Direction)pointer.getBlockState().get(DispenserBlock.FACING));
            BlockState blockState = iWorld.getBlockState(blockPos);
            Block block = blockState.getBlock();
            if (block instanceof FluidDrainable) {
                Fluid fluid = ((FluidDrainable)block).tryDrainFluid(iWorld, blockPos, blockState);
                if (!(fluid instanceof FlowableFluid)) {
                    return super.dispenseSilently(pointer, stack);
                } else {
                    Item item2 = bucketMaterial.getBucketFromType(BucketRegistry.BUCKETS.get(new Identifier("iron")).getITypeFromBucket(fluid.getBucketItem()));
                    stack.decrement(1);
                    if (stack.isEmpty()) {
                        return new ItemStack(item2);
                    } else {
                        if (((DispenserBlockEntity)pointer.getBlockEntity()).addToFirstFreeSlot(new ItemStack(item2)) < 0) {
                            this.field_13368.dispense(pointer, new ItemStack(item2));
                        }

                        return stack;
                    }
                }
            } else {
                return super.dispenseSilently(pointer, stack);
            }
        }
    };
}
