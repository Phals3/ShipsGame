package application.game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * Class used to display explosion animation on a game scene
 */
public class Explosion {

    private Game game;
    private ImageView explosion;
    private int frameDuration = 200;
    private double size1;
    private double size2;
    private double size3;

    private double layoutX;
    private double layoutY;

    /**
     * Constructor initializing Explosion class object,
     * it's size and position on a game scene
     *
     * @param layoutX position on X axis
     * @param layoutY position on Y axis
     * @param size    size of explosion
     * @param game    game in which explosion is happening
     */
    Explosion(double layoutX, double layoutY, double size, Game game) {
        this.game = game;
        explosion = new ImageView();
        this.layoutX = layoutX;
        this.layoutY = layoutY;
        size1 = size;
        size2 = 0.8 * size;
        size3 = 0.5 * size2;
    }

    /**
     * Method which plays explosion animation on a game scene
     */
    public void play() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    game.getGameView().addNode(explosion);
                    setFrameImage(new Image(GameModels.EXPLOSION1.getUrl()), size1);
                }),
                new KeyFrame(Duration.millis(frameDuration), e -> {
                    setFrameImage(new Image(GameModels.EXPLOSION2.getUrl()), size2);
                }),
                new KeyFrame(Duration.millis(2 * frameDuration), e -> {
                    setFrameImage(new Image(GameModels.EXPLOSION3.getUrl()), size3);
                }),
                new KeyFrame(Duration.millis(3 * frameDuration), e -> {
                    game.getGameView().removeNode(explosion);
                })
        );
        timeline.play();
    }

    /**
     * Sets image for explosion frame in animation timeline
     *
     * @param image image for frame of an explosion
     * @param size  size of explosion
     */
    private void setFrameImage(Image image, double size) {
        explosion.setImage(image);
        explosion.setLayoutX(layoutX - size / 2);
        explosion.setLayoutY(layoutY - size / 2);
        explosion.setFitWidth(size);
        explosion.setFitHeight(size);
    }

}
