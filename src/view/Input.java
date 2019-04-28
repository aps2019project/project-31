package view;

import controller.BattleMenu;
import model.BattleManager;
import model.Deployable;
import model.Map;
import model.Player;
import model.TargetStrings;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Input {
    static Scanner scanner = new Scanner(System.in);

    public static void handleSelectCardOrSelectComboCards(Player player) {
        String input = Input.scanner.nextLine();
        Pattern pattern = Pattern.compile("attack combo (\\d+)\\s+((\\d\\s*)+)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            int opponentCardId = Integer.parseInt(matcher.group(1));
            String[] strNumbers = matcher.group(2).split("\\s");
            ArrayList<Deployable> validCards = new ArrayList<>();
            for (String number : strNumbers) {
                int cardId = Integer.parseInt(number);
                if (Map.findCellByCardId(cardId).getCardInCell() != null &&
                        Map.findCellByCardId(opponentCardId).getCardInCell() != null &&
                        BattleManager.isAttackTypeValidForAttack(Map.findCellByCardId(cardId).getCardInCell(),
                                Map.findCellByCardId(opponentCardId).getCardInCell())) {
                   if(Map.findCellByCardId(cardId).getCardInCell().isCombo())
                        validCards.add(Map.findCellByCardId(cardId).getCardInCell());
                }
            }
            BattleMenu.getBattleManager().comboAtack(Map.findCellByCardId(opponentCardId).getCardInCell(), validCards);
        }
        if(input.matches("select \\d+"))
            player.selectACard(Integer.parseInt(input.replace("select","").trim()));
    }

    public static void moveAttackPlayCard() {
        String input = Input.scanner.nextLine();

    }
}
