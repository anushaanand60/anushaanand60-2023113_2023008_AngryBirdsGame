package io.github.gdxgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.audio.Music;
import io.github.gdxgame.MyGame;

public class PauseScreen implements Screen {
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture continueButton, restartButton, backButton, redBanner, pauseBg;
    private MyGame game;
    private Music backgroundMusic;
    private int currentLevel;

    public PauseScreen(MyGame game, int currentLevel) {
        this.game = game;
        this.currentLevel = currentLevel > 0 ? currentLevel : 1;
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("myfont.fnt"));
        continueButton = new Texture(Gdx.files.internal("continue_button.png"));
        restartButton = new Texture(Gdx.files.internal("restart_button.png"));
        backButton = new Texture(Gdx.files.internal("back_button.png"));
        redBanner = new Texture(Gdx.files.internal("red_banner.png"));
        pauseBg = new Texture(Gdx.files.internal("pause_bg.png"));
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(pauseBg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        float componentWidth = Gdx.graphics.getWidth() * 0.6f;
        float componentHeight = Gdx.graphics.getHeight() * 0.6f;
        float componentX = (Gdx.graphics.getWidth() - componentWidth) / 4;
        float componentY = (Gdx.graphics.getHeight() - componentHeight) / 2;

        batch.begin();
        float bannerWidth = redBanner.getWidth() * 0.22f;
        float bannerHeight = redBanner.getHeight() * 0.22f;
        float bannerX = componentX - 30;
        float bannerY = componentY + componentHeight - bannerHeight / 2;
        batch.draw(redBanner, bannerX, bannerY, bannerWidth, bannerHeight);
        font.draw(batch, "PAUSE", bannerX + bannerWidth / 2 - 55, bannerY + bannerHeight / 1.2f);

        float buttonScale1 = 0.46f, buttonScale2 = 0.63f, buttonScale3 = 0.34f;
        float continueButtonWidth = continueButton.getWidth() * buttonScale1, continueButtonHeight = continueButton.getHeight() * buttonScale1;
        float restartButtonWidth = restartButton.getWidth() * buttonScale2, restartButtonHeight = restartButton.getHeight() * buttonScale2;
        float backButtonWidth = backButton.getWidth() * buttonScale3, backButtonHeight = backButton.getHeight() * buttonScale3;

        float continueButtonX = bannerX + (bannerWidth / 2.4f) - (continueButtonWidth / 15.0f);
        float continueButtonY = bannerY - continueButtonHeight - 20;
        batch.draw(continueButton, continueButtonX, continueButtonY, continueButtonWidth, continueButtonHeight);

        float restartButtonX = continueButtonX;
        float restartButtonY = continueButtonY - restartButtonHeight - 30;
        batch.draw(restartButton, restartButtonX, restartButtonY, restartButtonWidth, restartButtonHeight);

        float backButtonX = bannerX - (backButtonWidth / 30) + 87;
        float backButtonY = continueButtonY - backButtonHeight - 100;
        batch.draw(backButton, backButtonX, backButtonY, backButtonWidth, backButtonHeight);
        batch.end();

        if (Gdx.input.isTouched()) {
            int x = Gdx.input.getX(), y = Gdx.graphics.getHeight() - Gdx.input.getY();
            if (isButtonTouched(x, y, continueButtonX, continueButtonY, continueButtonWidth, continueButtonHeight)) {
                continueGame();
            } else if (isButtonTouched(x, y, restartButtonX, restartButtonY, restartButtonWidth, restartButtonHeight)) {
                restartLevel();
            } else if (isButtonTouched(x, y, backButtonX, backButtonY, backButtonWidth, backButtonHeight)) {
                game.setGameScreen(MyGame.ScreenType.MAIN_MENU);
            }
        }
    }

    private boolean isButtonTouched(int touchX, int touchY, float buttonX, float buttonY, float buttonWidth, float buttonHeight) {
        return touchX >= buttonX && touchX <= buttonX + buttonWidth && touchY >= buttonY && touchY <= buttonY + buttonHeight;
    }

    private void continueGame() {
        switch (currentLevel) {
            case 1:
                game.setGameScreen(MyGame.ScreenType.GAME1, currentLevel, game.score);
                break;
            case 2:
                game.setGameScreen(MyGame.ScreenType.GAME2, currentLevel, game.score);
                break;
            case 3:
                game.setGameScreen(MyGame.ScreenType.GAME3, currentLevel, game.score);
                break;
            case 4:
                game.setGameScreen(MyGame.ScreenType.GAME4, currentLevel, game.score);
                break;
            default:
                game.setGameScreen(MyGame.ScreenType.MAIN_MENU);
                break;
        }
    }

    private void restartLevel() {
        continueGame();
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
        continueButton.dispose();
        restartButton.dispose();
        backButton.dispose();
        redBanner.dispose();
        pauseBg.dispose();
        font.dispose();
        backgroundMusic.dispose();
    }
}
