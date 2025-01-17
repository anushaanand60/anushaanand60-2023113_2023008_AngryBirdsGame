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
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import io.github.gdxgame.MyGame;
import com.badlogic.gdx.math.Vector2;
import io.github.gdxgame.components.Bird;
import io.github.gdxgame.components.Block;
import io.github.gdxgame.components.Pig;
import io.github.gdxgame.util.Physics;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

public class GameScreen1 implements Screen {
    private MyGame game;
    private Stage stage;
    private SpriteBatch batch;
    private Texture backgroundTexture, pauseButtonTexture, slingshotTexture, redBirdTexture, blueBirdTexture, yellowBirdTexture, whiteBirdTexture, soundOnTexture, soundOffTexture, pigTexture, woodBlockTexture, quitButtonTexture, dummyButtonTexture;
    private Image soundToggleButton, currentBird;
    private Label scoreLabel;
    private BitmapFont customFont;
    private ShapeRenderer shapeRenderer;

    private int score;
    private int currentLevel; // To track the current level
    private Vector2 birdStartPosition, birdVelocity, gravity;
    private boolean isDragging, birdLaunched;
    private List<Vector2> trajectoryPoints;

    private List<Block> obstacles; // List of structural blocks
    private List<Pig> pigs; // List for all structural blocks and pigs
    private Queue<Image> birdQueue; // Lineup of birds waiting to be launched
    private int currentBirdIndex;
    private List<Image> linedUpBirds;// Track the current bird being used

    public GameScreen1(MyGame game) {
        this.game = game;
        this.currentLevel = 1;
        shapeRenderer = new ShapeRenderer();
        score = 0;
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);
        backgroundTexture = new Texture(Gdx.files.internal("bg1.jpg"));
        pauseButtonTexture = new Texture(Gdx.files.internal("Pause_button.png"));
        soundOnTexture = new Texture(Gdx.files.internal("sound_on.png"));
        soundOffTexture = new Texture(Gdx.files.internal("sound_off.png"));
        woodBlockTexture = new Texture(Gdx.files.internal("wood_square.png"));
        quitButtonTexture = new Texture(Gdx.files.internal("quit_button.png"));
        dummyButtonTexture = new Texture(Gdx.files.internal("dummy_button.png"));
        slingshotTexture = new Texture(Gdx.files.internal("slingshot.png"));
        redBirdTexture = new Texture(Gdx.files.internal("bird_1.png"));
        yellowBirdTexture = new Texture(Gdx.files.internal("bird_2.png"));
        blueBirdTexture = new Texture(Gdx.files.internal("bird_3.png"));
        whiteBirdTexture = new Texture(Gdx.files.internal("bird_4.png"));
        customFont = new BitmapFont(Gdx.files.internal("myfont.fnt"));
        birdStartPosition = new Vector2(150, 100);
        birdVelocity = new Vector2(0, 0);
        gravity = new Vector2(0, -500);
        isDragging = false;
        birdLaunched = false;
        trajectoryPoints = new ArrayList<>();
        birdQueue = new LinkedList<>();
        currentBirdIndex = 0;

        setupBackground();
        setupUI();
        setupSlingshotAndBird(); // Set up birds in the lineup
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
                // Pass the current level when setting the PauseScreen
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

