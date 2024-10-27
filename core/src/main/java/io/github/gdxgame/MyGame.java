package io.github.gdxgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.gdxgame.screens.*;

public class MyGame extends Game {
    public enum ScreenType { HOME, MAIN_MENU, GAME1, PAUSE, END_LEVEL }
    private Music backgroundMusic;
    public SpriteBatch batch;
    public int coins;
    private boolean soundOn;

    @Override
    public void create() {
        batch = new SpriteBatch();
        coins = 0;
        soundOn = true;
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));
        backgroundMusic.setLooping(true);
        if (soundOn) backgroundMusic.play();
        setGameScreen(ScreenType.HOME);
    }

    public void setGameScreen(ScreenType screenType) {
        switch (screenType) {
            case HOME: setScreen(new HomeScreen(this)); break;
            case MAIN_MENU: setScreen(new MainMenuScreen(this, coins)); break;
            case GAME1: setScreen(new GameScreen1(this)); break;
            case PAUSE: setScreen(new PauseScreen(this)); break;
            case END_LEVEL: setScreen(new EndLevelScreen(this, 0)); break;
            default: throw new IllegalArgumentException("Unknown screen type: " + screenType);
        }
        manageBackgroundMusic();
    }

    public void showEndLevelScreen(int score) {
        setScreen(new EndLevelScreen(this, score));
        manageBackgroundMusic();
    }

    public boolean isSoundOn() {
        return soundOn;
    }

    public void toggleSound() {
        soundOn = !soundOn;
        manageBackgroundMusic();
    }

    public void manageBackgroundMusic() {
        if (soundOn) backgroundMusic.play();
        else backgroundMusic.pause();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        if (getScreen() != null) getScreen().dispose();
        backgroundMusic.dispose();
    }
}
