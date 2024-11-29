package io.github.gdxgame.util;

import io.github.gdxgame.game.GameState;

import java.io.*;

public class Save {

    private static final String SAVE_FILE = "game_state.ser";

    // Save the game state to a file
    public static void saveGame(GameState gameState) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(gameState);
            System.out.println("Game saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }

    // Load the game state from a file
    public static GameState loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            System.out.println("Game loaded successfully!");
            return (GameState) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading game: " + e.getMessage());
            return null; // Return null if loading fails
        }
    }

    // Check if a saved game exists
    public static boolean isSaveFileAvailable() {
        File saveFile = new File(SAVE_FILE);
        return saveFile.exists();
    }

    // Delete the save file (for reset functionality)
    public static void deleteSaveFile() {
        File saveFile = new File(SAVE_FILE);
        if (saveFile.exists() && saveFile.delete()) {
            System.out.println("Save file deleted successfully.");
        } else {
            System.err.println("No save file to delete.");
        }
    }
}
