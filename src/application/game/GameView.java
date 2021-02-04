package application.game;

import application.menu.*;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import static application.Main.game;
import static application.Main.menuView;

/**
 * This class is used to display view of the game in our application.
 */
public class GameView {

    private Stage mainStage;

    private double width;
    private double height;

    private Pane gamePane;
    private Scene gameScene;

    private boolean inGameMenuShowed = false;
    private VBox pauseView;
    private VBox leaveWarningView;
    private VBox gameOverView;

    /**
     * Constructor for GameView class creating and initializing game menu
     * with it's buttons on the application stage.
     *
     * @param stage  stage on which the game view is displayed
     * @param width  width of gameView stage
     * @param height height of gameView stage.
     */
    public GameView(Stage stage, double width, double height) {
        this.mainStage = stage;
        this.width = width;
        this.height = height;
        initializeStage();
    }

    /**
     * This method initializes stage of game view and creates all of it's elements:
     * background, buttons, pause view and leave to menu warning.
     */
    private void initializeStage() {
        gamePane = new Pane();
        gameScene = new Scene(gamePane, width, height);
        createBackground();
        createTopButtons();
        createPauseView();
        createLeaveWarning();
    }

    /**
     * This method creates a background for a game scene.
     */
    private void createBackground() {
        Image backgroundImage = new Image(GameModels.WATER_TILE.getUrl(),
                64, 64, false, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        gamePane.setBackground(new Background(background));
    }

    /**
     * This method creates functional in-game buttons allowing to pause the game or leave to menu view.
     */
    private void createTopButtons() {
        SmallMenuButton goMenuButton = new SmallMenuButton("☰");
        goMenuButton.setFocusTraversable(false);
        goMenuButton.setOnAction(e -> {
            if (!inGameMenuShowed) {
                if (isOnGamePane(pauseView)) removeNode(pauseView);
                game.pause();
                if (!isOnGamePane(leaveWarningView)) addNode(leaveWarningView);
                inGameMenuShowed = true;
            }
        });

        SmallMenuButton pauseButton = new SmallMenuButton("⏸");
        pauseButton.setFocusTraversable(false);
        pauseButton.setLayoutX(50);
        pauseButton.setOnAction(e -> {
            if (game.isRunning()) {
                if (!inGameMenuShowed) {
                    game.pause();
                    if (!isOnGamePane(pauseView)) addNode(pauseView);
                    pauseButton.setText("➤");
                }
            } else {
                if (!inGameMenuShowed) {
                    game.unpause();
                    if (isOnGamePane(pauseView)) removeNode(pauseView);
                    pauseButton.setText("⏸");
                }
            }
        });
        gamePane.getChildren().addAll(goMenuButton, pauseButton);
    }

    /**
     * This method is used to show "PAUSED" label in the middle of the game scene
     * when the game is paused.
     */
    private void createPauseView() {
        pauseView = new VBox();
        pauseView.setFocusTraversable(false);
        pauseView.setLayoutX(width / 2 - 125);
        pauseView.setLayoutY(height / 2 - 50);
        Label pausedLabel = new Label("PAUSED");
        pausedLabel.setTextAlignment(TextAlignment.CENTER);
        Font font = Font.loadFont(getClass().getResourceAsStream(MenuModels.FONT.getUrl()), 50);
        pausedLabel.setFont(font);
        pauseView.getChildren().add(pausedLabel);
    }

    /**
     * This method is used to show warning in the middle of the game scene
     * about leaving to menu and losing progress along with 2 buttons allowing user to choose.
     */
    private void createLeaveWarning() {
        leaveWarningView = new VBox();
        leaveWarningView.setFocusTraversable(false);
        leaveWarningView.setLayoutX(width / 2 - 325);
        leaveWarningView.setLayoutY(height / 2 - 150);
        leaveWarningView.setAlignment(Pos.CENTER);
        leaveWarningView.setSpacing(30);

        Label leaveLabel = new Label("Quit to menu?\nYour progress won't be saved.");
        leaveLabel.setTextAlignment(TextAlignment.CENTER);
        Font font = Font.loadFont(getClass().getResourceAsStream(MenuModels.FONT.getUrl()), 30);
        leaveLabel.setFont(font);

        MenuButton yesButton = new MenuButton("YES");
        yesButton.setOnAction(e -> {
            menuView.show();
        });

        MenuButton noButton = new MenuButton("NO");
        noButton.setOnAction(e -> {
            game.unpause();
            if (isOnGamePane(leaveWarningView)) removeNode(leaveWarningView);
            inGameMenuShowed = false;
        });

        leaveWarningView.getChildren().addAll(leaveLabel, yesButton, noButton);
    }

    /**
     * This method is used to show view for the finished game
     * depending on game result in the middle of a game scene.
     * View contains victory or defeat title text along with
     * 2 buttons to leave to menu or restart level.
     *
     * @param victory boolean value which is true if player has won the game
     */
    public void displayGameOverView(boolean victory) {
        gameOverView = new VBox();
        gameOverView.setFocusTraversable(false);
        gameOverView.setLayoutX(width / 2 - 250);
        gameOverView.setLayoutY(height / 2 - 150);
        gameOverView.setAlignment(Pos.CENTER);
        gameOverView.setSpacing(30);

        ImageView result;
        if (victory)
            result = new ImageView(GameModels.VICTORY.getUrl());
        else
            result = new ImageView(GameModels.DEFEAT.getUrl());
        result.setFitWidth(500);
        result.setFitHeight(100);

        MenuButton menuButton = new MenuButton("MENU");
        menuButton.setOnAction(e -> {
            menuView.show();
        });

        MenuButton restartButton = new MenuButton("RESTART");
        restartButton.setOnAction(e -> {
            this.gamePane.getChildren().clear();
            createTopButtons();
            inGameMenuShowed = false;
            game = new Game(this, menuView.getEnemyShipsAmount());
            game.setup();
            game.start();
        });

        gameOverView.getChildren().addAll(result, menuButton, restartButton);
        gamePane.getChildren().add(gameOverView);
        inGameMenuShowed = true;
    }

    /**
     * This method is used to add graphical node to a game view
     *
     * @param node node to be added to a game view
     */
    public void addNode(Node node) {
        gamePane.getChildren().add(node);
    }

    /**
     * This method is used to remove graphical node to a game view
     *
     * @param node node to be removed from a game view
     */
    public void removeNode(Node node) {
        gamePane.getChildren().remove(node);
    }

    /**
     * This method is used to remove cannon balls from a game view after finished game.
     */
    public void clearCannonballs() {
        gamePane.getChildren().removeIf(n ->
                n instanceof CannonBall
        );
    }

    /**
     * This method checks whether node is being displayed on a game pane.
     *
     * @param node node object
     * @return true if node object is on a game pane.
     */
    private boolean isOnGamePane(Node node) {
        return gamePane.getChildren().contains(node);
    }

    /**
     * This method returns bounds of a game pane layout.
     *
     * @return bounds of a game pane layout
     */
    public Bounds getGamePaneLayoutBounds() {
        return gamePane.getLayoutBounds();
    }

    /**
     * Getter for game view width
     *
     * @return width of a game view
     */
    public double getWidth() {
        return width;
    }

    /**
     * Getter for game view height
     *
     * @return height of a game view
     */
    public double getHeight() {
        return height;
    }

    /**
     * Calling this method will change main stage's scene
     * of application to a game scene contained in this class
     */
    public void show() {
        mainStage.setScene(gameScene);
        mainStage.show();
    }
}
