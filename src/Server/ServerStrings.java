package Server;

public class ServerStrings {
    public static final String LOGIN = "login username:(.*) password:(.*)";
    public static final String LOGINERROR = "wrong credentials";
    public static final String LOGINSUCCESS = "login successful";
    public static final String REQUEST_STOCK = "request card stock: (\\d+)";
    public static final String MULTIPLAYERREQUEST = "(DeathMatch|Flag|Domination) request from user:(.*)";
    public static final String MULTIPLAYERSUCCESS = "you have your opponent";
    public static final String MULTIPLAYERFAILED = "you can't play, go home";
}
