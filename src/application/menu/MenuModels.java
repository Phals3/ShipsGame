package application.menu;

/**
 * Enum used to store urls for menu models such as font or images.
 * Created for easier code modification and code clarity.
 */
public enum MenuModels {
    FONT("main_font.ttf"),
    BLUE_BUTTON_RELEASED("blue_button_released.png"),
    BLUE_BUTTON_PRESSED("blue_button_pressed.png"),
    SMALL_BLUE_BUTTON_RELEASED("small_blue_button_released.png"),
    SMALL_BLUE_BUTTON_PRESSED("small_blue_button_pressed.png"),
    WATER_TILE("water_tile.png");


    private static final String directoryName = "/application/resources/";
    private String url;

    /**
     * Constructor initializing enum type and setting
     * url of a model placed in project's "resources" package.
     *
     * @param modelFileName name of a file used as a model in application menu.
     */
    MenuModels(String modelFileName) {
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
}
