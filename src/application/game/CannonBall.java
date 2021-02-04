package application.game;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static application.game.Ship.Side.*;

/**
 * Class used to graphically represent cannon balls shot by ships on a game scene.
 */
public class CannonBall extends StackPane {
    private Game game;

    private ImageView ballImage;
    private Circle collisionCircle;

    private Ship ship;
    private double radius = 8;
    private double angle;
    private double speed = 10;
    private int damage;
    private boolean detonated = false;
    private int explosionSize = 30;

    /**
     * Constructor creating graphical Node for a cannon ball on a game scene
     * Cannon ball start position is placed in the center of ship which shoots it.
     *
     * @param damage    the amount of damage caused if cannon ball hits a target
     * @param ship      ship which shoots a cannon ball
     * @param boardSide ship board side from which the cannon is shot
     * @param game      object of Game class in which cannonball is shot
     */
    public CannonBall(int damage, Ship ship, Ship.Side boardSide, Game game) {
        this.game = game;
        this.ship = ship;

        this.setLayoutX(ship.getCenterX());
        this.setLayoutY(ship.getCenterY());

        ballImage = new ImageView(GameModels.CANNON_BALL.getUrl());
        ballImage.setFitWidth(2 * radius);
        ballImage.setFitHeight(2 * radius);

        collisionCircle = new Circle(radius);
        collisionCircle.setFill(Color.TRANSPARENT);

        if (boardSide == left) angle = ship.getAngle() - 90;
        else if (boardSide == right) angle = ship.getAngle() + 90;
        this.damage = damage;

        this.getChildren().addAll(collisionCircle, ballImage);
    }

    /**
     * Method used to update position of cannonball in a game loop.
     * If cannon ball position reaches out of the game window it's simply removed from a scene.
     */
    public void update() {
        double radians = Math.toRadians(angle);
        double dx = speed * Math.cos(radians);
        double dy = speed * Math.sin(radians);

        double newPosX = this.getLayoutX() + dx;
        double newPosY = this.getLayoutY() + dy;

        if (newPosX < 0 || newPosX > game.getGameView().getWidth() ||
                newPosY < 0 || newPosY > game.getGameView().getHeight()) {
            game.removeCannonBall(this);
        }
        this.setLayoutX(newPosX);
        this.setLayoutY(newPosY);
    }

    /**
     * Method called on cannon ball collision with a target.
     * Creates object of Explosion class which displays
     * explosion animation in a place of detonation.
     */
    public void detonate() {
        if (!detonated) {
            Explosion explosion = new Explosion(this.getLayoutX(), this.getLayoutY(), explosionSize, game);
            explosion.play();
            detonated = true;
        }
    }

    /**
     * Getter for cannon ball damage amount
     *
     * @return amount of damage that cannon ball causes on hit.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Getter for cannon ball detonation state.
     *
     * @return true if cannon ball has already been detonated
     */
    public boolean isDetonated() {
        return detonated;
    }

    /**
     * Method used to check whether cannon ball collides with other graphical node on a game scene
     *
     * @param node graphical node which is being checked for collision with cannonball
     * @return true if cannon ball collides with a node on a game scene.
     */
    public boolean collides(Node node) {
        if (this == node || this.ship == node) return false;
        Point2D ballCenter = this.collisionCircle.localToScene(collisionCircle.getCenterX(),
                collisionCircle.getCenterY());
        Bounds nodeBounds;
        if (node instanceof Ship) {
            Ship ship = (Ship) node;
            nodeBounds = ship.collisionRectangle.localToScene(ship.collisionRectangle.getBoundsInLocal());
        } else nodeBounds = node.localToScene(node.getBoundsInLocal());
        return nodeBounds.contains(ballCenter);
    }

}

