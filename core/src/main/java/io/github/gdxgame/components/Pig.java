package io.github.gdxgame.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Pig extends Image {
    private int health;
    private String type;

    public Pig(Texture texture, float x, float y, float width, float height, int health, String type) {
        super(texture);
        this.setPosition(x, y);
        this.setSize(width, height);
        this.health = health;
        this.type = type;
    }

    public boolean takeHit(int impact) {
        health -= impact;
        return health <= 0;
    }

    public int getHealth() {
        return health;
    }

    public String getType() {
        return type;
    }
}
