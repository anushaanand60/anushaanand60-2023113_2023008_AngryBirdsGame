package io.github.gdxgame.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Block extends Image {
    private int durability;
    private String material;

    public Block(Texture texture, float x, float y, float width, float height, int durability, String material) {
        super(texture);
        this.durability = durability;
        this.material = material;
        setPosition(x, y);
        setSize(width, height);
    }

    public boolean takeHit(int impact) {
        durability -= impact;
        return durability <= 0;
    }

    public int getDurability() {
        return durability;
    }

    public String getMaterial() {
        return material;
    }
}
