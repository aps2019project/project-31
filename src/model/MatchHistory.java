package model;

import java.util.Date;

public class MatchHistory {
    private String opponent;
    private String outcome;
    private Date time;

    public MatchHistory(String opponent, String outcome, String time) {
        this.opponent = opponent;
        this.outcome = outcome;
        this.time = new Date();
    }

    public void showMatchHistory() {
        System.out.println("Opponent: " + opponent + " , win/lose: " + outcome + " , time: " + figureTime());
    }

    private String figureTime() {

    }
}
