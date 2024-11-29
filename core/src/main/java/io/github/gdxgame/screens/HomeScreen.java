package io.github.gdxgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.gdxgame.MyGame;

public class HomeScreen implements Screen {
    private SpriteBatch batch;
    private Texture background, logo, playButton, exitButton, homeImage, soundOnButton, soundOffButton;
    private MyGame game;

    public HomeScreen(MyGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("original_bg.jpg"));
        logo = new Texture(Gdx.files.internal("angry_birds_logo.png"));
        playButton = new Texture(Gdx.files.internal("play_button.png"));
        exitButton = new Texture(Gdx.files.internal("exit_button.png"));
        homeImage = new Texture(Gdx.files.internal("photo_on_home.png"));
        soundOnButton = new Texture(Gdx.files.internal("sound_on.png"));
        soundOffButton = new Texture(Gdx.files.internal("sound_off.png"));
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float logoWidth = logo.getWidth() / 5, logoHeight = logo.getHeight() / 5;
        float logoX = (Gdx.graphics.getWidth() - logoWidth) / 2, logoY = Gdx.graphics.getHeight() - logoHeight - 20;
        batch.draw(logo, logoX, logoY, logoWidth, logoHeight);

        float homeImageWidth = homeImage.getWidth() / 1.75f, homeImageHeight = homeImage.getHeight() / 1.75f;
        float homeImageX = (Gdx.graphics.getWidth() - homeImageWidth) / 2, homeImageY = (Gdx.graphics.getHeight() - homeImageHeight) / 2;
        batch.draw(homeImage, homeImageX, homeImageY, homeImageWidth, homeImageHeight);

        float buttonScale = 0.4f;
        float playButtonWidth = playButton.getWidth() * buttonScale, playButtonHeight = playButton.getHeight() * buttonScale;
        float playButtonX = (Gdx.graphics.getWidth() - playButtonWidth) / 2, playButtonY = homeImageY - playButtonHeight + 40;
        batch.draw(playButton, playButtonX, playButtonY, playButtonWidth, playButtonHeight);

        float exitButtonScale = 0.22f;
        float exitButtonWidth = exitButton.getWidth() * exitButtonScale, exitButtonHeight = exitButton.getHeight() * exitButtonScale;
        float exitButtonX = (Gdx.graphics.getWidth() - exitButtonWidth) / 2, exitButtonY = playButtonY - exitButtonHeight + 40;
        batch.draw(exitButton, exitButtonX, exitButtonY, exitButtonWidth, exitButtonHeight);

        float soundButtonWidth = soundOnButton.getWidth() * 0.3f, soundButtonHeight = soundOnButton.getHeight() * 0.3f;
        float soundButtonX = Gdx.graphics.getWidth() - soundButtonWidth - 10, soundButtonY = 10;
        Texture soundButtonTexture = game.isSoundOn() ? soundOnButton : soundOffButton;
        batch.draw(soundButtonTexture, soundButtonX, soundButtonY, soundButtonWidth, soundButtonHeight);

        batch.end();

        if (Gdx.input.isTouched()) {
            int x = Gdx.input.getX(), y = Gdx.graphics.getHeight() - Gdx.input.getY();
            if (x >= playButtonX && x <= playButtonX + playButtonWidth && y >= playButtonY && y <= playButtonY + playButtonHeight) {

                game.setScreen(new MainMenuScreen(game));
            }
            if (x >= exitButtonX && x <= exitButtonX + exitButtonWidth && y >= exitButtonY && y <= exitButtonY + exitButtonHeight) {
                Gdx.app.exit();
            }
            if (x >= soundButtonX && x <= soundButtonX + soundButtonWidth && y >= soundButtonY && y <= soundButtonY + soundButtonHeight) {
                game.toggleSound();
            }
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        logo.dispose();
        playButton.dispose();
        exitButton.dispose();
        homeImage.dispose();
        soundOnButton.dispose();
        soundOffButton.dispose();
    }
}
