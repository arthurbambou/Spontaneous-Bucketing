package fr.arthurbambou.spontaneousbucketing.impl;

import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
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
                iron.addBucketType(Registry.FLUID.getId(fluid), fluid.getBucketItem());
            } else if (fluid == Fluids.EMPTY) {
                BUCKET_TYPES.add(Registry.FLUID.getId(fluid));
                iron.addBucketType(Registry.FLUID.getId(fluid), Items.BUCKET);
            }
        }
        iron.addBucketType(SpecialBucketTypes.MILK, Items.MILK_BUCKET)
                .addBucketType(SpecialBucketTypes.PUFFERFISH, Items.PUFFERFISH_BUCKET)
                .addBucketType(SpecialBucketTypes.SALMON, Items.SALMON_BUCKET)
                .addBucketType(SpecialBucketTypes.COD, Items.COD_BUCKET)
                .addBucketType(SpecialBucketTypes.TROPICAL_FISH, Items.TROPICAL_FISH_BUCKET);
        Registry.register(BUCKETS, iron.getID(), iron);

        RegistryEntryAddedCallback.event(BUCKETS).register((i, identifier, bucketMaterial) -> {
            for (Identifier bucketType : BUCKET_TYPES) {
                if (bucketType == SpecialBucketTypes.MILK) {
                    bucketMaterial.addBucketType(bucketType, new MilkBucketItem(new Item.Settings().maxCount(1).group(ItemGroup.MISC)));
                } else if (bucketType == SpecialBucketTypes.COD) {
                    bucketMaterial.addBucketType(bucketType, new FishBucketItem(EntityType.COD, Fluids.WATER, new Item.Settings().maxCount(1).group(ItemGroup.MISC)));
                } else if (bucketType == SpecialBucketTypes.SALMON) {
                    bucketMaterial.addBucketType(bucketType, new FishBucketItem(EntityType.SALMON, Fluids.WATER, new Item.Settings().maxCount(1).group(ItemGroup.MISC)));
                } else if (bucketType == SpecialBucketTypes.PUFFERFISH) {
                    bucketMaterial.addBucketType(bucketType, new FishBucketItem(EntityType.PUFFERFISH, Fluids.WATER, new Item.Settings().maxCount(1).group(ItemGroup.MISC)));
                } else if (bucketType == SpecialBucketTypes.TROPICAL_FISH) {
                    bucketMaterial.addBucketType(bucketType, new FishBucketItem(EntityType.TROPICAL_FISH, Fluids.WATER, new Item.Settings().maxCount(1).group(ItemGroup.MISC)));
                } else if (Registry.FLUID.containsId(bucketType)) {
                    bucketMaterial.addBucketType(bucketType, new BucketItem(Registry.FLUID.get(bucketType), new Item.Settings().maxCount(1).group(ItemGroup.MISC)));
                }
            }
        });
        RegistryEntryAddedCallback.event(Registry.FLUID).register((i, identifier, fluid) -> {
            if (fluid.isStill(fluid.getDefaultState())) registerBucketType(identifier);
        });
        Registry.register(BUCKETS, new Identifier("gold"), new BucketMaterial(new Identifier("gold"), Items.GOLD_INGOT, 0xf5ef42));
    }

    public static void registerBucketType(Identifier identifier) {
        if (!BUCKET_TYPES.contains(identifier)) {
            BUCKET_TYPES.add(identifier);
            if (Registry.FLUID.containsId(identifier)) {
                BUCKETS.forEach(bucketMaterial -> {
                    if (BUCKETS.getId(bucketMaterial) != new Identifier("iron")) {
                        bucketMaterial.addBucketType(identifier, new BucketItem(Registry.FLUID.get(identifier), new Item.Settings().group(ItemGroup.MISC).maxCount(1)));
                    } else {
                        bucketMaterial.addBucketType(identifier, Registry.FLUID.get(identifier).getBucketItem());
                    }
                });
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

    private static <T> DefaultedRegistry<T> createRegistry(String string, String string2, Supplier<T> defaultEntry) {
        return (DefaultedRegistry)putDefaultEntry(string, new DefaultedRegistry(string2), defaultEntry);
    }

    private static <T, R extends MutableRegistry<T>> R putDefaultEntry(String id, R mutableRegistry, Supplier<T> supplier) {
        Identifier identifier = new Identifier(id);
        return (R) Registry.REGISTRIES.add(identifier, mutableRegistry);
    }
}
