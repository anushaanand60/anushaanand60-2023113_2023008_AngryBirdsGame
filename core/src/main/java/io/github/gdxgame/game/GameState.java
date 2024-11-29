package io.github.gdxgame.game;

import io.github.gdxgame.components.Bird;
import io.github.gdxgame.components.Block;
import io.github.gdxgame.components.Pig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class GameState implements Serializable {
    private int currentLevel;
    private int score;
    private List<Block> blocks;
    private List<Pig> pigs;
    private Queue<Bird> birdQueue;
    private Bird currentBird;
    private float currentBirdX, currentBirdY;
    private float birdVelocityX, birdVelocityY;

    public GameState(int currentLevel, int score, List<Block> blocks, List<Pig> pigs,
                     Queue<Bird> birdQueue, Bird currentBird, float currentBirdX,
                     float currentBirdY, float birdVelocityX, float birdVelocityY) {
        this.currentLevel = currentLevel;
        this.score = score;
        this.blocks = blocks;
        this.pigs = pigs;
        this.birdQueue = birdQueue;
        this.currentBird = currentBird;
        this.currentBirdX = currentBirdX;
        this.currentBirdY = currentBirdY;
        this.birdVelocityX = birdVelocityX;
        this.birdVelocityY = birdVelocityY;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getScore() {
        return score;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public List<Pig> getPigs() {
        return pigs;
    }

    public Queue<Bird> getBirdQueue() {
        return birdQueue;
    }

    public Bird getCurrentBird() {
        return currentBird;
    }

    public float getCurrentBirdX() {
        return currentBirdX;
    }

    public float getCurrentBirdY() {
        return currentBirdY;
    }

    public float getBirdVelocityX() {
        return birdVelocityX;
    }

    public float getBirdVelocityY() {
        return birdVelocityY;
    }
}
