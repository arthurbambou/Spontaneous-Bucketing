package io.github.vampirestudios.spontaneousbucketing.impl;

import io.github.vampirestudios.spontaneousbucketing.client.styles.Style;
import io.github.vampirestudios.spontaneousbucketing.client.styles.Styles;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BucketMaterial {
    private Identifier ID;
    private Item material;
    private int color;
    private Style style;
    private Map<Identifier, Identifier> bucketTypeMap = new HashMap<>();
    private static final Identifier NULL = new Identifier("null");

    public BucketMaterial(Identifier ID, Item material) {
        this(ID, material, Styles.VANILLA);
    }

    public BucketMaterial(Identifier ID, Item material, Style style) {
        this(ID, material, -1, style);
    }

    public BucketMaterial(Identifier ID, Item material, int color) {
        this(ID, material, color, Styles.VANILLA);
    }

    public BucketMaterial(Identifier ID, Item material, int color, Style style) {
        this.ID = ID;
        this.material = material;
        this.color = color;
        this.style = style;
    }

    public Set<Map.Entry<Identifier, Identifier>> getEntrySet() {
        return this.bucketTypeMap.entrySet();
    }

    public Style getStyle() {
        return style;
    }

    public int getColor() {
        return color;
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
        if (this.bucketTypeMap.containsKey(type)) {
            if (Registry.ITEM.getId(bucket).toString().equals(new Identifier("air").toString())) {
                Registry.ITEM.set(Registry.ITEM.getRawId(Registry.ITEM.get(this.bucketTypeMap.get(type))),
                        new Identifier(this.ID.getNamespace(), this.ID.getPath() + "_" +
                        type.toString().replace("minecraft:","").replace(":","_") + "_bucket"), bucket);
            } else {
                Registry.ITEM.set(Registry.ITEM.getRawId(Registry.ITEM.get(this.bucketTypeMap.get(type))),
                        new Identifier(this.ID.getNamespace(), this.ID.getPath() + "_" +
                                type.toString().replace("minecraft:","").replace(":","_") + "_bucket"), null);
            }
            this.bucketTypeMap.replace(type, Registry.ITEM.getId(bucket));
        } else this.bucketTypeMap.put(type, Registry.ITEM.getId(bucket));
        return this;
    }

    public BucketMaterial setBucketForAType(BucketType bucketType) {
        return this.setBucketForAType(bucketType, bucketType.createItem());
    }

    public BucketMaterial setBucketForAType(BucketType bucketType, Item bucket) {
        return this.setBucketForAType(bucketType.getId(), bucket);
    }

    private static boolean hasBeenRegistered(Item item) {
        return Registry.ITEM.get(Registry.ITEM.getId(item)) == item;
    }

    public boolean containsBucket(Item bucket) {
        return getITypeFromBucket(bucket) != NULL;
    }

    public Item getBucketFromType(Identifier identifier) {
        return Registry.ITEM.get(this.bucketTypeMap.get(identifier));
    }

    public Item getBucketFromType(BucketType bucketType) {
        return this.getBucketFromType(bucketType.getId());
    }

    public Identifier getITypeFromBucket(Item bucket) {
        for (Map.Entry<Identifier, Identifier> entry : this.bucketTypeMap.entrySet()) {
            if (entry.getValue().toString().equals(Registry.ITEM.getId(bucket).toString())) return entry.getKey();
        }
        return NULL;
    }
}
