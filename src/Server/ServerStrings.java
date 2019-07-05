package Server;

public class ServerStrings {
    public static final String LOGIN = "login username:(.*) password:(.*)";
    public static final String LOGINERROR = "wrong credentials";
    public static final String LOGINSUCCESS = "login successful";
    public static final String MULTIPLAYERREQUEST = "(DeathMatch|Flag|Domination) request from user:(.*)";
    public static final String MULTIPLAYERSUCCESS = "you have your opponent";
    public static final String MULTIPLAYERFAILED = "you can't play, go home";
    public static final String REQUEST_STOCK = "(\\d+) request card stock: (\\d+)";
    public static final String REQUEST_BUY = "(\\d+) request to buy card: (\\d+)";
    public static final String BOUGHT = "bought successfully";
    public static final String OUT_OF_STOCK = "out of stock";
    public static final String REQUEST_SELL = "(\\d+) request to sell card: (\\d+)";
    public static final String SOLD = "sold successfully";
    public static final String GET_LEADERBOARD = "request leaderboard";
}
