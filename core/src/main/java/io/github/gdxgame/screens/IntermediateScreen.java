package io.github.gdxgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.gdxgame.MyGame;

public class IntermediateScreen implements Screen {
    private final MyGame game;
    private final Stage stage;
    private final SpriteBatch batch;

    private Texture backgroundTexture;
    private Texture backButtonTexture;
    private Label messageLabel;

    public IntermediateScreen(MyGame game, int currentLevel) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("blue_bg.jpg"));
        backButtonTexture = new Texture(Gdx.files.internal("back_button.png"));
        BitmapFont font = new BitmapFont(Gdx.files.internal("myfont.fnt"));
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.GOLD);

        String message = "You need to win Level " + currentLevel + " first!";
        messageLabel = new Label(message, labelStyle);
        messageLabel.setAlignment(Align.center);
        messageLabel.setFontScale(1f);

        Image backButton = new Image(backButtonTexture);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        Table table = new Table();
        table.setFillParent(true);
        table.add(messageLabel).expand().center().padBottom(20f);
        table.row();
        int backButtonWidth = backButtonTexture.getWidth();
        int backButtonHeight = backButtonTexture.getHeight();
        table.add(backButton).size(backButtonWidth * 0.30f, backButtonHeight * 0.30f).center().padBottom(50f);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        backgroundTexture.dispose();
        backButtonTexture.dispose();
    }

    @Override
    public void show() {}

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
}
