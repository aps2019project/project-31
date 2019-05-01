package model;

public class SinglePlayer extends BattleManager {
    private Player playerOne;
    private Player aiPlayer;

    public SinglePlayer(Map map, String gameMode, Player currentPlayer, int maxNumberOfFlags, Player playerOne, Player aiPlayer) {
        super(map, gameMode, currentPlayer, maxNumberOfFlags);
        this.playerOne = playerOne;
        this.aiPlayer = aiPlayer;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getOtherPlayer(String thisPlayerUserName) {
        if (thisPlayerUserName.equals(playerOne.getAccount().getUsername())) {
            return aiPlayer;
        } else
            return playerOne;
    }

    public Player getAiPlayer() {
        return aiPlayer;
    }
    public static void playAi(){

    }

    @Override
    public Player getOtherPlayer() {
        return aiPlayer;
    }
}
