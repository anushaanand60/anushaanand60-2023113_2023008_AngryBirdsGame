package io.github.gdxgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.gdxgame.screens.*;

public class MyGame extends Game {
    public enum ScreenType {
        HOME,
        MAIN_MENU,
        GAME1,
        GAME2,
        GAME3,
        GAME4,
        PAUSE,
        END_LEVEL
    }
    private Music backgroundMusic;
    public SpriteBatch batch;
    public int score;
    private boolean soundOn;
    private int highestUnlockedLevel = 1;

    @Override
    public void create() {
        batch = new SpriteBatch();
        score = 0;
        soundOn = true;
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));
        backgroundMusic.setLooping(true);
        if (soundOn) backgroundMusic.play();
        setGameScreen(ScreenType.HOME);
    }

    public void setGameScreen(ScreenType screenType) {
        setGameScreen(screenType, 0, 0);
    }

    public void setGameScreen(ScreenType screenType, int currentLevel, int score) {
        switch (screenType) {
            case HOME:
                setScreen(new HomeScreen(this));
                break;
            case MAIN_MENU:
                setScreen(new MainMenuScreen(this));
                break;
            case GAME1:
                setScreen(new GameScreen1(this));
                break;
            case GAME2:
                setScreen(new GameScreen2(this));
                break;
            case GAME3:
                setScreen(new GameScreen3(this));
                break;
            case GAME4:
                setScreen(new GameScreen4(this));
                break;
            case PAUSE:
                setScreen(new PauseScreen(this, currentLevel));
                break;
            case END_LEVEL:
                throw new IllegalArgumentException("Use showEndLevelScreen(int score) to set END_LEVEL screen.");
            default:
                throw new IllegalArgumentException("Unknown screen type: " + screenType);
        }
        manageBackgroundMusic();
    }

    public int getHighestUnlockedLevel() {
        return highestUnlockedLevel;
    }

    public void setHighestUnlockedLevel(int level) {
        this.highestUnlockedLevel = level;
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
