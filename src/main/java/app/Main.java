package app;

import controller.GameController;
import javafx.application.Application;
import javafx.stage.Stage;
import utils.SoundManager;

/**
 * Main application class responsible for starting the game.
 */
public class Main extends Application {

    /**
     * Starts the primary stage of the application.
     * Sets up the window title, initializes the GameController, plays BGM, and shows the stage.
     *
     * @param primaryStage The primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("The Last Withdraw");
        primaryStage.setResizable(false);
        GameController game = new GameController(primaryStage);
        SoundManager.playBGM("halloween-retro-gaming-music-no-copyright-401542.mp3", 0.2);
        game.startGame();
        primaryStage.show();
    }

    /**
     * Main method to launch the JavaFX application.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}