        scoreLabel = new Label("Score\n" + score, new Label.LabelStyle(customFont, Color.GOLD));
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

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.8f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update and draw the stage
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
                    score += impact; // Increment score by the bird's impact
                    scoreLabel.setText("Score\n" + score);
                    loadNextBird();
                }

                // Check if bird is out of bounds or has completed its job
                if (currentBird.getY() < 0 || currentBird.getX() < 0 || currentBird.getX() > Gdx.graphics.getWidth() || collisionOccurred) {
                    reloadCurrentBird(); // Proceed to the next bird after current action finishes
                }

                // Check win/lose conditions
                checkWinLoseConditions();
            }
        }
    }

    private void checkWinLoseConditions() {
        // First, check if all pigs are eliminated
        if (pigs.isEmpty()) {
            // All pigs are dead: show the win screen
            game.setScreen(new WinScreen(game, score, currentLevel));
            return; // Exit the method to prevent further checks
        }

        // Then, check if no birds are left in the queue and pigs remain
        if (birdQueue.isEmpty() && !pigs.isEmpty()) {
            // Birds are exhausted and pigs remain: show the lose screen
            game.setScreen(new LoseScreen(game, score, currentLevel));
        }
    }

    private void setupBirds() {
        float birdWidth = 40, birdHeight = 40;
        float lineupStartX = birdStartPosition.x - 95; // Lineup starting X position
        float lineupStartY = birdStartPosition.y - 40;

        // Initialize the visual lineup list
        linedUpBirds = new ArrayList<>();
        birdQueue = new LinkedList<>(); // Reset the queue

        // Add birds to the queue with their attributes
        for (int i = 0; i < 10; i++) {
            if (i % 3 == 0) {
                birdQueue.add(new Bird(redBirdTexture, 1.2f, 2, 40, 40));  // Red bird
            } else if (i % 3 == 1) {
                birdQueue.add(new Bird(yellowBirdTexture, 1.5f, 2, 40 , 40));  // Yellow bird
            } else if (i % 3 == 2) {
                birdQueue.add(new Bird(whiteBirdTexture, 1.2f, 1, 40, 40));  // Blue bird
            }
        }

        // Add only the first 3 birds to the lineup visually
        for (int i = 0; i < 3; i++) {
            if (!birdQueue.isEmpty()) {
                Bird bird = (Bird) birdQueue.poll(); // Fetch the bird
                bird.setSize(birdWidth, birdHeight); // Set bird size
                bird.setPosition(lineupStartX + 85 - i * birdWidth, lineupStartY); // Set bird position
                linedUpBirds.add(bird); // Add bird to lineup
                stage.addActor(bird); // Add bird to stage
            }
        }
    }


    private void loadNextBird() {
        // If the queue is empty, check for the end of the game
        if (birdQueue.isEmpty()) {
            System.out.println("No more birds in the queue!");
            return;
        }

        // Remove the current bird from the slingshot, if any
        if (currentBird != null) {
            stage.getActors().removeValue(currentBird, true);
            currentBird = null; // Clear the reference
        }

        if (!linedUpBirds.isEmpty()) {
            Image birdToRemove = linedUpBirds.remove(0);

            // Update positions of remaining birds
            float lineupStartX = birdStartPosition.x - 95;
            float lineupStartY = birdStartPosition.y - 40;
            float birdWidth = 40;

            for (int i = 0; i < linedUpBirds.size(); i++) {
                Image bird = linedUpBirds.get(i);
                bird.setPosition(lineupStartX + 85 - i * birdWidth, lineupStartY);
            }

            // Move the removed bird to the back of the lineup visually
            birdToRemove.setPosition(lineupStartX + 85 - linedUpBirds.size() * birdWidth, lineupStartY);
            linedUpBirds.add(birdToRemove); // Add to the back of the lineup
        }


        // Load the next bird from the queue
        currentBird = birdQueue.poll();
        if (currentBird != null) {
            // Position the bird on the slingshot
            currentBird.setPosition(birdStartPosition.x - 30, birdStartPosition.y + 35);
            stage.addActor(currentBird);

            // Reset states
            birdLaunched = false;
            birdVelocity.set(0, 0);
            trajectoryPoints.clear();

            // Add listeners for dragging and launching the bird
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

    private void reloadCurrentBird() {
        if (currentBird != null) {
            currentBird.setPosition(birdStartPosition.x - 30, birdStartPosition.y + 35);
            birdVelocity.set(0, 0);
            birdLaunched = false;
            trajectoryPoints.clear();
        }
    }


    private void calculateTrajectory() {
        trajectoryPoints = Physics.calculateTrajectory(
            birdStartPosition,
            new Vector2(currentBird.getX(), currentBird.getY()),
            gravity,
            30, // Steps
            0.1f // Time step
        );
    }

    private void setupStructures() {
        obstacles = new ArrayList<>(); // Initialize the list for blocks
        pigs = new ArrayList<>();      // Initialize the list for pigs

        float baseX = 350, baseY = 70;

        // ** Block Textures **
        Texture woodTexture = new Texture(Gdx.files.internal("wood_horizontal1.png"));
        Texture glassTexture = new Texture(Gdx.files.internal("glass_box.png"));
        Texture stoneTexture = new Texture(Gdx.files.internal("stone_rectangle.png"));

        // ** Bottom base (horizontal wood blocks) **
        for (int i = 0; i < 4; i++) {
            Block woodBlock = new Block(woodTexture, baseX + i * 50, baseY, 50, 20, 2, "wood");
            obstacles.add(woodBlock); // Add block to obstacles list
            stage.addActor(woodBlock); // Add block to the stage
        }

        // ** First layer vertical wood columns **
        for (int i = 0; i <= 3; i += 3) {
            Block woodColumn = new Block(woodTexture, baseX + i * 60, baseY + 20, 20, 80, 2, "wood");
            obstacles.add(woodColumn); // Add block to obstacles list
            stage.addActor(woodColumn); // Add block to the stage
        }

        // ** First floor (horizontal wood block) **
        Block firstFloor = new Block(woodTexture, baseX, baseY + 100, 205, 20, 2, "wood");
        obstacles.add(firstFloor); // Add block to obstacles list
        stage.addActor(firstFloor); // Add block to the stage

        // ** Second layer vertical glass columns **
        for (int i = 0; i <= 2; i += 2) {
            Block glassColumn = new Block(glassTexture, baseX + 38 + i * 50, baseY + 120, 20, 80, 1, "glass");
            obstacles.add(glassColumn); // Add block to obstacles list
            stage.addActor(glassColumn); // Add block to the stage
        }

        // ** Second floor (glass block) **
        Block glassBlock = new Block(glassTexture, baseX + 40, baseY + 200, 120, 65, 1, "glass");
        obstacles.add(glassBlock); // Add block to obstacles list
        stage.addActor(glassBlock); // Add block to the stage

        // ** Top layer (small stone block) **
        Block stoneBlock = new Block(stoneTexture, baseX + 80, baseY + 265, 40, 40, 3, "stone");
        obstacles.add(stoneBlock); // Add block to obstacles list
        stage.addActor(stoneBlock); // Add block to the stage

        // ** Pig Textures **
        Texture ministerTexture = new Texture(Gdx.files.internal("pig_minister.png"));
        Texture kingTexture = new Texture(Gdx.files.internal("pig_king.png"));

        // ** Adding pigs **
        Pig ministerPig = new Pig(ministerTexture, baseX + 80, baseY + 210, 40, 40, 1, "minister");
        pigs.add(ministerPig); // Add pig to pigs list
        stage.addActor(ministerPig); // Add pig to the stage

        Pig kingPig = new Pig(kingTexture, baseX + 79, baseY + 120, 40, 40, 3, "king");
        pigs.add(kingPig); // Add pig to pigs list
        stage.addActor(kingPig); // Add pig to the stage
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
    }
}
