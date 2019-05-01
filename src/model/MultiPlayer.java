package model;

public class MultiPlayer extends BattleManager {
    private Player playerOne;
    private Player playerTwo;

    public MultiPlayer(Map map, String gameMode, Player currentPlayer, int maxNumberOfFlags, Player playerOne, Player playerTwo) {
        super(map, gameMode, currentPlayer, maxNumberOfFlags);
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getOtherPlayer(String thisPlayerUserName) {
        if (thisPlayerUserName.equals(playerOne.getAccount().getUsername())) {
            return playerTwo;
        } else
            return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    @Override
    public Player getOtherPlayer() {
        if (getCurrentPlayer() == playerOne) return playerTwo;
        return playerOne;
    }
}
