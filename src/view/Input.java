package view;

import controller.BattleMenu;
import controller.CollectionMenu;
import model.Player;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Input {
    static Scanner scanner = new Scanner(System.in);

    public static void handleSelectCardOrSelectComboCards(Player player) {
        String input = Input.scanner.nextLine();
        if (input.matches("\\s*end turn\\s*"))
            BattleMenu.setAreWeInMiddleOfTurn(false);
        Pattern pattern = Pattern.compile("attack combo (\\d+)\\s+((\\d\\s*)+)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            int opponentCardId = Integer.parseInt(matcher.group(1));
            String[] strNumbers = matcher.group(2).split("\\s");
            BattleMenu.prepareComboAttack(strNumbers, opponentCardId);
        }
        if (input.matches("select \\d+"))
            player.selectACard(Integer.parseInt(input.replace("select", "").trim()));
        BattleMenu.getBattleManager().checkTheEndSituation();
    }

    public static void moveAttackPlayCard() {
        String input = Input.scanner.nextLine();
        if (input.matches("\\s*end turn\\s*"))
            BattleMenu.setAreWeInMiddleOfTurn(false);
        if (input.matches("attack\\s+\\d+")) {
            BattleMenu.attack(Integer.parseInt(input.replace("attack", "").trim()));
        }
        Pattern pattern = Pattern.compile("move to\\((\\d),(\\d)\\)\\s*");
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            BattleMenu.move(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
        }
        pattern = Pattern.compile("insert (\\d+) in \\((\\d),(\\d)\\)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            BattleMenu.insert(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3)));
        }
        BattleMenu.getBattleManager().checkTheEndSituation();
    }

    public static void handleCommandsInCollectionMenu() {
        String input = Input.scanner.nextLine();
        if (input.matches("exit\\s*")) {
            CollectionMenu.back();
            return;
        }
        if (input.matches("show\\s*")) {
            CollectionMenu.showAllCards();
            return;
        }
        if (input.matches("save\\s*")) {
            CollectionMenu.saveAndGoBack();
            return;
        }
        if (input.matches("help")) {
            CollectionMenu.help();
            return;
        }
        Pattern pattern = Pattern.compile("search ((\\w+\\s*)+)\\s*");
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            String[] names = matcher.group(1).trim().split("\\s");
            CollectionMenu.showCardsByNames(names);
            return;
        }
        pattern = Pattern.compile("create deck (\\w+)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches())
            CollectionMenu.createDeck(matcher.group(1));
        pattern = Pattern.compile("delete deck (\\w+)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches())
            CollectionMenu.deleteDeck(matcher.group(1));
        pattern = Pattern.compile("add ((\\d+\\s*)+)to deck (\\w+)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            String[] numbers = matcher.group(1).trim().split("\\s");
            CollectionMenu.addCardsToDeck(numbers,matcher.group(3).trim());
        }
        pattern = Pattern.compile("remove ((\\d+\\s*)+)from deck (\\w+)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            String[] numbers = matcher.group(1).trim().split("\\s");
            CollectionMenu.removeCardsFromDeck(numbers,matcher.group(3).trim());
        }
        if(input.matches("validate deck \\w+"))
            CollectionMenu.checkValidationOfDeck(input.replace("validate deck","").trim());
        if(input.matches("select deck \\w+")){
            CollectionMenu.selectAsMainDeck(input.replace("select deck","").trim());
        }
        if(input.matches("show all decks"))
            CollectionMenu.showAllDecks();
        if(input.matches("show deck \\w+"))
            CollectionMenu.showDeckByName(input.replace("show deck","").trim());



    }
}
