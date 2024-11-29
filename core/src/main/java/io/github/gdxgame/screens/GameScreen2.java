package io.github.gdxgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import io.github.gdxgame.MyGame;
import io.github.gdxgame.components.Bird;
import io.github.gdxgame.components.Block;
import io.github.gdxgame.components.Pig;
import io.github.gdxgame.util.Physics;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

public class GameScreen2 implements Screen {

    private MyGame game;
    private Stage stage;
    private SpriteBatch batch;
    private Texture backgroundTexture, pauseButtonTexture, slingshotTexture, redBirdTexture, yellowBirdTexture, blueBirdTexture, soundOnTexture, soundOffTexture, woodBlockTexture;
    private Image soundToggleButton, currentBird;
    private Label scoreLabel;
    private BitmapFont customFont;
    private ShapeRenderer shapeRenderer;

    private int score;
    private int currentLevel;
    private Vector2 birdStartPosition, birdVelocity, gravity;
    private boolean isDragging, birdLaunched;
    private List<Vector2> trajectoryPoints;
    private List<Block> obstacles;
    private List<Pig> pigs;
    private Queue<Bird> birdQueue;
    private List<Bird> linedUpBirds;


    public GameScreen2(MyGame game) {
        this.game = game;
        this.currentLevel = 2;
        shapeRenderer = new ShapeRenderer();
        score = 0;
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("bg2.jpeg"));
        pauseButtonTexture = new Texture(Gdx.files.internal("Pause_button.png"));
        soundOnTexture = new Texture(Gdx.files.internal("sound_on.png"));
        soundOffTexture = new Texture(Gdx.files.internal("sound_off.png"));
        woodBlockTexture = new Texture(Gdx.files.internal("wood_square.png"));
        slingshotTexture = new Texture(Gdx.files.internal("slingshot.png"));
        redBirdTexture = new Texture(Gdx.files.internal("bird_1.png"));
        yellowBirdTexture = new Texture(Gdx.files.internal("bird_2.png"));
        blueBirdTexture = new Texture(Gdx.files.internal("bird_3.png"));
        customFont = new BitmapFont(Gdx.files.internal("myfont.fnt"));
        birdStartPosition = new Vector2(150, 100);
        birdVelocity = new Vector2(0, 0);
        gravity = new Vector2(0, -500);
        isDragging = false;
        birdLaunched = false;
        trajectoryPoints = new ArrayList<>();
        birdQueue = new LinkedList<>();
        linedUpBirds = new ArrayList<>();
        obstacles = new ArrayList<>();
        pigs = new ArrayList<>();

