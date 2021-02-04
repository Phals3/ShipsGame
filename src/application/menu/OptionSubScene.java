package application.menu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Class used to display sub scene in menu view on press of corresponding menu button.
 */
public class OptionSubScene extends SubScene {

    private Options option;
    private double width;
    private double height;

    private BorderPane optionPane;

    private final Font font = Font.loadFont(getClass().getResourceAsStream(MenuModels.FONT.getUrl()), 25);

    /**
     * Constructor which creates and initializes sub scene
     * and it's content for corresponding menu button option.
     *
     * @param option enum type of Options class corresponding to a button
     * @param width  of a menu view scene
     * @param height of a menu view scene
     */
    OptionSubScene(Options option, double width, double height) {
        super(new BorderPane(), width, height);
        this.option = option;
        this.width = width;
        this.height = height;

        this.setVisible(false);

        optionPane = (BorderPane) this.getRoot();
        createBackground();
        createTitleLabel(option.getTitle());
        createCenterContent(option.getContent());
        createCloseButton();
    }

    /**
     * Creates and sets background for option sub scene
     */
    private void createBackground() {
        Background background = new Background(
                new BackgroundFill(Color.web("#1EA7E1"), new CornerRadii(25),
                        new Insets(0.0, 0.0, 0.0, 0.0)
                ));
        optionPane.setBackground(background);
        optionPane.setBorder(new Border(new BorderStroke(Color.web("#1989B8"),
                BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(10))));
    }

    /**
     * Creates and adjusts title label for option to display in sub scene.
     */
    private void createTitleLabel(String title) {
        Label titleLabel = new Label(title);
        titleLabel.setTextAlignment(TextAlignment.CENTER);
        titleLabel.setFont(font);

        optionPane.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        BorderPane.setMargin(titleLabel, new Insets(30, 30, 30, 30));
    }

    /**
     * Creates and initializes content text in a center part of sub scene pane.
     */
    private void createCenterContent(String content) {
        Label contentLabel = new Label(content);
        contentLabel.setTextAlignment(TextAlignment.CENTER);
        contentLabel.setFont(font);

        optionPane.setCenter(contentLabel);
        BorderPane.setAlignment(contentLabel, Pos.CENTER);
        BorderPane.setMargin(contentLabel, new Insets(30, 30, 30, 30));
    }

    /**
     * Creates sub scene close button.
     * On press it hides sub scene and returns to standard menu view.
     */
    private void createCloseButton() {
        MenuButton closeButton = new MenuButton("CLOSE");
        closeButton.setOnAction(e -> {
            this.setVisible(false);
        });

        optionPane.setBottom(closeButton);
        BorderPane.setAlignment(closeButton, Pos.CENTER);
        BorderPane.setMargin(closeButton, new Insets(30, 30, 30, 30));
    }

    /**
     * Method to replace center content in sub scene with a custom Node class object.
     *
     * @param node which will be displayed instead of center content text
     */
    public void addContentPane(Node node) {
        optionPane.setCenter(node);
        BorderPane.setAlignment(node, Pos.CENTER);
    }

}
