package application.menu;

import application.game.GameModels;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.LinkedList;

/**
 * Class used to replace center content in settings option sub scene.
 * Allows user to choose number of enemy ships which he will play against
 * from 1 to 5 including.
 */
public class SettingsView extends VBox {

    private HBox buttons;
    private MenuButton decreaseButton;
    private Label shipsAmountLabel;
    private MenuButton increaseButton;

    private Label contentLabel;

    private LinkedList<ImageView> shipImages;

    private int shipsAmount = 1;

    private final Font font = Font.loadFont(getClass().getResourceAsStream(MenuModels.FONT.getUrl()), 25);

    /**
     * Constructor creating and initializing content for "SETTINGS" option sub scene.
     */
    SettingsView() {
        this.setAlignment(Pos.TOP_CENTER);
        shipImages = new LinkedList<>();
        createTopMenu();
        createFirstShipImage();
    }

    /**
     * Creates and adds 2 buttons, allowing to change amount of enemy ships,
     * and a label containing number of enemy ships to a top of settings view pane
     */
    private void createTopMenu() {
        contentLabel = new Label(Options.SETTINGS.getContent());
        contentLabel.setFont(font);
        contentLabel.setPadding(new Insets(30, 30, 30, 30));

        decreaseButton = new MenuButton("-");
        decreaseButton.setOnAction(e -> {
            removeShip();
        });
        shipsAmountLabel = new Label(Integer.toString(shipsAmount));
        shipsAmountLabel.setFont(font);

        increaseButton = new MenuButton("+");
        increaseButton.setOnAction(e -> {
            addShip();
        });

        buttons = new HBox();
        buttons.getChildren().addAll(decreaseButton, shipsAmountLabel, increaseButton);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(50);

        this.getChildren().addAll(contentLabel, buttons);
    }

    /**
     * Method used to initialize starting view of a settings view
     * with one ship image - which is minimum amount of enemy ships
     * for a player to play against.
     */
    private void createFirstShipImage() {
        ImageView img = new ImageView(GameModels.getRandomEnemyShipUrl());
        shipImages.add(img);
        this.getChildren().add(img);
    }

    /**
     * Method used to add 1 enemy ship image to a settings view and
     * increase enemy ships number by 1 in game up to 5 on increasing button press.
     */
    private void addShip() {
        if (shipsAmount < 5) {
            ImageView img = new ImageView(GameModels.getRandomEnemyShipUrl());
            shipImages.add(img);
            this.getChildren().add(img);
            shipsAmount++;
            shipsAmountLabel.setText(Integer.toString(shipsAmount));
        }
    }

    /**
     * Method used to remove 1 enemy ship image from a settings view and
     * decrease enemy ships number by 1 in game down to 1 on decreasing button press.
     */
    private void removeShip() {
        if (shipsAmount > 1) {
            ImageView img = shipImages.peekLast();
            shipImages.removeLast();
            this.getChildren().remove(img);
            shipsAmount--;
            shipsAmountLabel.setText(Integer.toString(shipsAmount));
        }
    }

    /**
     * Getter for ships Amount chosen in settings view.
     *
     * @return ships number that a player will play against
     */
    public int getShipsAmount() {
        return shipsAmount;
    }
}
