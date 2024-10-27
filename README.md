# Angry Birds Game Project

## Contributors
- **2023113 - Anusha Anand**
- **2023008 - Aarehant Jain**

## Overview
This project is an Angry Birds-style game developed in Java using the LibGDX framework. The game incorporates object-oriented programming principles to manage game components, including screens, scores, and gameplay mechanics.

---

## Features and Game Screenshots

### 1. HomeScreen
- Displays the game logo.
- Allows players to start or exit the game.

### 2. MainMenuScreen
- Shows available levels with locked and unlocked states based on player progress.
- Allows sound control and navigation to the home screen.

### 3. GameScreen1
- Main gameplay screen, where players launch birds and interact with structures.
- Displays the player’s score and offers sound control.

### 4. PauseScreen
- Accessible from the gameplay screen to pause the game.
- Allows players to resume, restart, or return to the main menu.

### 5. EndLevelScreen
- Displays the player’s final score after a level.
- Provides options to retry the level, proceed to the next level, or return to the main menu.

---

## Code Flow of the Game

### 1. Game Initialization
- The game sets the initial screen to `HomeScreen`.

### 2. Starting Screen Flow
- **Play Button**: Transitions to `MainMenuScreen` when clicked.
- **Exit Button**: Exits the game when clicked.

### 3. Main Menu Flow
- **Level Buttons**:
  - Players can select levels based on coins (representing progress or achievements).
  - Level 1 is always unlocked; other levels unlock as players earn coins.
  - Selecting Level 1 transitions to `GameScreen1`.
- **Back Button**: Returns to `HomeScreen`.

### 4. Gameplay Screen Flow
- **Game Elements**:
  - **Slingshot and Birds**: `setupSlingshotAndBirds()` initializes gameplay components.
  - **Score Display**: Updates and displays the player’s score.
- **Controls**:
  - **Pause Button**: Pauses the game and transitions to `PauseScreen`.
  - **Sound Toggle**: Enables or disables sound.
  - **Quit Button**: Ends the level and transitions to `EndLevelScreen`.
  - **Gameplay Mechanic**: Detects interactions with the slingshot and structures, updating score based on player actions.

### 5. Pause Screen Flow
- **Resume Button**: Resumes gameplay.
- **Restart Button**: Resets the level to its initial state.
- **Main Menu Button**: Switches to `MainMenuScreen`, allowing players to exit gameplay.

### 6. End of Level Screen Flow
- **Final Score Display**: Shows the score achieved in `GameScreen1`.
- **Retry Button**: Resets the level.
- **Next Level Button**: Checks for the next unlocked level and transitions accordingly.
- **Main Menu Button**: Exits to `MainMenuScreen` to allow level selection.

---

## Screen Transition Flow
- **HomeScreen ➔ MainMenuScreen ➔ GameScreen1**
- **GameScreen1 ➔ PauseScreen**
- **GameScreen1 ➔ EndLevelScreen**

---

## Running the Game
1. In the Project panel, navigate to the `core` directory.
2. Locate the `MyGame` class in your main package.
3. Select `Run 'MyGame.main()'`.
4. IntelliJ will compile the code, and a new game window will open, displaying the initial `HomeScreen`.

---

## Gameplay Navigation
- **HomeScreen**: Click Play to start or Exit to close.
- **MainMenuScreen**: Choose levels, toggle sound, or navigate back to the home screen.
- **GameScreen1**: Engage in gameplay and track your score.
- **PauseScreen**: Pause, resume, or exit to the main menu.
- **EndLevelScreen**: Shows the score after a level with options to replay or go to the main menu.

---

## OOP Concepts Implemented

### 1. Encapsulation
- Each screen class (e.g., `HomeScreen`, `MainMenuScreen`) encapsulates its own UI elements, textures, and logic.
- `MyGame` class manages sound and screen transitions, providing public methods like `toggleSound()` to manage sound state, while keeping other attributes private.

### 2. Inheritance
- All screens (`HomeScreen`, `MainMenuScreen`, etc.) implement the LibGDX `Screen` interface, inheriting methods such as `show()`, `render()`, and `dispose()`.
- `MyGame` extends the `Game` class from LibGDX to serve as the main controller.

### 3. Polymorphism
- `MyGame` uses polymorphism to manage and transition between screens, treating them generically as `Screen` instances.
- Each screen can toggle sound through a sound button, regardless of its specific implementation.

### 4. Abstraction
- The `Screen` interface abstracts the setup and rendering of each screen.
- `MyGame` provides a simple interface for screen transitions, managing complex game state and resource management internally.

### 5. Composition
- Each screen is composed of multiple LibGDX elements (e.g., `Image`, `Label`) to create complex interfaces.
- `GameScreen1` uses composition to include UI elements, the slingshot, birds, and structures.

### 6. Aggregation
- `MainMenuScreen` aggregates multiple `Texture` instances for level buttons, displayed based on player progress.

---
