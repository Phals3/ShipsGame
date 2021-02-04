package application.menu;

/**
 * Enum used to store title and content for sub scene of corresponding menu option
 * Created for easier menu content modification and code clarity.
 */
public enum Options {

    PLAY("PLAY", ""),
    SETTINGS("SETTINGS", "Choose amount of enemy ships"),
    CREDITS("CREDITS", "Created By\nMateusz Borowiecki"),
    HELP("HELP", "To win simply destroy\n" +
            "all enemy ships\n\n\n" +
            "Controls:\n\n" +
            "W - speed up\n" +
            "S - Slow Down\n" +
            "A - Turn Left\nD - Turn Right\n" +
            "Q, E - Shoot"),
    EXIT("EXIT", "");

    private String title;
    private String content;

    /**
     * Initializes menu option with it's title and content.
     *
     * @param title   for menu button and it's menu option sub scene
     * @param content is a text which will be showed on option subscene.
     */
    Options(String title, String content) {
        this.title = title;
        this.content = content;
    }

    /**
     * Getter for title of an option.
     *
     * @return option title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for content text of an option.
     *
     * @return content text
     */
    public String getContent() {
        return content;
    }
}
