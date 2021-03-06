package model;

import constants.GameMode;

import java.util.Date;

public class MatchHistory {
    private Player me;
    private Player opponent;
    private String outcome;
    private Date time;
    private GameRecord gameRecord;
    private GameMode gameMode;

    public MatchHistory(Player opponent, Player me, String outcome, GameRecord gameRecord, GameMode gameMode) {
        this.opponent = opponent;
        this.outcome = outcome;
        this.time = new Date();
        this.gameRecord = gameRecord;
        this.me = me;
        this.gameMode = gameMode;
    }

    public void showMatchHistory() {
        System.out.println("Opponent: " + opponent + " , win/lose: " + outcome + " , time: " + figureTime());
    }

    public GameRecord getGameRecord() {
        return gameRecord;
    }

    public String figureTime() {
        Date now = new Date();
        if (now.getYear() - time.getYear() != 0) {
            return (now.getYear() - time.getYear()) + " years ago";
        } else if (now.getMonth() - time.getMonth() != 0) {
            return (now.getMonth() - time.getMonth()) + " months ago";
        } else if (now.getDay() - time.getDay() != 0) {
            return (now.getDay() - time.getDay()) + " days ago";
        } else if (now.getHours() - time.getHours() != 0) {
            return (now.getHours() - time.getHours()) + " hours ago";
        } else if (now.getMinutes() - time.getMinutes() != 0) {
            return (now.getMinutes() - time.getMinutes()) + " minutes ago";
        } else if (now.getSeconds() - time.getSeconds() != 0) {
            return (now.getSeconds() - time.getSeconds()) + " seconds ago";
        }
        return ":|";
    }


    public Player getMe() {
        return me;
    }

    public Player getOpponent() {
        return opponent;
    }

    public String getOutcome() {
        return outcome;
    }

    public Date getTime() {
        return time;
    }

    public GameMode getGameMode() {
        return gameMode;
    }
}
