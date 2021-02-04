package application.menu;

import application.Main;
import application.game.Game;
import application.game.GameView;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;

import static application.Main.game;

/**
 * This class is used to display view of the menu in our application.
 */
public class MenuView {

    private Stage mainStage;
    private double width;
    private double height;

    private Pane menuPane;
    private Scene menuScene;

    private SettingsView settingsView;

    private VBox menuButtons;

    private ArrayList<OptionSubScene> subScenes;

    private int enemyShipsAmount;

    /**
     * Constructor for MenuView class creating and initializing game menu
     * with it's buttons on the application stage.
     *
     * @param stage  stage on which the menu view is displayed
     * @param width  width of menuView stage
     * @param height height of menuView stage.
     */
    public MenuView(Stage stage, double width, double height) {
        this.mainStage = stage;
        this.width = width;
        this.height = height;

        menuButtons = new VBox();
        menuButtons.setSpacing(30);
        menuButtons.setLayoutX(width / 2 - 95);
        menuButtons.setLayoutY(height / 2 - 200);

        menuPane = new Pane();
        menuScene = new Scene(menuPane, width, height);

        createButtons();
        createBackground();

        subScenes = new ArrayList<>();
        createSubScenes();
    }

    /**
     * Creates object of OptionSubScene class which will
     * be displayed after choosing corresponding option in game menu
     *
     * @param option type of sub scene which will be created based on it's type in Options Enum class.
     */
    private void createOptionSubScene(Options option) {
        OptionSubScene scene = new OptionSubScene(option, width - 100, height - 100);
        scene.setLayoutX(50);
        scene.setLayoutY(50);
        subScenes.add(scene);
        menuPane.getChildren().add(scene);
    }


    /**
     * Creates sub scenes for menu buttons except the "PLAY" and "EXIT" button.
     */
    private void createSubScenes() {
        createOptionSubScene(Options.SETTINGS);
        settingsView = new SettingsView();
        subScenes.get(0).addContentPane(settingsView);

        createOptionSubScene(Options.HELP);
        createOptionSubScene(Options.CREDITS);
    }

    /**
     * Creates menu background and adds it to main pane of menu view
     */
    private void createBackground() {
        Image backgroundImage = new Image(MenuModels.WATER_TILE.getUrl(), 64, 64, false, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        menuPane.setBackground(new Background(background));
    }

    /**
     * Initializes and creates menu buttons displayed on menu scene.
     */
    private void createButtons() {
        createPlayButton();
        createSettingsButton();
        createHelpButton();
        createCreditsButton();
        createExitButton();
        menuPane.getChildren().add(menuButtons);
        menuButtons.setAlignment(Pos.CENTER);
    }

    /**
     * Initializes and creates "PLAY" menu button used to start a new game.
     * On press the menu scene will get replaced with game scene.
     */
    private void createPlayButton() {
        MenuButton playButton = new MenuButton(Options.PLAY.getTitle());
        addMenuButton(playButton);
        playButton.setOnAction(e -> {
            Main.gameView = new GameView(mainStage, width, height);
            Main.gameView.show();

            enemyShipsAmount = settingsView.getShipsAmount();
            game = new Game(Main.gameView, enemyShipsAmount);
            game.setup();
            game.start();
        });
    }

    /**
     * Initializes and creates "SETTINGS" menu button used to change settings of game.
     * On press the settings sub scene will be displayed to let user decide amount of enemy ships in game.
     */
    private void createSettingsButton() {
        MenuButton settingsButton = new MenuButton(Options.SETTINGS.getTitle());
        addMenuButton(settingsButton);
        settingsButton.setOnAction(e -> {
            subScenes.get(0).setVisible(true);
        });
    }

    /**
     * Initializes and creates "HELP" menu button used to prompt user about the game.
     * On press the help sub scene will be displayed along with text explaining rules of the game .
     */
    private void createHelpButton() {
        MenuButton helpButton = new MenuButton(Options.HELP.getTitle());
        addMenuButton(helpButton);
        helpButton.setOnAction(e -> {
            subScenes.get(1).setVisible(true);
        });
    }

    /**
     * Initializes and creates "CREDITS" menu button used show author's of game name.
     * On press the credits sub scene with a name of the author will be displayed.
     */
    private void createCreditsButton() {
        MenuButton creditsButton = new MenuButton(Options.CREDITS.getTitle());
        addMenuButton(creditsButton);
        creditsButton.setOnAction(e -> {
            subScenes.get(2).setVisible(true);
        });
    }

    /**
     * Initializes and creates "EXIT" menu button used to close application from menu view.
     * On press the JavaFX application will end it's execution.
     */
    private void createExitButton() {
        MenuButton exitButton = new MenuButton(Options.EXIT.getTitle());
        addMenuButton(exitButton);
        exitButton.setOnAction(e -> {
            mainStage.close();
        });
    }

    /**
     * This method adds specific menu button to a menuButtons Pane in MenuView class.
     *
     * @param button menu button to display on menu view
     */
    private void addMenuButton(MenuButton button) {
        menuButtons.getChildren().add(button);
    }

    /**
     * Getter for amount of enemy ships, chosen in game menu, which player will have to play against.
     *
     * @return amount of enemy ships wanted in game - limited from 1 to 5 including.
     */
    public int getEnemyShipsAmount() {
        return enemyShipsAmount;
    }

    /**
     * Calling this method will change main stage's scene
     * of application to a menu scene contained in this class
     */
    public void show() {
        mainStage.setScene(menuScene);
        mainStage.show();
    }
}
