package io.github.gdxgame.screens;

import io.github.gdxgame.MyGame;

public class LoseScreen extends EndLevelScreen {

    public LoseScreen(MyGame game, int score, int currentLevel) {
        super(game, score, false, currentLevel);
    }

    @Override
    protected String getBannerMessage() {
        return "You\nLose!";
    }
}
