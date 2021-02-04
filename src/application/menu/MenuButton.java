package application.menu;

import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.text.Font;

/**
 * This class is used to create MenuButton objects which are buttons for game menu display.
 */
public class MenuButton extends Button {

    private final String BUTTON_STYLE = "-fx-background-color: transparent; -fx-background-image: url("
            + MenuModels.BLUE_BUTTON_RELEASED.getUrl() + ")";
    private final String BUTTON_PRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image: url("
            + MenuModels.BLUE_BUTTON_PRESSED.getUrl() + ")";

    /**
     * Constructor for initializing button with specific parameters.
     * and initializing their mouse listeners.
     *
     * @param text text displayed inside a menu button
     */
    public MenuButton(String text) {
        setPrefWidth(190);
        setPrefHeight(49);
        Font font = Font.loadFont(getClass().getResourceAsStream(MenuModels.FONT.getUrl()), 25);
        setFont(font);
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
        });
        setOnMouseExited(e -> {
            setEffect(null);
        });
    }
}
