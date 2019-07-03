package model;

import java.util.Date;

public class MatchHistory {
    private String opponent;
    private String outcome;
    private Date time;
    private GameRecord gameRecord;

    public MatchHistory(String opponent, String outcome, GameRecord gameRecord) {
        this.opponent = opponent;
        this.outcome = outcome;
        this.time = new Date();
        this.gameRecord = gameRecord;
    }

    public void showMatchHistory() {
        System.out.println("Opponent: " + opponent + " , win/lose: " + outcome + " , time: " + figureTime());
    }

    public GameRecord getGameRecord() {
        return gameRecord;
    }

    private String figureTime() {
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
}
