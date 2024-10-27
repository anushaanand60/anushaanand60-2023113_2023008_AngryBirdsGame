package io.github.gdxgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.gdxgame.MyGame;

public class EndLevelScreen implements Screen {

    private SpriteBatch batch;
    private BitmapFont font;
    private MyGame game;
    private Texture redBanner, restartButtonImage, nextLevelButtonImage, backButtonImage, soundOnButtonImage, soundOffButtonImage, pauseBg;
    private int score;
    private float buttonWidth, buttonHeight;

    public EndLevelScreen(MyGame game, int score) {
        this.game = game;
        this.score = score;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("myfont.fnt"));
        redBanner = new Texture(Gdx.files.internal("red_banner.png"));
        restartButtonImage = new Texture(Gdx.files.internal("restart_button.png"));
        nextLevelButtonImage = new Texture(Gdx.files.internal("next_level_button.png"));
        backButtonImage = new Texture(Gdx.files.internal("back_button.png"));
        soundOnButtonImage = new Texture(Gdx.files.internal("sound_on.png"));
        soundOffButtonImage = new Texture(Gdx.files.internal("sound_off.png"));
        pauseBg = new Texture(Gdx.files.internal("pause_bg.png"));
        buttonWidth = nextLevelButtonImage.getWidth();
        buttonHeight = nextLevelButtonImage.getHeight();
        game.manageBackgroundMusic();
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
        float bannerHeight = redBanner.getHeight() * 0.4f;
        float bannerX = componentX - 30;
        float bannerY = componentY + componentHeight - bannerHeight / 2;
        batch.draw(redBanner, bannerX, bannerY, bannerWidth, bannerHeight);

        String bannerMessage;
        float messageX, messageY, scoreX, scoreY;
        font.getData().setScale(0.9f);
        font.setColor(Color.GOLD);

        if (score >= 100) {
            bannerMessage = "    Yay\nyou won!";
            float messageWidth = font.draw(batch, bannerMessage, 0, 0).width;
            messageX = bannerX + (bannerWidth - messageWidth) / 2;
            messageY = bannerY + bannerHeight / 1.125f;
            scoreX = bannerX + bannerWidth / 2 - 78;
            scoreY = bannerY + 15;
        } else {
            bannerMessage = "You\nlost!";
            messageX = bannerX + bannerWidth / 1.65f - (bannerMessage.length() * 7);
            messageY = bannerY + bannerHeight / 1.1f;
            scoreX = bannerX + bannerWidth / 2 - 60;
            scoreY = bannerY + 15;
        }

        font.draw(batch, bannerMessage, messageX, messageY);
        font.draw(batch, "Score: " + score, scoreX, scoreY);

        float buttonScale1 = 0.43f;
        float buttonScale2 = 0.60f;
        float buttonScale3 = 0.30f;
        float nextButtonWidth = nextLevelButtonImage.getWidth() * buttonScale1;
        float nextButtonHeight = nextLevelButtonImage.getHeight() * buttonScale1;
        float restartButtonWidth = restartButtonImage.getWidth() * buttonScale2;
        float restartButtonHeight = restartButtonImage.getHeight() * buttonScale2;
        float backButtonWidth = backButtonImage.getWidth() * buttonScale3;
        float backButtonHeight = backButtonImage.getHeight() * buttonScale3;

        float nextButtonX = bannerX + (bannerWidth / 2.4f) - (nextButtonWidth / 15.0f);
        float nextButtonY = bannerY - nextButtonHeight - 40;
        batch.draw(nextLevelButtonImage, nextButtonX, nextButtonY, nextButtonWidth, nextButtonHeight);

        float restartButtonX = nextButtonX;
        float restartButtonY = nextButtonY - restartButtonHeight - 30;
        batch.draw(restartButtonImage, restartButtonX, restartButtonY, restartButtonWidth, restartButtonHeight);

        float backButtonX = bannerX - (backButtonWidth / 30) + 90;
        float backButtonY = restartButtonY - backButtonHeight - 25;
        batch.draw(backButtonImage, backButtonX, backButtonY, backButtonWidth, backButtonHeight);

        float soundButtonWidth = soundOnButtonImage.getWidth() * 0.3f;
        float soundButtonHeight = soundOnButtonImage.getHeight() * 0.3f;
        float soundButtonX = 20;
        float soundButtonY = 20;
        if (game.isSoundOn()) {
            batch.draw(soundOnButtonImage, soundButtonX, soundButtonY, soundButtonWidth, soundButtonHeight);
        } else {
            batch.draw(soundOffButtonImage, soundButtonX, soundButtonY, soundButtonWidth, soundButtonHeight);
        }

        batch.end();

        if (Gdx.input.isTouched()) {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (isButtonTouched(x, y, nextButtonX, nextButtonY, nextButtonWidth, nextButtonHeight)) {}

            if (isButtonTouched(x, y, restartButtonX, restartButtonY, restartButtonWidth, restartButtonHeight)) {
                game.setGameScreen(MyGame.ScreenType.GAME1);
            }

            if (isButtonTouched(x, y, backButtonX, backButtonY, backButtonWidth, backButtonHeight)) {
                game.setGameScreen(MyGame.ScreenType.MAIN_MENU);
            }

            if (isButtonTouched(x, y, soundButtonX, soundButtonY, soundButtonWidth, soundButtonHeight)) {
                game.toggleSound();
            }
        }
    }

    private boolean isButtonTouched(int touchX, int touchY, float buttonX, float buttonY, float buttonWidth, float buttonHeight) {
        return touchX >= buttonX && touchX <= buttonX + buttonWidth && touchY >= buttonY && touchY <= buttonY + buttonHeight;
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
        font.dispose();
        redBanner.dispose();
        restartButtonImage.dispose();
        nextLevelButtonImage.dispose();
        backButtonImage.dispose();
        soundOnButtonImage.dispose();
        soundOffButtonImage.dispose();
        pauseBg.dispose();
    }
}
