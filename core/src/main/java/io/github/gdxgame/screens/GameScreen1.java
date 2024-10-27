package io.github.gdxgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import io.github.gdxgame.MyGame;

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

    public GameScreen1(MyGame game) {
        this.game = game;
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
        float buttonScale = 0.45f;
        Image pauseButton = new Image(pauseButtonTexture);
        pauseButton.setSize(pauseButton.getWidth() * buttonScale, pauseButton.getHeight() * buttonScale);
        pauseButton.setPosition(10, Gdx.graphics.getHeight() - pauseButton.getHeight() - 10);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setGameScreen(MyGame.ScreenType.PAUSE);
            }
        });
        stage.addActor(pauseButton);

        float quitButtonWidth = quitButtonTexture.getWidth() * 0.05f;
        float quitButtonHeight = quitButtonTexture.getHeight() * 0.05f;
        Image quitButton = new Image(quitButtonTexture);
        quitButton.setSize(quitButtonWidth, quitButtonHeight);
        quitButton.setPosition(4.3f, pauseButton.getY() - quitButtonHeight - 10);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.showEndLevelScreen(score);
            }
        });
        stage.addActor(quitButton);

        float dummyButtonWidth = dummyButtonTexture.getWidth() * 0.35f;
        float dummyButtonHeight = dummyButtonTexture.getHeight() * 0.35f;
        Image dummyButton = new Image(dummyButtonTexture);
        dummyButton.setSize(dummyButtonWidth, dummyButtonHeight);
        dummyButton.setPosition(quitButton.getX() + 5, quitButton.getY() - dummyButtonHeight - 10);
        dummyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.showEndLevelScreen(100);  // Show win screen with a winning score
            }
        });
        stage.addActor(dummyButton);

        float soundButtonWidth = soundOnTexture.getWidth() * 0.3f;
        float soundButtonHeight = soundOnTexture.getHeight() * 0.3f;
        soundToggleButton = new Image(game.isSoundOn() ? soundOnTexture : soundOffTexture);
        soundToggleButton.setSize(soundButtonWidth, soundButtonHeight);
        soundToggleButton.setPosition(10, 10);
        soundToggleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.toggleSound();
                soundToggleButton.setDrawable(new Image(game.isSoundOn() ? soundOnTexture : soundOffTexture).getDrawable());
            }
        });
        stage.addActor(soundToggleButton);

        LabelStyle labelStyle = new LabelStyle(customFont, Color.GOLD);
        scoreLabel = new Label("Score\n" + score, labelStyle);
        scoreLabel.setPosition(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 85);
        stage.addActor(scoreLabel);
    }

    private void setupSlingshotAndBirds() {
        float slingshotScale = 0.19f;
        float slingshotWidth = slingshotTexture.getWidth() * slingshotScale;
        float slingshotHeight = slingshotTexture.getHeight() * slingshotScale;
        float slingshotX = 158;
        float slingshotY = 75;

        float loadedBirdScale = 0.13f;
        float loadedBirdWidth = redBirdTexture.getWidth() * loadedBirdScale;
        float loadedBirdHeight = redBirdTexture.getHeight() * loadedBirdScale;

        float scoreLabelX = scoreLabel.getX();
        float scoreLabelY = scoreLabel.getY();
        float loadedBirdX = scoreLabelX + (scoreLabel.getWidth() / 2) - (loadedBirdWidth) - 400;
        float loadedBirdY = scoreLabelY - loadedBirdHeight - 244;

        Image loadedBird = new Image(redBirdTexture);
        loadedBird.setSize(loadedBirdWidth, loadedBirdHeight);
        loadedBird.setPosition(loadedBirdX, loadedBirdY);
        stage.addActor(loadedBird);

        Image slingshot = new Image(slingshotTexture);
        slingshot.setSize(slingshotWidth, slingshotHeight);
        slingshot.setPosition(slingshotX, slingshotY);
        stage.addActor(slingshot);

        float birdScale = 0.13f;
        float birdWidth = redBirdTexture.getWidth() * birdScale;
        float birdHeight = redBirdTexture.getHeight() * birdScale;
        float birdY = slingshotY - 5;
        float firstBirdX = slingshotX - 40;

        setupBirds(firstBirdX, birdY, birdWidth, birdHeight, 3);
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
            bird.setPosition(startX - i * (birdWidth + 5), startY);
            stage.addActor(bird);
        }
    }

    private void setupStructures() {
        float baseX = 350;
        float baseY = 70;
        Texture woodHoriz = new Texture(Gdx.files.internal("wood_horizontal1.png"));
        Texture stoneRect = new Texture(Gdx.files.internal("stone_rectangle.png"));
        Texture glassHoriz = new Texture(Gdx.files.internal("glass_horizontal.png"));
        Texture glassBox = new Texture(Gdx.files.internal("glass_box.png"));

        Image stone1 = new Image(stoneRect);
        stone1.setPosition(baseX, baseY);
        stone1.setSize(120, 40);
        stage.addActor(stone1);

        Image stone2 = new Image(stoneRect);
        stone2.setPosition(baseX + 120, baseY);
        stone2.setSize(120, 40);
        stage.addActor(stone2);

        Image wood1 = new Image(woodHoriz);
        wood1.setPosition(baseX, baseY + 40);
        wood1.setSize(240, 20);
        stage.addActor(wood1);

        Image glass1 = new Image(glassHoriz);
        glass1.setPosition(baseX, baseY + 60);
        glass1.setSize(180, 20);
        stage.addActor(glass1);

        Image glassbox = new Image(glassBox);
        glassbox.setPosition(baseX + 180, baseY + 60);
        glassbox.setSize(60, 60);
        stage.addActor(glassbox);

        Texture pigMinister = new Texture(Gdx.files.internal("pig_minister.png"));
        Image pig1 = new Image(pigMinister);
        pig1.setPosition(baseX + 70, baseY + 75);
        pig1.setSize(40, 40);
        stage.addActor(pig1);

        Texture pigKing = new Texture(Gdx.files.internal("pig_king.png"));
        Image pig2 = new Image(pigKing);
        pig2.setPosition(baseX + 193, baseY + 71);
        pig2.setSize(30, 30);
        stage.addActor(pig2);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.8f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
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
        pigTexture.dispose();
        woodBlockTexture.dispose();
        quitButtonTexture.dispose();
        dummyButtonTexture.dispose();
        customFont.dispose();
    }
}
