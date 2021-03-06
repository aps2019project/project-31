package Server;

public class ServerStrings {
    public static final String LOGIN = "login username:(.*) password:(.*)";
    public static final String LOGINERROR = "wrong credentials";
    public static final String LOGINSUCCESS = "login successful";
    public static final String MULTIPLAYERREQUEST = "(\\d+) (DeathMatch|Flag|Domination) request from user:(.*)";
    public static final String MULTIPLAYERSUCCESS = "you have your opponent";
    public static final String MULTIPLAYERFAILED = "you can't play, go home";
    public static final String REQUEST_STOCK = "(\\d+) request card stock: (\\d+)";
    public static final String REQUEST_BUY = "(\\d+) request to buy card: (\\d+)";
    public static final String BOUGHT = "bought successfully";
    public static final String OUT_OF_STOCK = "out of stock";
    public static final String REQUEST_SELL = "(\\d+) request to sell card: (\\d+)";
    public static final String SOLD = "sold successfully";
    public static final String GET_LEADERBOARD = "request leaderboard";
    public static final String LOGOUT = "requesting logout";
    public static final String SIGNUP_SUCCESSFUL = "signup successful!";
    public static final String REQUEST_SIGNUP = "signup username:(.*) password:(.*)";
    public static final String ALREADY_TAKEN = "username taken";
    public static final String NEW_DECK = "new deck:(.*)";
    public static final String NEW_DECK_SUCCESS = "new deck created!";
    public static final String ADD_CARD_TO_DECK = "add (\\d+) to (.*)";
    public static final String CARD_ADDED = "card added successfuly";
    public static final String CANCELPLAYREQUEST = "request is canceled";
    public static final String CANCELSUCCESSFULLY = "canceled successfully";
    public static final String DECK_DELETED = "deck deleted!";
    public static final String DELETE_DECK = "remove deck:(.*)";
    public static final String CARD_DELETED = "card removed from deck!";
    public static final String DELETE_CARD_REQUEST = "remove card:(\\d+) from deck:(.*)";
    public static final String MAIN_DECK_SET = "main deck successfully set";
    public static final String SET_AS_MAIN_REQUEST = "set (.*) as main";
    public static final String CONCEDE = "CONCEDE";
    public static final String NOTALLOWED = "not allowed";
    public static final String ENDTURN = "end turn";
    public static final String GAMEENDED = "the game ended";
    public static final String ENTERING_CHATROOM = "entering chatroom";
    public static final String RECEIVE_MESSAGE = "receive message: (.*): ###(.*)";
    public static final String EXIT_CHATROOM = "exit chatroom";
    public static final String SEND_MESSAGE = "send message: (.*)";
    public static final String CHERTMESSAGE = "chert message";
    public static final String SENDING_NEW_CARD = "sending new custom card";
}
