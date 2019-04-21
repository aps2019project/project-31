package view;

import model.Account;

public class Output {

    public static void showLeaderBoard() {
        Account.sortAllAccounts();
        for (int i = 0; i < Account.getAllAccounts().size(); i++) {
            System.out.println((i + 1) + " - UserName: " + Account.getAllAccounts().get(i).getUsername() + " -Wins: " +
                    Account.getAllAccounts().get(i).getWinLoseDraw()[0]);
        }

    }
}
