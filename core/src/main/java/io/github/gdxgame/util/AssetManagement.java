package io.github.gdxgame.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AssetManagement {
    private static final AssetManager assetManager = new AssetManager();

    // Preload assets (called during initialization)
    public static void loadAssets() {
        // Load background textures
        assetManager.load("bg1.jpg", Texture.class);
        assetManager.load("bg2.jpg", Texture.class);
        assetManager.load("bg3.jpg", Texture.class);
        assetManager.load("bg4.jpg", Texture.class);
        assetManager.load("blue_bg.jpg", Texture.class);
        assetManager.load("original_bg.jpg", Texture.class);
        assetManager.load("pause_bg.png", Texture.class);
        assetManager.load("new_bg.jpg", Texture.class);

        // Load bird textures
        assetManager.load("bird_1.png", Texture.class);
        assetManager.load("bird_2.png", Texture.class);
        assetManager.load("bird_3.png", Texture.class);
        assetManager.load("bird_4.png", Texture.class);

        // Load pig textures
        assetManager.load("pig_king.png", Texture.class);
        assetManager.load("pig_minister.png", Texture.class);
        assetManager.load("pig_queen.png", Texture.class);

        // Load block textures
        assetManager.load("wood_square.png", Texture.class);
        assetManager.load("wood_horizontal1.png", Texture.class);
        assetManager.load("wood_horizontal2.jpg", Texture.class);
        assetManager.load("wood_left_triangle.png", Texture.class);
        assetManager.load("wood_rectangle.png", Texture.class);
        assetManager.load("wood_triangle.png", Texture.class);
        assetManager.load("glass_box.png", Texture.class);
        assetManager.load("glass_circle.png", Texture.class);
        assetManager.load("glass_vertical1.png", Texture.class);
        assetManager.load("glass_vertical2.png", Texture.class);
        assetManager.load("stone_rectangle.png", Texture.class);
        assetManager.load("stone_circle.png", Texture.class);
        assetManager.load("stone_triangle.png", Texture.class);
        assetManager.load("TNT.png", Texture.class);

        // Load UI elements
        assetManager.load("back_button.png", Texture.class);
        assetManager.load("play_button.png", Texture.class);
        assetManager.load("restart_button.png", Texture.class);
        assetManager.load("save.png", Texture.class);
        assetManager.load("pause_button.png", Texture.class);
        assetManager.load("sound_on.png", Texture.class);
        assetManager.load("sound_off.png", Texture.class);
        assetManager.load("quit_button.png", Texture.class);
        assetManager.load("next_level_button.png", Texture.class);
        assetManager.load("continue_button.png", Texture.class);
        assetManager.load("locked_level.png", Texture.class);

        // Load level selection icons
        assetManager.load("level_1.png", Texture.class);
        assetManager.load("level_2.png", Texture.class);
        assetManager.load("level_3.png", Texture.class);
        assetManager.load("level_4.png", Texture.class);

        // Load miscellaneous assets
        assetManager.load("slingshot.png", Texture.class);
        assetManager.load("red_banner.png", Texture.class);
        assetManager.load("angry_birds_logo.png", Texture.class);
        assetManager.load("dummy_button.png", Texture.class);
        assetManager.load("reset.png", Texture.class);
        assetManager.load("loading_bar.png", Texture.class);

        // Load screenshots for transitions
        assetManager.load("GameScreenSS.jpg", Texture.class);
        assetManager.load("HomeScreenSS.jpg", Texture.class);
        assetManager.load("MainMenuSS.jpg", Texture.class);
        assetManager.load("PauseMenuSS.jpg", Texture.class);
        assetManager.load("EndLevelWinSS.jpg", Texture.class);
        assetManager.load("EndLevelLoseSS.jpg", Texture.class);

        // Load font
        assetManager.load("myfont.fnt", Texture.class);

        // Load music and sounds
        assetManager.load("background_music.mp3", Music.class);
    }

    // Dispose of assets when no longer needed
    public static void dispose() {
        assetManager.dispose();
    }

    // Check if assets are loaded
    public static boolean update() {
        return assetManager.update(); // Returns true when all assets are loaded
    }

    // Get assets
    public static String getTexture(String fileName) {
        return assetManager.get(fileName, Texture.class);
    }

    public static Music getMusic(String fileName) {
        return assetManager.get(fileName, Music.class);
    }

    public static Sound getSound(String fileName) {
        return assetManager.get(fileName, Sound.class);
    }
}
