package io.github.gdxgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import io.github.gdxgame.MyGame;

public class MainMenuScreen implements Screen {
    private SpriteBatch batch;
    private Texture background, soundOnButton, soundOffButton, level1, level2, level3, level4, lockedLevel, backButton;
    private BitmapFont font;
    private MyGame game;

    public MainMenuScreen(MyGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("myfont.fnt"));
        font.setColor(Color.WHITE);
        font.getData().setScale(1.2f);
        background = new Texture(Gdx.files.internal("original_bg.jpg"));
        soundOnButton = new Texture(Gdx.files.internal("sound_on.png"));
        soundOffButton = new Texture(Gdx.files.internal("sound_off.png"));
        level1 = new Texture(Gdx.files.internal("level_1.png"));
        level2 = new Texture(Gdx.files.internal("level_2.png"));
        level3 = new Texture(Gdx.files.internal("level_3.png"));
        level4 = new Texture(Gdx.files.internal("level_4.png"));
        lockedLevel = new Texture(Gdx.files.internal("locked_level.png"));
        backButton = new Texture(Gdx.files.internal("back_button.png"));
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float soundButtonWidth = soundOnButton.getWidth() * 0.3f, soundButtonHeight = soundOnButton.getHeight() * 0.3f;
        float soundButtonX = Gdx.graphics.getWidth() - soundButtonWidth - 10, soundButtonY = 10;
        batch.draw(game.isSoundOn() ? soundOnButton : soundOffButton, soundButtonX, soundButtonY, soundButtonWidth, soundButtonHeight);

        drawLevels();

        float backButtonWidth = backButton.getWidth() * 0.18f, backButtonHeight = backButton.getHeight() * 0.18f;
        float backButtonX = 3, backButtonY = 10;
        batch.draw(backButton, backButtonX, backButtonY, backButtonWidth, backButtonHeight);

        batch.end();

        if (Gdx.input.isTouched()) {
            int x = Gdx.input.getX(), y = Gdx.graphics.getHeight() - Gdx.input.getY();
            if (x >= soundButtonX && x <= soundButtonX + soundButtonWidth && y >= soundButtonY && y <= soundButtonY + soundButtonHeight) {
                game.toggleSound();
            }
            if (x >= backButtonX && x <= backButtonX + backButtonWidth && y >= backButtonY && y <= backButtonY + backButtonHeight) {
                game.setScreen(new HomeScreen(game));
            }
            handleLevelClicks(x, y);
        }
    }

    private void drawLevels() {
        int highestUnlockedLevel = game.getHighestUnlockedLevel();
        float levelIconSize = 128;
        float totalWidth = (levelIconSize * 2) + 20;
        float startX = (Gdx.graphics.getWidth() - totalWidth) / 2;
        float startY = (Gdx.graphics.getHeight() / 3) + (levelIconSize / 1.5f);
        float colSpacing = levelIconSize + 20;
        float rowSpacing = levelIconSize + 20;

        batch.draw(level1, startX, startY, levelIconSize, levelIconSize);
        if (highestUnlockedLevel >= 2) {
            batch.draw(level2, startX + colSpacing, startY, levelIconSize, levelIconSize);
        } else {
            batch.draw(level2, startX + colSpacing, startY, levelIconSize, levelIconSize);
            batch.draw(lockedLevel, startX + colSpacing, startY + 10, levelIconSize, levelIconSize);
        }

        if (highestUnlockedLevel >= 3) {
            batch.draw(level3, startX, startY - rowSpacing, levelIconSize, levelIconSize);
        } else {
            batch.draw(level3, startX, startY - rowSpacing, levelIconSize, levelIconSize);
            batch.draw(lockedLevel, startX, startY - rowSpacing + 10, levelIconSize, levelIconSize);
        }

        if (highestUnlockedLevel >= 4) {
            batch.draw(level4, startX + colSpacing, startY - rowSpacing, levelIconSize, levelIconSize);
        } else {
            batch.draw(level4, startX + colSpacing, startY - rowSpacing, levelIconSize, levelIconSize);
            batch.draw(lockedLevel, startX + colSpacing, startY - rowSpacing + 10, levelIconSize, levelIconSize);
        }
    }

    private void handleLevelClicks(int x, int y) {
        int highestUnlockedLevel = game.getHighestUnlockedLevel();
        float levelIconSize = 128, totalWidth = (levelIconSize * 2) + 20;
        float startX = (Gdx.graphics.getWidth() - totalWidth) / 2, startY = (Gdx.graphics.getHeight() / 3) + (levelIconSize / 1.5f);
        float colSpacing = levelIconSize + 20, rowSpacing = levelIconSize + 20;
        if (x >= startX && x <= startX + levelIconSize && y >= startY && y <= startY + levelIconSize) {
            game.setScreen(new GameScreen1(game));
        }
        if (highestUnlockedLevel >= 2 && x >= startX + colSpacing && x <= startX + colSpacing + levelIconSize && y >= startY && y <= startY + levelIconSize) {
            game.setScreen(new GameScreen2(game));
        }
        if (highestUnlockedLevel >= 3 && x >= startX && x <= startX + levelIconSize && y >= startY - rowSpacing && y <= startY - rowSpacing + levelIconSize) {
            game.setScreen(new GameScreen3(game));
        }
        if (highestUnlockedLevel >= 4 && x >= startX + colSpacing && x <= startX + colSpacing + levelIconSize && y >= startY - rowSpacing && y <= startY - rowSpacing + levelIconSize) {
            game.setScreen(new GameScreen4(game));
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
        soundOnButton.dispose();
        soundOffButton.dispose();
        level1.dispose();
        level2.dispose();
        level3.dispose();
        level4.dispose();
        lockedLevel.dispose();
        backButton.dispose();
        font.dispose();
    }
}
