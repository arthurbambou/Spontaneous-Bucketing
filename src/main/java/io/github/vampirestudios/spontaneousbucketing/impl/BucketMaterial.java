package io.github.vampirestudios.spontaneousbucketing.impl;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class BucketMaterial {
    private Identifier ID;
    private Item material;
    private int color = -1;
    private Map<Identifier, Identifier> bucketTypeMap = new HashMap<>();
    private static final Identifier NULL = new Identifier("null");

    public BucketMaterial(Identifier ID, Item material) {
        this.ID = ID;
        this.material = material;
    }

    public BucketMaterial(Identifier ID, Item material, int color) {
        this.ID = ID;
        this.material = material;
        this.color = color;
    }

    public Identifier getID() {
        return ID;
    }

    public Item getMaterial() {
        return material;
    }

    public BucketMaterial addBucketType(Identifier type, Item bucket) {
        if (!hasBeenRegistered(bucket)) Registry.register(Registry.ITEM,
                new Identifier(this.ID.getNamespace(), this.ID.getPath() + "_" +
                        type.toString().replace("minecraft:","").replace(":","_") + "_bucket"), bucket);
        this.bucketTypeMap.putIfAbsent(type, Registry.ITEM.getId(bucket));
        return this;
    }

    public BucketMaterial setBucketForAType(Identifier type, Item bucket) {
        if (this.bucketTypeMap.containsKey(type)) this.bucketTypeMap.replace(type, Registry.ITEM.getId(bucket));
        else this.bucketTypeMap.put(type, Registry.ITEM.getId(bucket));
        return this;
    }

    private static boolean hasBeenRegistered(Item item) {
        return Registry.ITEM.get(Registry.ITEM.getId(item)) == item;
    }

    public boolean containsBucket(Item bucket) {
        return getTypeFromBucket(bucket) != NULL;
    }

    public Item getBucketFromType(Identifier identifier) {
        return Registry.ITEM.get(this.bucketTypeMap.get(identifier));
    }

    public Identifier getTypeFromBucket(Item bucket) {
        for (Map.Entry<Identifier, Identifier> entry : this.bucketTypeMap.entrySet()) {
            if (entry.getValue().toString().equals(Registry.ITEM.getId(bucket).toString())) return entry.getKey();
        }
        return NULL;
    }
}
