package application.game;

import java.util.ArrayList;
import java.util.Random;

/**
 * Enum used to store urls for game models - mainly images.
 * Created for easier code modification and code clarity.
 */
public enum GameModels {
    WATER_TILE("water_tile.png"),
    CANNON_BALL("cannonball.png"),
    EXPLOSION1("explosion1.png"),
    EXPLOSION2("explosion2.png"),
    EXPLOSION3("explosion3.png"),

    VICTORY("victory.png"),
    DEFEAT("defeat.png"),

    PLAYER_SHIP("player_ship.png"),

    ENEMY_SHIP1("enemy_ship1.png"),
    ENEMY_SHIP2("enemy_ship2.png"),
    ENEMY_SHIP3("enemy_ship3.png"),
    ENEMY_SHIP4("enemy_ship4.png"),
    ENEMY_SHIP5("enemy_ship5.png");

    private static final String directoryName = "/application/resources/";
    private String url;
    private static final int enemyShipModelsAmount = 5;

    /**
     * Constructor initializing enum type and setting
     * url of a model placed in project's "resources" package.
     *
     * @param modelFileName of a file used as a model in game.
     */
    GameModels(String modelFileName) {
        this.url = directoryName + modelFileName;
    }

    /**
     * Getter for model url.
     *
     * @return url of a model file in project's "resources" package
     */
    public String getUrl() {
        return url;
    }

    /**
     * Static method returning random ship model url used to create enemy ship.
     *
     * @return random url of enemy ship image model
     */
    public static String getRandomEnemyShipUrl() {
        ArrayList<String> enemyShips = new ArrayList<>();
        enemyShips.add(ENEMY_SHIP1.url);
        enemyShips.add(ENEMY_SHIP2.url);
        enemyShips.add(ENEMY_SHIP3.url);
        enemyShips.add(ENEMY_SHIP4.url);
        enemyShips.add(ENEMY_SHIP5.url);
        int r = new Random().nextInt(enemyShipModelsAmount);
        return enemyShips.get(r);
    }
}
