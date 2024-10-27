README FILE (ANGRY BIRDS)
2023113- Anusha Anand
2023008-Aarehant Jain
Overview
This project is an Angry Birds game developed in Java using the LibGDX framework. The game applies object-oriented programming principles to manage game components such as screens, scores, and gameplay mechanics.

Features
HomeScreen
Displays the game logo.
Allows players to start or exit the game.
MainMenuScreen
Shows available levels with locked and unlocked states based on player progress.
Allows sound control and navigation to the home screen.
GameScreen1
The main gameplay screen, where players launch birds and interact with structures.
Displays the player’s score and offers sound control.
PauseScreen
Accessible from the gameplay screen to pause the game.
Allows players to resume, restart, or return to the main menu.
EndLevelScreen
Displays the player’s final score after a level.
Provides options to retry the level, proceed to the next level, or return to the main menu



Code Flow of the Angry Birds-Style Game
Game Initialization:
The game sets the initial screen to HomeScreen.
Starting Screen 
Flow:
Play Button: When clicked, MyGame is called to transition to the MainMenuScreen.
Exit Button: When clicked, it exits the game.
Main Menu :
Flow:
Level Buttons:
Players can select levels based on coins (representing progress or achievements).
Level 1 is always unlocked; other levels unlock when players earn enough coins.
On selecting Level 1,GameScreen1is called, transitioning to the GameScreen1.
Back Button:
Returns to HomeScreen
Gameplay Screen 
Flow:
Game Elements:
Slingshot and Birds: setupSlingshotAndBirds() arranges the slingshot and birds, initializing gameplay components.
Score Display: The player’s score is updated and displayed.
Controls:
Pause Button: Pauses the game ,transitioning to PauseScreen.
Sound Toggle: Changes sound status 
Quit Button: Ends the level and transitions to EndLevelScreen 
Gameplay Mechanic: This screen detects interactions with the slingshot and structures, updating the score based on player actions.



Pause Screen : 
Flow:
Resume Button: To resume gameplay.
Restart Button: Resets the level with the initial game state.
Main Menu Button: Switches to MainMenuScreen, allowing players to exit gameplay.
End of Level Screen:
Flow:
Final Score Display: Shows the score achieved in GameScreen1.
Retry Button: Resets the level 
Next Level Button: Checks for the next unlocked level and transitions accordingly.
Main Menu Button: Exits to MainMenuScreen, allowing players to select another level.




Screen Transition Flow:
HomeScreen ➔ MainMenuScreen ➔ GameScreen1
GameScreen1 ➔ PauseScreen 
GameScreen1 ➔ EndLevelScreen
Run the Game
In the Project panel, navigate to the core directory 
Locate the MyGame class in your main package (MyGame).
Select Run 'MyGame.main()'.
IntelliJ will compile the code, and a new game window will open, displaying the initial HomeScreen.



Gameplay Navigation
HomeScreen: Click Play to begin or Exit to close.
MainMenuScreen: Choose levels, toggle sound, or navigate back to the home screen.
GameScreen1: Engage with the gameplay and track your score.
PauseScreen: Pause and resume or exit to the main menu.
EndLevelScreen: Shows the score after a level, with options to replay or go to the main menu.






OOPS CONCEPTS IMPLEMENTED:

Encapsulation:
Each screen class (e.g., HomeScreen, MainMenuScreen) encapsulates its own UI elements, textures, and logic.
MyGame class controls sound and screen transitions, providing public methods like toggleSound() to manage sound state, while keeping other attributes private.
Inheritance:
All screens (HomeScreen, MainMenuScreen, etc.) implement the LibGDX Screen interface, inheriting the standard show(), render(), and dispose() methods.
MyGame extends the Game class from LibGDX to serve as the main controller.
Polymorphism:
MyGame uses polymorphism to manage and transition between screens, treating them generically as Screen instances.
Each screen can toggle sound through the sound button, regardless of its specific implementation.
Abstraction:
The Screen interface abstracts the setup and rendering of each screen.
MyGame provides a simple interface for screen transitions, handling complex game state and resource management internally.
Composition:
Each screen is composed of multiple LibGDX elements (e.g. Image, Label) to create complex interfaces.
GameScreen1 uses composition by including UI elements, the slingshot, birds, and structures.
Aggregation:
The MainMenuScreen aggregates multiple Texture instances for level buttons, which are displayed based on player progress. These textures are reusable assets, separate from the screen itself.

