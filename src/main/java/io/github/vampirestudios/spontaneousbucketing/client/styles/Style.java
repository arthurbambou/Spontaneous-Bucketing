package io.github.vampirestudios.spontaneousbucketing.client.styles;

public class Style {
    private String name;
    private boolean hasCustomLiquidTextures;

    public Style(String name) {
        this.name = name;
        this.hasCustomLiquidTextures = false;
    }

    public Style(String name, boolean hasCustomLiquidTextures) {
        this.name = name;
        this.hasCustomLiquidTextures = hasCustomLiquidTextures;
    }

    public String getName() {
        return name;
    }

    public boolean hasCustomLiquidTextures() {
        return hasCustomLiquidTextures;
    }
}
