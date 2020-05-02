package io.github.vampirestudios.spontaneousbucketing.impl;

import io.github.vampirestudios.spontaneousbucketing.client.BucketColorManager;
import io.github.vampirestudios.spontaneousbucketing.client.BucketTextureManager;
import io.github.vampirestudios.spontaneousbucketing.data.BucketDataManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BucketRegistry {

    public static final DefaultedRegistry<BucketMaterial> BUCKETS = createRegistry("spontaneousbucketing:buckets", "null", () -> null);

    private static final List<Identifier> BUCKET_TYPES = new ArrayList<>();

    public static void init() {
        BucketMaterial iron = new BucketMaterial(new Identifier("iron"), Items.IRON_INGOT);
        BUCKET_TYPES.add(SpecialBucketTypes.MILK);
        BUCKET_TYPES.add(SpecialBucketTypes.PUFFERFISH);
        BUCKET_TYPES.add(SpecialBucketTypes.SALMON);
        BUCKET_TYPES.add(SpecialBucketTypes.COD);
        BUCKET_TYPES.add(SpecialBucketTypes.TROPICAL_FISH);
        for (Fluid fluid : Registry.FLUID) {
            if (fluid.isStill(null)) {
                BUCKET_TYPES.add(Registry.FLUID.getId(fluid));
                iron.setBucketForAType(Registry.FLUID.getId(fluid), fluid.getBucketItem());
            } else if (fluid == Fluids.EMPTY) {
                BUCKET_TYPES.add(Registry.FLUID.getId(fluid));
                iron.setBucketForAType(Registry.FLUID.getId(fluid), Items.BUCKET);
            }
        }
        iron.setBucketForAType(SpecialBucketTypes.MILK, Items.MILK_BUCKET)
                .setBucketForAType(SpecialBucketTypes.PUFFERFISH, Items.PUFFERFISH_BUCKET)
                .setBucketForAType(SpecialBucketTypes.SALMON, Items.SALMON_BUCKET)
                .setBucketForAType(SpecialBucketTypes.COD, Items.COD_BUCKET)
                .setBucketForAType(SpecialBucketTypes.TROPICAL_FISH, Items.TROPICAL_FISH_BUCKET);
        Registry.register(BUCKETS, iron.getID(), iron);

        RegistryEntryAddedCallback.event(BUCKETS).register((i, identifier, bucketMaterial) -> {
            for (Identifier bucketType : BUCKET_TYPES) {
                if (bucketType == SpecialBucketTypes.MILK) {
                    bucketMaterial.addBucketType(bucketType, new MilkBucketItem(new Item.Settings().maxCount(1).group(ItemGroup.MISC)));
                } else if (bucketType == SpecialBucketTypes.COD) {
                    bucketMaterial.addBucketType(bucketType, new FishBucketItem(EntityType.COD, Fluids.WATER, new Item.Settings().maxCount(1).group(ItemGroup.MISC)));
                    DispenserBlock.registerBehavior(bucketMaterial.getBucketFromType(bucketType), CustomDispenserBehaviors.EMPTY);
                } else if (bucketType == SpecialBucketTypes.SALMON) {
                    bucketMaterial.addBucketType(bucketType, new FishBucketItem(EntityType.SALMON, Fluids.WATER, new Item.Settings().maxCount(1).group(ItemGroup.MISC)));
                    DispenserBlock.registerBehavior(bucketMaterial.getBucketFromType(bucketType), CustomDispenserBehaviors.EMPTY);
                } else if (bucketType == SpecialBucketTypes.PUFFERFISH) {
                    bucketMaterial.addBucketType(bucketType, new FishBucketItem(EntityType.PUFFERFISH, Fluids.WATER, new Item.Settings().maxCount(1).group(ItemGroup.MISC)));
                    DispenserBlock.registerBehavior(bucketMaterial.getBucketFromType(bucketType), CustomDispenserBehaviors.EMPTY);
                } else if (bucketType == SpecialBucketTypes.TROPICAL_FISH) {
                    bucketMaterial.addBucketType(bucketType, new FishBucketItem(EntityType.TROPICAL_FISH, Fluids.WATER, new Item.Settings().maxCount(1).group(ItemGroup.MISC)));
                    DispenserBlock.registerBehavior(bucketMaterial.getBucketFromType(bucketType), CustomDispenserBehaviors.EMPTY);
                } else if (Registry.FLUID.getId(Registry.FLUID.get(bucketType)).toString().equals(bucketType.toString())) {
                    if (Registry.FLUID.get(bucketType) == Fluids.EMPTY) {
                        bucketMaterial.addBucketType(bucketType, new BucketItem(Registry.FLUID.get(bucketType), new Item.Settings().maxCount(16).group(ItemGroup.MISC)));
                        DispenserBlock.registerBehavior(bucketMaterial.getBucketFromType(bucketType), CustomDispenserBehaviors.FILL);
                    } else {
                        bucketMaterial.addBucketType(bucketType, new BucketItem(Registry.FLUID.get(bucketType), new Item.Settings().maxCount(1).group(ItemGroup.MISC)));
                        DispenserBlock.registerBehavior(bucketMaterial.getBucketFromType(bucketType), CustomDispenserBehaviors.EMPTY);
                    }
                }
            }
        });
        RegistryEntryAddedCallback.event(Registry.ITEM).register((i, identifier, item) -> {
            if (item instanceof BucketItem) {
                for (Fluid fluid : Registry.FLUID) {
                    if (Registry.ITEM.getId(fluid.getBucketItem()).toString().equals(identifier.toString()) && fluid.isStill(fluid.getDefaultState())) {
                        registerBucketType(Registry.FLUID.getId(fluid));
                        break;
                    }
                }
            }
        });
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) Registry.register(BUCKETS, new Identifier("gold"), new BucketMaterial(new Identifier("gold"), Items.GOLD_INGOT, 0xf5ef42));
    }

    public static void registerBucketType(Identifier identifier) {
        if (!BUCKET_TYPES.contains(identifier)) {
            BUCKET_TYPES.add(identifier);
            if (Registry.FLUID.containsId(identifier)) {
                BUCKETS.forEach(bucketMaterial -> {
                    if (!BUCKETS.getId(bucketMaterial).toString().equals(new Identifier("iron").toString())) {
                        bucketMaterial.addBucketType(identifier, new BucketItem(Registry.FLUID.get(identifier), new Item.Settings().group(ItemGroup.MISC).maxCount(1)));
                        DispenserBlock.registerBehavior(bucketMaterial.getBucketFromType(identifier), CustomDispenserBehaviors.EMPTY);
                    } else {
                        bucketMaterial.setBucketForAType(identifier, Registry.FLUID.get(identifier).getBucketItem());
                    }
                });
                if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
                    BucketColorManager.newBucketType(identifier);
                }
            }
        }
    }

    public static BucketMaterial getMaterialFromBucket(Item bucket) {
        for (BucketMaterial bucketMaterial : BUCKETS) {
            if (bucketMaterial.containsBucket(bucket)) return bucketMaterial;
        }
        System.out.println("Oof");
        return BUCKETS.get(new Identifier("iron"));
    }

    public static Identifier getTypeFromBucket(Item bucket) {
        for (BucketMaterial bucketMaterial : BUCKETS) {
            if (bucketMaterial.containsBucket(bucket)) return bucketMaterial.getTypeFromBucket(bucket);
        }
        System.out.println("Oof");
        return new Identifier("empty");
    }

    private static <T> DefaultedRegistry<T> createRegistry(String string, String string2, Supplier<T> defaultEntry) {
        return (DefaultedRegistry)putDefaultEntry(string, new DefaultedRegistry(string2), defaultEntry);
    }

    private static <T, R extends MutableRegistry<T>> R putDefaultEntry(String id, R mutableRegistry, Supplier<T> supplier) {
        Identifier identifier = new Identifier(id);
        return (R) Registry.REGISTRIES.add(identifier, mutableRegistry);
    }
}
