package application.game;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static application.game.Ship.Side.left;
import static application.game.Ship.Side.right;

/**
 * This class is used to create parameters necessary for a ship
 * and to use mainly by extending this class - for example by PlayerShip or EnemyShip class
 */
public class Ship extends Pane {

    protected Game game;

    protected ImageView shipImage;
    protected double width = 100;
    protected double height = 50;
    protected Rectangle collisionRectangle;
    protected HealthBar healthBar;

    protected int currentHealthPoints;
    protected int maxHealthPoints;
    protected int healthPoints = 100;
    protected double angle = 0;
    protected double turningAbility = 3;
    protected double minSpeed = 0;
    protected double maxSpeed = 3;
    protected double speed = 1;
    protected double dxSpeed = 0.5;

    protected int cannonballDamage = 10;
    protected final int CANNONS_RELOAD_TIME = 30;
    protected int rightCannonsReloadTime;
    protected int leftCannonsReloadTime;

    protected enum Side {left, right}

    /**
     * Constructor which creates and initializes a ship for our game:
     * it's image, invisible collision rectangle, health bar and other parameters.
     *
     * @param shipImageUrl url for a ship image which will be displayed on a game scene
     * @param posX         ship start X coordinate on a game scene
     * @param posY         ship start Y coordinate on a game scene
     * @param game         object of Game class in which ship is placed
     */
    public Ship(String shipImageUrl, double posX, double posY, Game game) {
        this.setWidth(height);
        this.setHeight(width);
        this.setLayoutX(posX);
        this.setLayoutY(posY);

        collisionRectangle = new Rectangle(0.7 * width, 0.4 * height, Color.TRANSPARENT);
        collisionRectangle.setLayoutX(0.15 * width);
        collisionRectangle.setLayoutY(0.3 * height);

        this.game = game;
        shipImage = new ImageView(shipImageUrl);
        shipImage.setFitWidth(width);
        shipImage.setFitHeight(height);

        maxHealthPoints = healthPoints;
        currentHealthPoints = maxHealthPoints;
        healthBar = new HealthBar(maxHealthPoints);
        healthBar.setLayoutX(width / 4);

        leftCannonsReloadTime = 0;
        rightCannonsReloadTime = 0;
        this.getChildren().addAll(shipImage, healthBar, collisionRectangle);
    }

    /**
     * This method increases speed by it's ability to accelerate,
     * which is stored in dxSpeed variable.
     */
    protected void increaseSpeed() {
        if (speed < maxSpeed) speed += dxSpeed;
    }

    /**
     * This method decreases speed by it's ability to slow down,
     * which is stored in dxSpeed variable.
     */
    protected void decreaseSpeed() {
        if (speed > minSpeed) speed -= dxSpeed;
    }

    /**
     * This method rotates ship image and it's collision rectangle
     * by given angle.
     *
     * @param angle angle in which ship is directed
     */
    protected void setAngle(double angle) {
        shipImage.setRotate(angle);
        collisionRectangle.setRotate(angle);
    }

    /**
     * This method causes ship to change it's angle by turning ability
     * so it turns left on a game scene.
     */
    protected void turnLeft() {
        angle -= turningAbility;
        setAngle(angle);
    }

    /**
     * This method causes ship to change it's angle by turning ability
     * so it turns right on a game scene.
     */
    protected void turnRight() {
        angle += turningAbility;
        setAngle(angle);
    }

    /**
     * This method updates ship position on a game scene based on current position, angle, speed.
     * It's also decreasing cannonsReloadTime to zero allowing ship too shoot.
     */
    public void update() {
        double radians = Math.toRadians(angle);
        double dx = speed * Math.cos(radians);
        double dy = speed * Math.sin(radians);

        double newPosX = this.getLayoutX() + dx;
        double newPosY = this.getLayoutY() + dy;

        leftCannonsReloadTime--;
        rightCannonsReloadTime--;
        if (newPosX < 0 || newPosX > game.getGameView().getWidth() - shipImage.getFitWidth() ||
                newPosY < 0 || newPosY > game.getGameView().getHeight() - shipImage.getFitHeight()) {
            return;
        }
        this.setLayoutX(newPosX);
        this.setLayoutY(newPosY);
    }

    /**
     * This method is used to shoot cannon ball from a ship's side.
     * @param boardSide side of a ship from which cannon ball will be shot.
     */
    protected void cannonsShoot(Side boardSide) {
        if ((boardSide == left && leftCannonsReloadTime > 0) || (boardSide == right && rightCannonsReloadTime > 0)) {
            return;
        }
        CannonBall c = new CannonBall(this.cannonballDamage, this, boardSide, game);
        game.addCannonBall(c);
        if (boardSide == left) leftCannonsReloadTime = CANNONS_RELOAD_TIME;
        else if (boardSide == right) rightCannonsReloadTime = CANNONS_RELOAD_TIME;
    }

    /**
     * This method causes ship receive dmg by decreasing it's current health points.
     * If ship's current health points are lower then zero then ship explodes and is removed from the game.
     * @param damage amount of dmg taken by ship
     */
    public void receiveDamage(int damage) {
        currentHealthPoints -= damage;
        if (currentHealthPoints <= 0) {
            Explosion explosion = new Explosion(this.getLayoutX() + width / 2,
                    this.getLayoutY() + height / 2, width, game);
            explosion.play();
            game.removeShip(this);
        } else
            healthBar.show(currentHealthPoints);
    }

    /**
     * Method used to check whether ship collides with other graphical node on a game scene.
     *
     * @param node graphical node which is being checked for collision with a ship
     * @return true if ship collides with a node on a game scene.
     */
    public boolean collides(Node node) {
        if (this == node) return false;
        Bounds thisBounds = this.collisionRectangle.localToScene(this.collisionRectangle.getBoundsInLocal());
        Bounds nodeBounds;
        if (node instanceof Ship) {
            Ship ship = (Ship) node;
            nodeBounds = ship.collisionRectangle.localToScene(ship.collisionRectangle.getBoundsInLocal());
        } else nodeBounds = node.localToScene(node.getBoundsInLocal());
        return thisBounds.intersects(nodeBounds);
    }

    /**
     * Getter of current ship angle which it's rotated by.
     * @return angle which ship is directed in.
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Getter for current ship's health points.
     * @return current ship's health points
     */
    public int getCurrentHealthPoints() {
        return currentHealthPoints;
    }

    /**
     * Getter for a ship's center X position on a game layout.
     * @return ship's center X position
     */
    public double getCenterX() {
        return this.getLayoutX() + this.width / 2;
    }

    /**
     * Getter for a ship's center Y position on a game layout.
     * @return ship's center Y position
     */
    public double getCenterY() {
        return this.getLayoutY() + this.height / 2;
    }

}