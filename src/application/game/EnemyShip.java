package application.game;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

import java.util.List;
import java.util.Random;

import static application.game.EnemyShip.ways.*;
import static application.game.Ship.Side.left;
import static application.game.Ship.Side.right;

/**
 * This class is used to create instances of EnemyShip class in game.
 * They are bots which player will play against and they are controlled by simple AI.
 */
public class EnemyShip extends Ship {

    private List<Ship> ships;
    private PlayerShip playerShip;

    private final double lineLength = 400;
    private Line leftShootingLine;
    private Line rightShootingLine;
    private Rotate shootingLineRotate;

    private Polygon leftTriangle;
    private Polygon rightTriangle;
    private Rotate trianglesRotate;
    private double triangleLength = 100;
    private double triangleWidth = 100;

    public enum ways {goLeft, goStraight, goRight}

    private ways way = ways.goStraight;
    private Random random;

    /**
     * Constructor which creates and initializes enemy ship by calling it's Ship parent constructor
     * and creates invisible graphical nodes which are used for simple AI mechanism.
     *
     * @param shipImageUrl url for a ship image which will be displayed on a game scene
     * @param posX         ship start X coordinate on a game scene
     * @param posY         ship start Y coordinate on a game scene
     * @param game         object of Game class in which ship is placed
     */
    public EnemyShip(String shipImageUrl, double posX, double posY, Game game) {
        super(shipImageUrl, posX, posY, game);
        this.ships = game.getShips();
        this.playerShip = game.getPlayerShip();
        random = new Random();
        angle += 90;
        super.setAngle(angle);
        createShootingLines();
        createFrontViewTriangles();
    }

    /**
     * Method used to initialize triangles, which will be used as a front fields of a ship's view
     * Triangles help to avoid collisions with other ships and avoid getting stuck on
     * game scene borders which they can't cross.
     */
    private void createFrontViewTriangles() {
        trianglesRotate = new Rotate();
        trianglesRotate.setPivotX(width / 2);
        trianglesRotate.setPivotY(height / 2);
        trianglesRotate.setAngle(angle);

        leftTriangle = createFrontViewTriangle(left);
        rightTriangle = createFrontViewTriangle(right);
    }

    /**
     * Method used to initialize specific left or right triangle, which will be used as a front field of a ship's view
     * Triangle help to avoid collisions with other ships and avoid getting stuck on
     * game scene borders which they can't cross
     */
    private Polygon createFrontViewTriangle(Side side) {
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(width / 2, height / 2,
                width + triangleLength, height / 2,
                width + triangleLength, side == left ? height / 2 - triangleWidth : height / 2 + triangleWidth);
        triangle.setFill(Color.TRANSPARENT);
        triangle.getTransforms().add(trianglesRotate);
        this.getChildren().add(triangle);
        return triangle;
    }

    /**
     * Method used to initialize left and right board shooting lines which will
     * help detect if player ship is on it's shooting line.
     */
    private void createShootingLines() {
        shootingLineRotate = new Rotate();
        shootingLineRotate.setPivotX(width / 2);
        shootingLineRotate.setPivotY(height / 2);
        shootingLineRotate.setAngle(angle);

        leftShootingLine = createShootingLine(left);
        rightShootingLine = createShootingLine(right);
    }

    /**
     * Method used to initialize specific left or right board shooting line which will
     * help detect if player ship is on it's shooting line.
     */
    private Line createShootingLine(Side side) {
        Line shootingLine = new Line(width / 2, height / 2, width / 2,
                side == left ? -lineLength : lineLength);
        shootingLine.setStrokeWidth(5);
        shootingLine.setStroke(Color.TRANSPARENT);
        shootingLine.getTransforms().add(shootingLineRotate);
        this.getChildren().add(shootingLine);
        return shootingLine;
    }

    /**
     * This method overrides it's Ship parent method and calls it and other methods
     * which will determine it's behavior based on current positions of ships on a game scene.
     * Simple AI for enemy ship detects whether to shoot player ship and chooses it's
     * moving direction to avoid collisions or getting stuck on a border of a game scene.
     */
    @Override
    public void update() {
        shootPlayerShip();
        chooseWay();
        if (way == goLeft) turnLeft();
        else if (way == goRight) turnRight();
        super.update();
    }

    /**
     * This method overrides it's Ship parent method and calls it and other methods
     * to rotate shooting lines and view triangles along with their current direction
     * and ship rotation angle in game scene.
     *
     * @param angle angle in which ship is directed
     */
    @Override
    protected void setAngle(double angle) {
        super.setAngle(angle);
        shootingLineRotate.setAngle(angle);
        trianglesRotate.setAngle(angle);
    }

    /**
     * Method which determines in which way a ship will go to next based on surroundings.
     * It prioritizes avoiding collisions with other ships and then not getting stuck on
     * game scene borders which they can't cross.
     * If ship detects no ships around it chooses random way.
     */
    private void chooseWay() {
        for (Ship ship : ships) {
            if (this == ship) continue;
            if (ship.collides(leftTriangle) && ship.collides(rightTriangle)) {
                way = goStraight;
                return;
            } else if (ship.collides(leftTriangle)) {
                way = goRight;
                return;
            } else if (ship.collides(rightTriangle)) {
                way = goLeft;
                return;
            } else if (!ship.collides(leftTriangle) && !ship.collides(rightTriangle)) {
                way = randomWay();
            }
        }

        if (isOutOfTheWindow(leftTriangle)) {
            way = goRight;
        } else if (isOutOfTheWindow(rightTriangle)) {
            way = goLeft;
        }

    }

    /**
     * This method chooses random way of moving for a ship
     * with bigger chance for going straight
     *
     * @return random way that a ship will go next
     */
    private ways randomWay() {
        int b = 2000;
        int x = random.nextInt(b);
        if (0 < x && x < 0.25 * b) return goLeft;
        if (0.25 * b < x && x < 0.75 * b) return goStraight;
        if (0.75 * b < x && x < b) return goRight;
        return goStraight;
    }

    /**
     * This method informs whether ship detects that it's view is outside of the game scene
     *
     * @param viewPolygon figure which represents ship's field of view
     * @return true if field of view is out of the application window
     */
    private boolean isOutOfTheWindow(Polygon viewPolygon) {
        Bounds gamePaneBounds = game.getGameView().getGamePaneLayoutBounds();
        Bounds polygonBounds = viewPolygon.localToScene(viewPolygon.getBoundsInLocal());
        return !gamePaneBounds.contains(polygonBounds);
    }

    /**
     * Method used to shoot in direction of player ship.
     * If shooting line from one side collides with a player
     * then it shoots cannon balls from corresponding board side
     */
    private void shootPlayerShip() {
        if (playerShip.collides(leftShootingLine))
            cannonsShoot(left);
        else if (playerShip.collides(rightShootingLine))
            cannonsShoot(right);
    }

}