        setupBackground();
        setupUI();
        setupSlingshotAndBird();
        setupStructures();
    }

    private void setupBackground() {
        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);
    }

    private void setupUI() {
        Image pauseButton = new Image(pauseButtonTexture);
        pauseButton.setSize(pauseButton.getWidth() * 0.45f, pauseButton.getHeight() * 0.45f);
        pauseButton.setPosition(10, Gdx.graphics.getHeight() - pauseButton.getHeight() - 10);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setGameScreen(MyGame.ScreenType.PAUSE, currentLevel, score);
            }
        });
        stage.addActor(pauseButton);

        soundToggleButton = new Image(game.isSoundOn() ? soundOnTexture : soundOffTexture);
        soundToggleButton.setSize(soundOnTexture.getWidth() * 0.3f, soundOnTexture.getHeight() * 0.3f);
        soundToggleButton.setPosition(10, 10);
        soundToggleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.toggleSound();
                soundToggleButton.setDrawable(new Image(game.isSoundOn() ? soundOnTexture : soundOffTexture).getDrawable());
            }
        });
        stage.addActor(soundToggleButton);

        scoreLabel = new Label("Score\n" + score, new LabelStyle(customFont, Color.GOLD));
        scoreLabel.setPosition(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 85);
        stage.addActor(scoreLabel);
    }

    private void setupSlingshotAndBird() {
        Image slingshot = new Image(slingshotTexture);
        slingshot.setSize(60, 100);
        slingshot.setPosition(birdStartPosition.x - 30, birdStartPosition.y - 30);
        stage.addActor(slingshot);
        setupBirds();
        loadNextBird();
    }
    @Override
    public void show() {
        setupBackground();
        setupUI();
        setupSlingshotAndBird();
        setupStructures();
    }

    private void setupBirds() {
        float birdWidth = 40, birdHeight = 40;
        float lineupStartX = birdStartPosition.x - 95;
        float lineupStartY = birdStartPosition.y - 40;
        linedUpBirds = new ArrayList<>();
        birdQueue = new LinkedList<>();

        for (int i = 0; i < 16; i++) {
            if (i % 3 == 0) {
                birdQueue.add(new Bird(redBirdTexture, 1.2f, 2, 40, 40));
            } else if (i % 3 == 1) {
                birdQueue.add(new Bird(yellowBirdTexture, 1.5f, 2, 40 , 40));
            } else if (i % 3 == 2) {
                birdQueue.add(new Bird(blueBirdTexture, 1.2f, 1, 40, 40));
            }
        }

        for (int i = 0; i < 3; i++) {
            if (!birdQueue.isEmpty()) {
                Bird bird = (Bird) birdQueue.poll();
                bird.setSize(birdWidth, birdHeight);
                bird.setPosition(lineupStartX + 85 - i * birdWidth, lineupStartY);
                linedUpBirds.add(bird);
                stage.addActor(bird);
            }
        }
    }

    private void setupStructures() {
        obstacles = new ArrayList<>();
        pigs = new ArrayList<>();
        float baseX = 350;
        float baseY = 50;
        float scaleFactor = 0.99f;

        Texture woodTexture = new Texture(Gdx.files.internal("wood_horizontal1.png"));
        Texture glassTexture = new Texture(Gdx.files.internal("glass_box.png"));
        Texture stoneTexture = new Texture(Gdx.files.internal("stone_rectangle.png"));

        for (int i = 0; i < 5; i++) {
            Block woodBlock = new Block(woodTexture, baseX + i * 50 * scaleFactor, baseY, 50 * scaleFactor, 20 * scaleFactor, 2, "wood");
            obstacles.add(woodBlock);
            stage.addActor(woodBlock);
        }

        for (int i = 0; i < 2; i++) {
            Block woodColumn = new Block(woodTexture, baseX + i * 150 * scaleFactor, baseY + 20 * scaleFactor, 20 * scaleFactor, 80 * scaleFactor, 2, "wood");
            obstacles.add(woodColumn);
            stage.addActor(woodColumn);
        }

        Block firstFloor = new Block(woodTexture, baseX + 30 * scaleFactor, baseY + 100 * scaleFactor, 180 * scaleFactor, 20 * scaleFactor, 2, "wood");
        obstacles.add(firstFloor);
        stage.addActor(firstFloor);

        for (int i = 0; i < 3; i++) {
            Block glassBlock = new Block(glassTexture, baseX + i * 60 * scaleFactor + 20 * scaleFactor, baseY + 120 * scaleFactor, 50 * scaleFactor, 25 * scaleFactor, 1, "glass");
            obstacles.add(glassBlock);
            stage.addActor(glassBlock);
        }

        for (int i = 0; i < 2; i++) { // Reduced the number of columns
            Block stoneColumn = new Block(stoneTexture, baseX + i * 120 * scaleFactor + 20 * scaleFactor, baseY + 140 * scaleFactor, 20 * scaleFactor, 80 * scaleFactor, 3, "stone");
            obstacles.add(stoneColumn);
            stage.addActor(stoneColumn);
        }

        Block secondFloor = new Block(woodTexture, baseX + 30 * scaleFactor, baseY + 200 * scaleFactor, 180 * scaleFactor, 20 * scaleFactor, 2, "wood");
        obstacles.add(secondFloor);
        stage.addActor(secondFloor);

        Pig pig1 = new Pig(new Texture(Gdx.files.internal("pig_queen.png")), baseX + 60 * scaleFactor, baseY + 220 * scaleFactor, 38, 38, 2, "queen");
        pigs.add(pig1);
        stage.addActor(pig1);

        Pig pig2 = new Pig(new Texture(Gdx.files.internal("pig_minister.png")), baseX + 120 * scaleFactor, baseY + 220 * scaleFactor, 38, 38, 1, "minister");
        pigs.add(pig2);
        stage.addActor(pig2);

        for (int i = 0; i < 2; i++) {
            Block topGlass = new Block(glassTexture, baseX + i * 60 * scaleFactor + 50 * scaleFactor, baseY + 260 * scaleFactor, 50 * scaleFactor, 20 * scaleFactor, 1, "glass");
            obstacles.add(topGlass);
            stage.addActor(topGlass);
        }

        for (int i = 0; i < 2; i++) {
            Block leftStone = new Block(stoneTexture, baseX - 70 * scaleFactor, baseY + i * 50 * scaleFactor, 50 * scaleFactor, 50 * scaleFactor, 3, "stone");
            obstacles.add(leftStone);
            stage.addActor(leftStone);
        }

        Pig leftPig = new Pig(new Texture(Gdx.files.internal("pig_minister.png")), baseX - 50 * scaleFactor, baseY + 100 * scaleFactor, 38, 38, 1, "minister");
        pigs.add(leftPig);
        stage.addActor(leftPig);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.8f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        if (currentBird != null) {
            shapeRenderer.setProjectionMatrix(stage.getViewport().getCamera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.BLACK);

            for (Vector2 point : trajectoryPoints) {
                shapeRenderer.circle(point.x, point.y, 2);
            }
            shapeRenderer.end();

            if (birdLaunched && !isDragging) {
                birdVelocity.add(gravity.x * delta, gravity.y * delta);
                currentBird.setPosition(
                    currentBird.getX() + birdVelocity.x * delta,
                    currentBird.getY() + birdVelocity.y * delta
                );

                int impact = ((Bird) currentBird).getImpact();
                boolean collisionOccurred = Physics.handleCollisions(currentBird, obstacles, pigs, impact);
                if (collisionOccurred) {
                    score += impact;
                    scoreLabel.setText("Score\n" + score);
                    loadNextBird();
                }

                if (currentBird.getY() < 0 || currentBird.getX() < 0 || currentBird.getX() > Gdx.graphics.getWidth() || collisionOccurred) {
                    reloadCurrentBird();
                }

                checkWinLoseConditions();
            }
        }
    }

    private void checkWinLoseConditions() {
        if (pigs.isEmpty()) {
            game.setScreen(new WinScreen(game, score, currentLevel));
            return;
        }
        if (birdQueue.isEmpty() && !pigs.isEmpty()) {
            game.setScreen(new LoseScreen(game, score, currentLevel));

        }
    }

    private void reloadCurrentBird() {
        if (currentBird != null) {
            currentBird.setPosition(birdStartPosition.x - 30, birdStartPosition.y + 35);
            birdVelocity.set(0, 0);
            birdLaunched = false;
            trajectoryPoints.clear();
        }
    }


    private void loadNextBird() {
        if (birdQueue.isEmpty()) {
            System.out.println("No more birds in the queue!");
            return;
        }

        if (currentBird != null) {
            stage.getActors().removeValue(currentBird, true);
            currentBird = null;
        }

        if (!linedUpBirds.isEmpty()) {
            Image birdToRemove = linedUpBirds.remove(0);

            float lineupStartX = birdStartPosition.x - 95;
            float lineupStartY = birdStartPosition.y - 40;
            float birdWidth = 40;

            for (int i = 0; i < linedUpBirds.size(); i++) {
                Image bird = linedUpBirds.get(i);
                bird.setPosition(lineupStartX + 85 - i * birdWidth, lineupStartY);
            }

            birdToRemove.setPosition(lineupStartX + 85 - linedUpBirds.size() * birdWidth, lineupStartY);
            linedUpBirds.add((Bird) birdToRemove);
        }

        currentBird = birdQueue.poll();
        if (currentBird != null) {
            currentBird.setPosition(birdStartPosition.x - 30, birdStartPosition.y + 35);
            stage.addActor(currentBird);

            birdLaunched = false;
            birdVelocity.set(0, 0);
            trajectoryPoints.clear();

            currentBird.clearListeners();
            currentBird.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (!birdLaunched) isDragging = true;
                    return true;
                }

                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    if (isDragging) {
                        currentBird.setPosition(
                            Gdx.input.getX() - currentBird.getWidth() / 2,
                            Gdx.graphics.getHeight() - Gdx.input.getY() - currentBird.getHeight() / 2
                        );
                        calculateTrajectory();
                    }
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    if (isDragging) {
                        isDragging = false;
                        birdLaunched = true;
                        birdVelocity.set(
                            birdStartPosition.x - currentBird.getX(),
                            birdStartPosition.y - currentBird.getY()
                        ).scl(5);
                    }
                }
            });
        } else {
            System.out.println("Queue has birds, but none are loading!");
        }
    }

    private void calculateTrajectory() {
        trajectoryPoints = Physics.calculateTrajectory(
            birdStartPosition,
            new Vector2(currentBird.getX(), currentBird.getY()),
            gravity,
            30,
            0.1f
        );
    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        backgroundTexture.dispose();
        pauseButtonTexture.dispose();
        slingshotTexture.dispose();
        redBirdTexture.dispose();
        soundOnTexture.dispose();
        soundOffTexture.dispose();
        woodBlockTexture.dispose();
    }
}
