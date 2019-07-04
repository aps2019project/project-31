package model;

import javafx.scene.layout.HBox;

public class InfoHBox extends HBox {
    private MatchHistory matchHistory = null;

    public InfoHBox(){
        super();
    }

    public MatchHistory getMatchHistory() {
        return matchHistory;
    }

    public void setMatchHistory(MatchHistory matchHistory) {
        this.matchHistory = matchHistory;
    }
}
