package application;

import application.game.Game;
import application.game.GameView;
import application.menu.MenuView;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Main is the main class of JavaFX used to launch graphic java application.
 *
 * @author Mateusz Borowiecki
 */
public class Main extends Application {

    private double WIDTH;
    private double HEIGHT;
    public static Game game;
    public static MenuView menuView;
    public static GameView gameView;

    /**
     * JavaFX start method required to launch JavaFX application.
     * Sets stage for application and shows it's menu.
     *
     * @param stage JavaFX Stage class object
     * @throws Exception Standard JavaFX start method exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        setStage(stage);
        menuView = new MenuView(stage, WIDTH, HEIGHT);
        menuView.show();
    }

    /**
     * Adjusts JavaFX stage's width and height to a maximum size of a screen.
     * Sets stage as maximized and removes unnecessary utility icons from application's window.
     *
     * @param stage JavaFX Stage class object
     */
    private void setStage(Stage stage) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        WIDTH = primaryScreenBounds.getWidth();
        HEIGHT = primaryScreenBounds.getHeight();
        stage.setMaximized(true);
    }

    /**
     * Method required to launch JavaFX application
     *
     * @param args Arguments used to launch JavaFX applications
     */
    public static void main(String[] args) {
        launch(args);
    }
}
