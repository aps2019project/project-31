package view;

import controller.*;
import model.BattleManager;
import model.Player;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Input {
    private static Input instance = null;

    public static Input getInstance() {
        if (instance == null)
            instance = new Input();
        return instance;
    }

    private Input() {

    }

    static Scanner scanner = new Scanner(System.in);
    private static MenuManager menuManager = new MenuManager();

    public static MenuManager getMenuManager() {
        return menuManager;
    }

    public static void setMenuManager(MenuManager menuManager) {
        Input.menuManager = menuManager;
    }

    public static String giveMeInput() {
        return scanner.nextLine();
    }

    public static void handleSelectComboCards(Player player, String input) {

        Pattern pattern = Pattern.compile("attack combo (\\d+)\\s+((\\d\\s*)+)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            int opponentCardId = Integer.parseInt(matcher.group(1));
            String[] strNumbers = matcher.group(2).split("\\s");
            BattleMenu.prepareComboAttack(strNumbers, opponentCardId);
        }

    }

    public static void moveAttackPlayCard(String input) {
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

    public static void start() {
        MenuManager.initMenus();
        System.err.println("MenuManager initialized");
        while (scanner.hasNextLine()) {
            switch (menuManager.getCurrentMenu().getId()) {
                case Menu.Id.MAIN_MENU:
                    handleCommandsInMainMenu();
                    break;
                case Menu.Id.COLLECTION_MENU:
                    handleCommandsInCollectionMenu();
                    break;
                case Menu.Id.BATTLE_MENU:
                    handleCommandsInBattleMenu();
                    break;
                case Menu.Id.LOGIN_MENU:
                    handleCommandsInLoginMenu();
                    break;
                case Menu.Id.SHOP_MENU:
                    handleCommandsInShop();
                    break;
            }
        }
    }

    private static void checkGenerals(String input) {
        if (input.matches("\\s*\\d+\\s*")) {
            System.err.println("user entered a number");
            int index = Integer.parseInt(input) - 1;
            menuManager.performClickOnMenu(index);
        } else if (input.equalsIgnoreCase("back")) {
            if (menuManager.getCurrentMenu().getId() == Menu.Id.MAIN_MENU) {
                MenuManager.exitGame();
            }
            System.err.println("going back...");
            menuManager.back();
        }
    }

    public static void handleCommandsInBattleMenu(Player player, boolean isThereSelectedCard) {
        String input = scanner.nextLine();
        if (input.matches("select \\d+"))
            player.selectACard(Integer.parseInt(input.replace("select", "").trim()));
        if (isThereSelectedCard) {
            moveAttackPlayCard(input);
        } else {
            handleSelectComboCards(player, input);
        }
        if (input.matches("\\s*end turn\\s*")) {
            BattleMenu.setAreWeInMiddleOfTurn(false);
        }
        if (input.equalsIgnoreCase("game info")) {
            BattleMenu.showGameInfo();
        } else if (input.trim().equalsIgnoreCase("show my minions")) {
            BattleMenu.showPlayerMinions(player);
        } else if (input.trim().equalsIgnoreCase("show opponent minions")) {
            BattleMenu.showPlayerMinions(BattleMenu.getBattleManager().getOtherPlayer());
        } else if (input.matches("show card info \\d+")) {
            String cardUniqueId = input.replace("show card info", "").trim();
            System.out.println(BattleManager.findCardByUniqueid(Integer.parseInt(cardUniqueId)).infoToString());
        }
        BattleMenu.getBattleManager().checkTheEndSituation();
    }

    public static void handleCommandsInCollectionMenu() {
        String input = scanner.nextLine();
        checkGenerals(input);
        if (input.equalsIgnoreCase("show")) {
            System.err.println("showing all card in collection");
            CollectionMenu.showAllMyCards();
            return;
        }
        if (input.equalsIgnoreCase("save")) {
            System.err.println("saving ...");
            CollectionMenu.saveAndGoBack();
            return;
        }
        if (input.equalsIgnoreCase("help")) {
            System.err.println("showing user it's options");
            System.out.println("commands you can enter :\n" +
                    "-show\n" +
                    "-save\n" +
                    "-search [card1 name] [card2 name] ...\n" +
                    "-create deck [deck name]\n" +
                    "-delete deck [deck name]\n" +
                    "-add [card ids] to deck [deck name]\n" +
                    "-remove [card ids] from deck [deck name]\n" +
                    "-validate deck [deck name]\n" +
                    "-select deck [deck name]\n" +
                    "-show deck [deck name]\n" +
                    "-show all decks\n"
            );
            return;
        }
        Pattern pattern = Pattern.compile("search ((\\w+\\s*)+)\\s*");
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            System.err.println("searching in collection...");
            String[] names = matcher.group(1).trim().split("\\s");
            System.err.println("founded cards :");
            CollectionMenu.showCardsByNames(names);
            return;
        }
        pattern = Pattern.compile("create deck (\\w+)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            System.err.println("creating deck");
            CollectionMenu.createDeck(matcher.group(1));
            return;
        }
        pattern = Pattern.compile("delete deck (\\w+)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            System.err.println("deleting deck");
            CollectionMenu.deleteDeck(matcher.group(1));
            return;
        }
        pattern = Pattern.compile("add ((\\d+\\s*)+)to deck (\\w+)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            System.err.println("adding cards to deck");
            String[] numbers = matcher.group(1).trim().split("\\s");
            CollectionMenu.addCardsToDeck(numbers, matcher.group(3).trim());
            return;
        }
        pattern = Pattern.compile("remove ((\\d+\\s*)+)from deck (\\w+)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            System.err.println("removing card from deck");
            String[] numbers = matcher.group(1).trim().split("\\s");
            CollectionMenu.removeCardsFromDeck(numbers, matcher.group(3).trim());
            return;
        }
        if (input.matches("validate deck \\w+")) {
            System.err.println("validating deck");
            CollectionMenu.checkValidationOfDeck(input.replace("validate deck", "").trim());
            return;
        }
        if (input.matches("select deck \\w+")) {
            System.err.println("selecting deck");
            CollectionMenu.selectAsMainDeck(input.replace("select deck", "").trim());
            return;
        }
        if (input.matches("show deck \\w+")) {
            System.err.println("showing deck");
            CollectionMenu.showDeckByName(input.replace("show deck", "").trim());
            return;
        }
        if (input.matches("show all decks")) {
            System.err.println("showing all decks");
            CollectionMenu.showAllDecks();
            return;
        }
    }

    public static void handleCommandsInMainMenu() {
        String input = scanner.nextLine();
        checkGenerals(input);
    }

    public static void handleCommandsInShop() {
        String input = scanner.nextLine();
        checkGenerals(input);
        if (input.equalsIgnoreCase("help")) {
            System.err.println("showing user it's options");
            System.out.println("commands you can enter :\n" +
                    "-show\n" +
                    "-show collection\n" +
                    "-search cards [card1 name] [card2 name] ...\n" +
                    "-search collection [card1 name] [card2 name] ...\n" +
                    "-buy [card1 name] [card2 name] ...\n" +
                    "-sell [card1 name] [card2 name] ..."
            );
            System.err.println("showing all cards :");
            Shop.showAllCards();
        }
        if (input.equalsIgnoreCase("show")) {
            System.err.println("showing all cards in shop");
            Shop.showAllCards();
        }
        if (input.matches("show collection")) {
            System.err.println("showing all cards in user collection");
            CollectionMenu.showAllMyCards();
        }
        Pattern pattern = Pattern.compile("search ((\\w+\\s*)+)\\s*");
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            System.err.println("searching cards:");
            String[] cardNames = matcher.group(1).trim().split("\\s");
            Shop.searchCardsByNames(cardNames);
        }
        pattern = Pattern.compile("search collection ((\\w+\\s*)+)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            System.err.println("searching cards in collection");
            String[] cardNames = matcher.group(1).trim().split("\\s");
            Shop.searchCardsByNamesInCollection(cardNames);
        }
        pattern = Pattern.compile("buy ((\\w+\\s*)+)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            System.err.println("buying cards");
            String[] cardNames = matcher.group(1).trim().split("\\s");
            Shop.buyCardsByName(cardNames);
        }
        pattern = Pattern.compile("sell ((\\w+\\s*)+)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            System.err.println("selling cards");
            String[] cardNames = matcher.group(1).trim().split("\\s");
            Shop.sellCardsByName(cardNames);
        }
    }

    public static void handleCommandsInBattleMenu() {
        String input = scanner.nextLine();
        checkGenerals(input);
    }

    public static void handleCommandsInLoginMenu() {
        String input = scanner.nextLine();
        checkGenerals(input);
    }

    public void onItemClicked(int id) {
        //This method can be implemented in the presenter, controller, etc.
        //or you can call appropriate methods on them based on the clicked item
        System.out.printf("Item with id: %d was clicked.%n", id);
    }

    public void showMenu(ParentMenu menu) {
        System.out.println(menu.getTitle());
        System.out.println("--------");
        for (int i = 0; i < menu.getSubMenus().size(); i++) {
            Menu item = menu.getSubMenus().get(i);
            System.out.printf("%d. %s%n", i + 1, item.getTitle());
        }
        System.out.println("--enter 'help' to see functions you can run");
        System.out.println("--enter 'back' to go back");
    }
}