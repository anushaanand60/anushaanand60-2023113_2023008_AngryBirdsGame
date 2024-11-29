package io.github.gdxgame.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import io.github.gdxgame.components.Block;
import io.github.gdxgame.components.Pig;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;
import java.util.List;

public class Physics {

    public static List<Vector2> calculateTrajectory(Vector2 startPosition, Vector2 targetPosition, Vector2 gravity, int steps, float timeStep) {
        List<Vector2> trajectoryPoints = new ArrayList<>();
        Vector2 velocity = new Vector2(startPosition.x - targetPosition.x, startPosition.y - targetPosition.y).scl(5);
        Vector2 position = new Vector2(targetPosition.x, targetPosition.y);
        for (int i = 0; i < steps; i++) {
            position.add(velocity.x * timeStep, velocity.y * timeStep);
            velocity.add(gravity.x * timeStep, gravity.y * timeStep);
            trajectoryPoints.add(new Vector2(position.x, position.y));
        }
        return trajectoryPoints;
    }

    public static boolean handleCollisions(Image bird, List<Block> blocks, List<Pig> pigs, int impact) {
        boolean collisionOccurred = false;
        List<Block> blocksToRemove = new ArrayList<>();
        List<Pig> pigsToRemove = new ArrayList<>();

        for (Block block : new ArrayList<>(blocks)) {
            if (bird.getX() < block.getX() + block.getWidth() &&
                bird.getX() + bird.getWidth() > block.getX() &&
                bird.getY() < block.getY() + block.getHeight() &&
                bird.getY() + bird.getHeight() > block.getY()) {

                if (block.takeHit(impact)) {
                    dealHitToBelow(blocks, pigs, block.getX(), block.getY());
                    blocksToRemove.add(block);
                    blocks.remove(block);
                }
                collisionOccurred = true;
            }
        }

        for (Pig pig : new ArrayList<>(pigs)) {
            if (bird.getX() < pig.getX() + pig.getWidth() &&
                bird.getX() + bird.getWidth() > pig.getX() &&
                bird.getY() < pig.getY() + pig.getHeight() &&
                bird.getY() + bird.getHeight() > pig.getY()) {

                if (pig.takeHit(impact)) {
                    pigsToRemove.add(pig);
                    pigs.remove(pig);
                }
                collisionOccurred = true;
            }
        }
        for (Block block : blocksToRemove) {
            initiateFall(block, () -> {});
        }
        for (Pig pig : pigsToRemove) {
            initiateFall(pig, () -> {});
        }
        return collisionOccurred;
    }

    private static void dealHitToBelow(List<Block> blocks, List<Pig> pigs, float blockX, float blockY) {
        List<Block> blocksToRemove = new ArrayList<>();
        List<Pig> pigsToRemove = new ArrayList<>();

        for (Block block : new ArrayList<>(blocks)) {
            if (block.getX() == blockX && block.getY() < blockY) {
                if (block.takeHit(1)) {
                    blocksToRemove.add(block);
                    blocks.remove(block);
                }
            }
        }

        for (Pig pig : new ArrayList<>(pigs)) {
            if (pig.getX() == blockX && pig.getY() < blockY) {
                if (pig.takeHit(1)) {
                    pigsToRemove.add(pig);
                    pigs.remove(pig);
                }
            }
        }
        for (Block block : blocksToRemove) {
            initiateFall(block, () -> {});
        }
        for (Pig pig : pigsToRemove) {
            initiateFall(pig, () -> {});
        }
    }

    private static void initiateFall(Image entity, Runnable onRemove) {
        entity.addAction(Actions.sequence(
            Actions.moveBy(0, -entity.getY(), 1f),
            Actions.run(() -> {
                onRemove.run();
                entity.remove();
            })
        ));
    }
}
