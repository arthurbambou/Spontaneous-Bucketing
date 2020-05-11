package io.github.vampirestudios.spontaneousbucketing.impl;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public abstract class BucketType {
    private String identifier;

    public BucketType(Identifier identifier) {
        this(identifier.toString());
    }

    public BucketType(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Identifier getId() {
        return new Identifier(identifier);
    }

    public abstract Item createItem();

    public abstract void dispenseBehavior(BucketMaterial bucketMaterial);
}
