package io.github.vampirestudios.spontaneousbucketing.impl;

import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.DispenserBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class BucketRegistry {

    public static final DefaultedRegistry<BucketMaterial> BUCKETS = createRegistry("spontaneousbucketing:buckets", "null", () -> null);

    private static final DefaultedRegistry<BucketType> BUCKET_TYPES = createRegistry("spontaneousbucketing:bucket_types", "null", () -> null);

    public static void init() {
        BucketMaterial iron = new BucketMaterial(new Identifier("iron"), Items.IRON_INGOT);
        registerBucketType(SpecialBucketTypes.MILK);
        registerBucketType(SpecialBucketTypes.PUFFERFISH);
        registerBucketType(SpecialBucketTypes.SALMON);
        registerBucketType(SpecialBucketTypes.COD);
        registerBucketType(SpecialBucketTypes.TROPICAL_FISH);
        for (Fluid fluid : Registry.FLUID) {
            if (fluid.isStill(null)) {
                registerBucketType(new BucketType(Registry.FLUID.getId(fluid)) {
                    @Override
                    public Item createItem() {
                        return new BucketItem(fluid, new Item.Settings().group(ItemGroup.MISC).maxCount(1));
                    }

                    @Override
                    public void dispenseBehavior(BucketMaterial bucketMaterial) {
                        DispenserBlock.registerBehavior(bucketMaterial.getBucketFromType(this.getId()), CustomDispenserBehaviors.EMPTY);
                    }
                });
                iron.setBucketForAType(Registry.FLUID.getId(fluid), fluid.getBucketItem());
            } else if (fluid == Fluids.EMPTY) {
                registerBucketType(new BucketType(Registry.FLUID.getId(fluid)) {
                    @Override
                    public Item createItem() {
                        return new BucketItem(Fluids.EMPTY, new Item.Settings().group(ItemGroup.MISC).maxCount(16));
                    }

                    @Override
                    public void dispenseBehavior(BucketMaterial bucketMaterial) {
                        DispenserBlock.registerBehavior(bucketMaterial.getBucketFromType(this.getId()), CustomDispenserBehaviors.FILL);
                    }
                });
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
            for (BucketType bucketType : BUCKET_TYPES) {
                bucketMaterial.addBucketType(bucketType.getId(), bucketType.createItem());
                bucketType.dispenseBehavior(bucketMaterial);
            }
        });
        RegistryEntryAddedCallback.event(Registry.ITEM).register((i, identifier, item) -> {
            if (item instanceof BucketItem) {
                for (Fluid fluid : Registry.FLUID) {
                    if (Registry.ITEM.getId(fluid.getBucketItem()).toString().equals(identifier.toString()) && fluid.isStill(fluid.getDefaultState())) {
                        if (BUCKET_TYPES.get(identifier) == null) {
                            BucketType bucketType = Registry.register(BUCKET_TYPES, identifier, new BucketType(identifier) {
                                @Override
                                public Item createItem() {
                                    return new BucketItem(fluid, new Item.Settings().maxCount(1).group(ItemGroup.MISC));
                                }

                                @Override
                                public void dispenseBehavior(BucketMaterial bucketMaterial) {
                                    DispenserBlock.registerBehavior(bucketMaterial.getBucketFromType(this.getId()), CustomDispenserBehaviors.EMPTY);
                                }
                            });
                            BUCKETS.forEach(bucketMaterial -> {
                                if (!BUCKETS.getId(bucketMaterial).toString().equals(new Identifier("iron").toString())) {
                                    bucketMaterial.addBucketType(new Identifier(bucketType.getIdentifier()), bucketType.createItem());
                                    bucketType.dispenseBehavior(bucketMaterial);
                                } else {
                                    System.out.println("Iron");
                                    bucketMaterial.setBucketForAType(identifier, fluid.getBucketItem());
                                }
                            });
                        }
                        break;
                    }
                }
            }
        });
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) Registry.register(BUCKETS, new Identifier("gold"), new BucketMaterial(new Identifier("gold"), Items.GOLD_INGOT, 0xf5ef42));
    }

    public static void registerBucketType(BucketType bucketType) {
        if (BUCKET_TYPES.getId(bucketType).toString().equals(new Identifier("null").toString())) {
            Registry.register(BUCKET_TYPES, new Identifier(bucketType.getIdentifier()), bucketType);
            BUCKETS.forEach(bucketMaterial -> {
                if (!BUCKETS.getId(bucketMaterial).toString().equals(new Identifier("iron").toString())) {
                    bucketMaterial.setBucketForAType(new Identifier(bucketType.getIdentifier()), bucketType.createItem());
                    bucketType.dispenseBehavior(bucketMaterial);
                }
            });
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
            if (bucketMaterial.containsBucket(bucket)) return bucketMaterial.getITypeFromBucket(bucket);
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
