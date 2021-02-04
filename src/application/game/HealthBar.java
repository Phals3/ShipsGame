package application.game;

import application.menu.MenuModels;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * This class is used to graphically display health points of a ship in game.
 */
public class HealthBar extends StackPane {

    private int currentHealthPoints;
    private int maxHealthPoints;
    private int duration = 2;

    private Label healthLabel;
    private Rectangle health;
    private Rectangle frame;

    /**
     * Constructor for graphical node of a ship health bar
     * displaying "current / maximum" health points of a ship.
     *
     * @param healthPoints healthPoints of a Ship object which has a health bar.
     */
    HealthBar(int healthPoints) {
        this.setVisible(false);
        maxHealthPoints = healthPoints;
        currentHealthPoints = maxHealthPoints;
        healthLabel = new Label(Integer.toString(currentHealthPoints) + '/' + Integer.toString(maxHealthPoints));
        Font font = Font.loadFont(getClass().getResourceAsStream(MenuModels.FONT.getUrl()), 10);
        healthLabel.setFont(font);

        frame = new Rectangle(0, 0, 50, 10);
        frame.setStroke(Color.BLACK);
        frame.setFill(Color.TRANSPARENT);

        health = new Rectangle(0, 0, 50, 10);
        health.setFill(Color.GREEN);

        this.getChildren().addAll(frame, health, healthLabel);
    }

    /**
     * This method shows ship's health bar briefly after it's ship has been hit.
     *
     * @param currentHealthPoints current ship health points to update on a health bar label
     */
    public void show(int currentHealthPoints) {
        this.currentHealthPoints = currentHealthPoints;
        healthLabel.setText(Integer.toString(currentHealthPoints) + '/' + maxHealthPoints);
        health.setWidth((float) currentHealthPoints / 2);
        if ((float) currentHealthPoints / maxHealthPoints <= 0.25) health.setFill(Color.RED);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    this.setVisible(true);
                }),
                new KeyFrame(Duration.seconds(duration), e -> {
                    this.setVisible(false);
                })
        );
        timeline.play();
    }
}
