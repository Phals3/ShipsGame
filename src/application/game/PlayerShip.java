package application.game;

import javafx.scene.input.KeyCode;

import java.util.*;

import static application.game.Ship.Side.*;

/**
 * This class is used to create a player ship in game which is controlled by keyboard input.
 */
public class PlayerShip extends Ship {

    /**
     * Constructor which creates and initializes player ship by calling it's Ship parent constructor
     * and creates key listeners for a keyboard to control a ship manually.
     *
     * @param shipImageUrl url for a ship image which will be displayed on a game scene
     * @param posX         ship start X coordinate on a game scene
     * @param posY         ship start Y coordinate on a game scene
     * @param game         object of Game class in which ship is placed
     */
    public PlayerShip(String shipImageUrl, double posX, double posY, Game game) {
        super(shipImageUrl, posX, posY, game);
        createKeyListeners();
    }

    /**
     * This method makes player ship listen to keyboard input and defines behaviour on specific keys.
     */
    private void createKeyListeners() {
        this.setFocusTraversable(true);
        List<KeyCode> acceptedCodes = Arrays.asList(KeyCode.W, KeyCode.S, KeyCode.A, KeyCode.D, KeyCode.Q, KeyCode.E);
        Set<KeyCode> codes = new HashSet<>();
        this.setOnKeyPressed(e -> {
            if (acceptedCodes.contains(e.getCode()) && game.isRunning()) {
                codes.add(e.getCode());
                if (codes.contains(KeyCode.W))
                    increaseSpeed();
                if (codes.contains(KeyCode.S))
                    decreaseSpeed();
                if (codes.contains(KeyCode.A))
                    turnLeft();
                if (codes.contains(KeyCode.D))
                    turnRight();
                if (codes.contains(KeyCode.Q))
                    cannonsShoot(left);
                if (codes.contains(KeyCode.E))
                    cannonsShoot(right);
            }
        });
        this.setOnKeyReleased(e -> {
            codes.remove(e.getCode());
        });
    }

}
