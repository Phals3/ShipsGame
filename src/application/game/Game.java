package application.game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.List;

/**
 * This class is used as a game engine in which behavior of a game is defined.
 */
public class Game {
    private GameView gameView;

    private Timeline gameTimeline;

    private boolean running = false;

    private PlayerShip playerShip;

    private int enemyShipsAmount;
    private List<Ship> ships;
    private List<Ship> newShips;
    private List<Ship> toRemoveShips;

    private List<CannonBall> cannonBalls;
    private List<CannonBall> newCannonBalls;
    private List<CannonBall> toRemoveCannonBalls;

    /**
     * Constructor which initializes it's GameView object and amount of enemy ships in game.
     *
     * @param gameView         game view of a game
     * @param enemyShipsAmount amount of enemy ships in game
     */
    public Game(GameView gameView, int enemyShipsAmount) {
        this.gameView = gameView;
        this.enemyShipsAmount = enemyShipsAmount;
    }

    /**
     * This method nitializes data structures in which ships and
     * cannon balls are stored and creates ships in a game
     */
    public void setup() {
        cannonBalls = new LinkedList<>();
        newCannonBalls = new LinkedList<>();
        toRemoveCannonBalls = new LinkedList<>();

        ships = new LinkedList<>();
        newShips = new LinkedList<>();
        toRemoveShips = new LinkedList<>();

        createPlayerShip();
        createEnemyShips();
    }

    /**
     * This method creates PlayerShip object and places it on a game scene.
     */
    private void createPlayerShip() {
        playerShip = new PlayerShip(GameModels.PLAYER_SHIP.getUrl(),
                gameView.getWidth() / 2 - 100, gameView.getHeight() - 100, this);
        addShip(playerShip);
    }

    /**
     * This method creates EnemyShip objects and places them on a game
     * scene in specific positions depending on their amount.
     */
    private void createEnemyShips() {
        for (int i = 0; i < enemyShipsAmount; i++) {
            double spawnPosX = (gameView.getWidth() / 2) - (enemyShipsAmount) * 100 + i * 200;
            EnemyShip es = new EnemyShip(GameModels.getRandomEnemyShipUrl(), spawnPosX, 100, this);
            addShip(es);
        }
    }

    /**
     * This method is used to start a game loop in which all objects are being animated.
     * In each key frame it calls run function to render a game.
     */
    public void start() {
        running = true;
        int frameRate = 60;
        final Duration d = Duration.millis((int) (1000 / frameRate));
        final KeyFrame oneFrame = new KeyFrame(d, this::run);
        gameTimeline = new Timeline(frameRate, oneFrame);
        gameTimeline.setCycleCount(Animation.INDEFINITE);
        gameTimeline.play();
    }

    /**
     * Method responding for game animation which is called in every new frame of an animation.
     *
     * @param event event which calls the run function
     */
    private void run(Event event) {
        for (Ship s : ships) s.update();
        for (CannonBall c : cannonBalls) c.update();
        checkHits();
        checkCollisions();
        updateShipsList();
        updateCannonBallsList();
        if (gameFinished()) {
            gameView.displayGameOverView(isPlayerAlive());
            gameTimeline.stop();
            gameView.clearCannonballs();
        }
    }

    /**
     * Calling this method stops game animation timeline and pauses the game.
     */
    public void pause() {
        gameTimeline.stop();
        running = false;
    }

    /**
     * Calling this method resumes game animation timeline and unpauses the game.
     */
    public void unpause() {
        gameTimeline.play();
        running = true;
    }

    /**
     * This method is used to check if conditions for finished game are met.
     *
     * @return true if player won or lost
     */
    private boolean gameFinished() {
        return ships.size() <= 1 || playerShip.getCurrentHealthPoints() <= 0;
    }

    /**
     * This method is used to check if player ship is still alive.
     *
     * @return true if player ship is alive
     */
    private boolean isPlayerAlive() {
        return ships.contains(playerShip);
    }

    /**
     * Method used to update ships list during game loop.
     */
    private void updateShipsList() {
        ships.removeAll(toRemoveShips);
        ships.addAll(newShips);
        toRemoveShips.clear();
        newShips.clear();
    }

    /**
     * Method used to update cannon balls list during game loop.
     */
    private void updateCannonBallsList() {
        cannonBalls.removeAll(toRemoveCannonBalls);
        cannonBalls.addAll(newCannonBalls);
        toRemoveCannonBalls.clear();
        newCannonBalls.clear();
    }

    /**
     * Method used to add ship to a game and it's game view.
     *
     * @param ship ship which is added to a game
     */
    public void addShip(Ship ship) {
        newShips.add(ship);
        gameView.addNode(ship);
    }

    /**
     * Method used to remove ship from game and it's game view.
     *
     * @param ship ship which is removed from a game
     */
    public void removeShip(Ship ship) {
        toRemoveShips.add(ship);
        gameView.removeNode(ship);
    }

    /**
     * Method used to add cannon ball to a game and it's game view.
     *
     * @param cannonBall cannon ball which is added to a game
     */
    public void addCannonBall(CannonBall cannonBall) {
        newCannonBalls.add(cannonBall);
        gameView.addNode(cannonBall);
    }

    /**
     * Method used to remove cannon ball from game and it's game view.
     *
     * @param cannonBall cannon ball which is removed from a game
     */
    public void removeCannonBall(CannonBall cannonBall) {
        toRemoveCannonBalls.add(cannonBall);
        gameView.removeNode(cannonBall);
    }

    /**
     * This method checks if any of the ships has collided with other.
     * If it does, the colliding ships is instantly destroyed.
     */
    private void checkCollisions() {
        for (Ship ship : ships)
            for (Ship ship2 : ships)
                if (ship.collides(ship2)) {
                    ship.receiveDamage(1000);
                    ship2.receiveDamage(1000);
                }
    }

    /**
     * This method checks if any of the cannon balls has hit a ship.
     * If it does, the hit method is called to hit a ship by this cannon ball.
     */
    private void checkHits() {
        for (CannonBall cannonBall : cannonBalls)
            for (Ship ship : ships)
                if (cannonBall.collides(ship) && !cannonBall.isDetonated())
                    hit(cannonBall, ship);

    }

    /**
     * This method applies hit effects on cannon ball and a ship.
     *
     * @param cannonBall cannon ball which hits a ship
     * @param ship       which is hit by a cannon ball
     */
    private void hit(CannonBall cannonBall, Ship ship) {
        ship.receiveDamage(cannonBall.getDamage());
        cannonBall.detonate();
        removeCannonBall(cannonBall);
    }

    /**
     * Getter to check whether the game is running.
     *
     * @return true if game is running.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Getter for ships in a game.
     *
     * @return list of ships in game.
     */
    public List<Ship> getShips() {
        return this.ships;
    }

    /**
     * Getter for a player ships on a game scene.
     *
     * @return PlayerShip object in game
     */
    public PlayerShip getPlayerShip() {
        return playerShip;
    }

    /**
     * Getter for a game's GameView object.
     *
     * @return game's GameView object
     */
    public GameView getGameView() {
        return gameView;
    }
}
