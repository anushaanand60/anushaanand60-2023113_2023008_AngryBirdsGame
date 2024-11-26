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
import java.util.ArrayList;
import java.util.List;

public class GameScreen1 implements Screen {

    private MyGame game;
    private Stage stage;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Texture pauseButtonTexture;
    private Texture slingshotTexture;
    private Texture redBirdTexture;
    private Texture soundOnTexture;
    private Texture soundOffTexture;
    private Texture pigTexture;
    private Texture woodBlockTexture;
    private Texture quitButtonTexture;
    private Texture dummyButtonTexture;  // Dummy button texture
    private Image soundToggleButton;
    private Label scoreLabel;
    private BitmapFont customFont;
    private int score;
    private ShapeRenderer shapeRenderer;
    private Vector2 birdStartPosition, birdVelocity, gravity;
    private boolean isDragging, birdLaunched;
    private List<Vector2> trajectoryPoints;

    public GameScreen1(MyGame game) {
        this.game = game;
        shapeRenderer = new ShapeRenderer();
        this.score = 0;
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("bg1.jpg"));
        pauseButtonTexture = new Texture(Gdx.files.internal("Pause_button.png"));
        slingshotTexture = new Texture(Gdx.files.internal("slingshot.png"));
        redBirdTexture = new Texture(Gdx.files.internal("bird_1.png"));
        soundOnTexture = new Texture(Gdx.files.internal("sound_on.png"));
        soundOffTexture = new Texture(Gdx.files.internal("sound_off.png"));
        pigTexture = new Texture(Gdx.files.internal("pig_soldier.png"));
        woodBlockTexture = new Texture(Gdx.files.internal("wood_square.png"));
        quitButtonTexture = new Texture(Gdx.files.internal("quit_button.png"));
        dummyButtonTexture = new Texture(Gdx.files.internal("dummy_button.png")); // Dummy button texture
        customFont = new BitmapFont(Gdx.files.internal("myfont.fnt"));
        birdStartPosition = new Vector2(150, 100);
        birdVelocity = new Vector2(0, 0);
        gravity = new Vector2(0, -500);
        isDragging = false;
        birdLaunched = false;
        trajectoryPoints = new ArrayList<>();
        setupBackground();
        setupUI();
        setupSlingshotAndBirds();
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
                game.setGameScreen(MyGame.ScreenType.PAUSE);
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
        setupBirds(birdStartPosition.x + 50, birdStartPosition.y - 20, 40, 40, 3);
        bird = new Image(redBirdTexture);
        bird.setSize(40, 40);
        bird.setPosition(birdStartPosition.x - 30, birdStartPosition.y + 35);
        stage.addActor(bird);
        bird.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!birdLaunched) isDragging = true;
                return true;
            }
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (isDragging) {
                    bird.setPosition(Gdx.input.getX() - bird.getWidth() / 2, Gdx.graphics.getHeight() - Gdx.input.getY() - bird.getHeight() / 2);
                    calculateTrajectory();
                }
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (isDragging) {
                    isDragging = false;
                    birdLaunched = true;
                    birdVelocity.set(birdStartPosition.x - bird.getX(), birdStartPosition.y - bird.getY()).scl(5);
                }
            }
        });
    }

    private void setupBirds(float startX, float startY, float birdWidth, float birdHeight, int numberOfBirds) {
        Texture[] birdTextures = {
            new Texture("bird_1.png"),
            new Texture("bird_2.png"),
            new Texture("bird_3.png"),
            redBirdTexture
        };
        for (int i = 0; i < numberOfBirds; i++) {
            Image bird = new Image(birdTextures[i % birdTextures.length]);
            bird.setSize(birdWidth, birdHeight);
            bird.setPosition(startX - 105 - i * (birdWidth), startY - 10);
            stage.addActor(bird);
        }
    }

    private void calculateTrajectory() {
        trajectoryPoints.clear();
        Vector2 velocity = new Vector2(birdStartPosition.x - bird.getX(), birdStartPosition.y - bird.getY()).scl(5);
        Vector2 position = new Vector2(bird.getX(), bird.getY());
        for (int i = 0; i < 30; i++) {
            position.add(velocity.x * 0.1f, velocity.y * 0.1f);
            velocity.add(gravity.x * 0.1f, gravity.y * 0.1f);
            trajectoryPoints.add(new Vector2(position.x, position.y));
        }
    }

    private void setupStructures() {
        float baseX = 350, baseY = 70;
        Texture woodHoriz = new Texture(Gdx.files.internal("wood_horizontal1.png")), glassBox = new Texture(Gdx.files.internal("glass_box.png")), stoneRect = new Texture(Gdx.files.internal("stone_rectangle.png"));
        for (int i = 0; i < 4; i++) {
            Image woodBlock = new Image(woodHoriz);
            woodBlock.setSize(50, 20);
            woodBlock.setPosition(baseX + i * 50, baseY);
            stage.addActor(woodBlock);
        }
        for (int i = 0; i <= 3; i += 3) {
            Image woodColumn = new Image(woodHoriz);
            woodColumn.setSize(20, 80);
            woodColumn.setPosition(baseX + i * 60, baseY + 20);
            stage.addActor(woodColumn);
        }
        Image woodBlock = new Image(woodHoriz);
        woodBlock.setSize(205, 20);
        woodBlock.setPosition(baseX, baseY + 100);
        stage.addActor(woodBlock);
        for (int i = 0; i <= 2; i += 2) {
            Image woodColumn = new Image(woodHoriz);
            woodColumn.setSize(20, 80);
            woodColumn.setPosition(baseX + 38 + i * 50, baseY + 120);
            stage.addActor(woodColumn);
        }
        Image glassBlock = new Image(glassBox);
        glassBlock.setSize(120, 65);
        glassBlock.setPosition(baseX + 40, baseY + 200);
        stage.addActor(glassBlock);
        Image stoneBlock = new Image(stoneRect);
        stoneBlock.setSize(40, 40);
        stoneBlock.setPosition(baseX + 80, baseY + 265);
        stage.addActor(stoneBlock);
        Image pig1 = new Image(new Texture(Gdx.files.internal("pig_minister.png")));
        pig1.setSize(40, 40);
        pig1.setPosition(baseX + 79, baseY + 120);
        stage.addActor(pig1);
        Image pig2 = new Image(new Texture(Gdx.files.internal("pig_king.png")));
        pig2.setSize(40, 40);
        pig2.setPosition(baseX + 80, baseY + 210);
        stage.addActor(pig2);
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
        stage.act(delta);
        stage.draw();
        if (bird != null) {
            shapeRenderer.setProjectionMatrix(stage.getViewport().getCamera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.BLACK);
            for (Vector2 point : trajectoryPoints) shapeRenderer.circle(point.x, point.y, 2);
            shapeRenderer.end();
            if (birdLaunched && !isDragging) {
                birdVelocity.add(gravity.x * delta, gravity.y * delta);
                bird.setPosition(bird.getX() + birdVelocity.x * delta, bird.getY() + birdVelocity.y * delta);
                if (bird.getY() < 0 || bird.getX() < 0 || bird.getX() > Gdx.graphics.getWidth()) resetBird();
            }
        }
    }

    private void resetBird() {
        bird.setPosition(birdStartPosition.x - 30, birdStartPosition.y + 35);
        birdVelocity.set(0, 0);
        birdLaunched = false;
        trajectoryPoints.clear();
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
