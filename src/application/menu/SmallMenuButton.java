package application.menu;

import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * This class is used to create SmallMenuButton objects which are buttons for game menu display.
 */
public class SmallMenuButton extends Button {

    private final String BUTTON_STYLE = "-fx-background-color: transparent; -fx-background-image: url("
            + MenuModels.SMALL_BLUE_BUTTON_RELEASED.getUrl() + ")";
    private final String BUTTON_PRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image: url("
            + MenuModels.SMALL_BLUE_BUTTON_PRESSED.getUrl() + ")";

    /**
     * Constructor for initializing button with specific parameters.
     * and initializing their mouse listeners.
     *
     * @param text text displayed inside a menu button
     */
    public SmallMenuButton(String text) {
        this.setOpacity(0.5);
        setPrefWidth(49);
        setPrefHeight(49);
        setTextAlignment(TextAlignment.CENTER);
        setFont(Font.font("Arial", FontWeight.BOLD, 20));
        setText(text);
        setStyle(BUTTON_STYLE);
        initializeButtonListeners();
    }

    /**
     * Used to set pressed button style when it's clicked by mouse.
     */
    private void setButtonPressedStyle() {
        setStyle(BUTTON_PRESSED_STYLE);
        setPrefHeight(45);
        setLayoutY(getLayoutY() + 4);
    }

    /**
     * Used to set released button style on mouse release.
     */
    private void setButtonReleasedStyle() {
        setStyle(BUTTON_STYLE);
        setPrefHeight(49);
        setLayoutY(getLayoutY() - 4);
    }

    /**
     * Creates button listeners for mouse events.
     */
    private void initializeButtonListeners() {
        setOnMousePressed(e -> {
            setButtonPressedStyle();
        });
        setOnMouseReleased(e -> {
            setButtonReleasedStyle();
        });
        setOnMouseEntered(e -> {
            setEffect(new DropShadow());
            this.setOpacity(1);
        });
        setOnMouseExited(e -> {
            setEffect(null);
            this.setOpacity(0.5);
        });
    }
}
