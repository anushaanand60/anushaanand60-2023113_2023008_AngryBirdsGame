package io.github.gdxgame.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Bird extends Image {
    private float speed;
    private int impact;
    private float width;
    private float height;

    public Bird(Texture texture, float speed, int impact, float width, float height) {
        super(texture);
        this.speed = speed;
        this.impact = impact;
        this.width = width;
        this.height = height;
        setSize(width, height);
    }

    public float getSpeed() {
        return speed;
    }

    public int getImpact() {
        return impact;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
