package io.github.gdxgame.screens;

import io.github.gdxgame.MyGame;

public class WinScreen extends EndLevelScreen {

    public WinScreen(MyGame game, int score, int currentLevel) {
        super(game, score, true, currentLevel);
        if (game.getHighestUnlockedLevel() < currentLevel + 1) {
            game.setHighestUnlockedLevel(currentLevel + 1);
        }
    }

    @Override
    protected String getBannerMessage() {
        return "    Yay\nYou Win!";
    }
}
