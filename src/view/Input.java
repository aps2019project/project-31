package view;

import controller.*;
import com.sun.tools.javac.Main;
import controller.BattleMenu;
import controller.CollectionMenu;
import controller.Menu;
import controller.MenuManager;
import controller.ParentMenu;
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

    public static MenuManager getMenuManager() {
        return menuManager;
    }

    public static void setMenuManager(MenuManager menuManager) {
        Input.menuManager = menuManager;
    }

    private static MenuManager menuManager = new MenuManager();

    public static void handleSelectCardOrSelectComboCards(Player player) {
        String input = scanner.nextLine();
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
        String input = scanner.nextLine();
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
        if (input.matches("\\d+"))
        {
            System.err.println("you entered a number");
            int index = Integer.parseInt(input) - 1;
            menuManager.performClickOnMenu(index);
        } else if (input.equalsIgnoreCase("back"))
            menuManager.back();
    }
    public static void handleCommandsInCollectionMenu() {
        String input = scanner.nextLine();
        checkGenerals(input);
        if (input.matches("exit\\s*")) {
            CollectionMenu.back();
            return;
        }
        if (input.matches("show\\s*")) {
            CollectionMenu.showAllMyCards();
            return;
        }
        if (input.matches("save\\s*")) {
            CollectionMenu.saveAndGoBack();
            return;
        }
        if (input.matches("help")) {
            //CollectionMenu.help();
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
            CollectionMenu.addCardsToDeck(numbers, matcher.group(3).trim());
        }
        pattern = Pattern.compile("remove ((\\d+\\s*)+)from deck (\\w+)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            String[] numbers = matcher.group(1).trim().split("\\s");
            CollectionMenu.removeCardsFromDeck(numbers, matcher.group(3).trim());
        }
        if (input.matches("validate deck \\w+"))
            CollectionMenu.checkValidationOfDeck(input.replace("validate deck", "").trim());
        if (input.matches("select deck \\w+")) {
            CollectionMenu.selectAsMainDeck(input.replace("select deck", "").trim());
        }
        if (input.matches("show all decks"))
            CollectionMenu.showAllDecks();
        if (input.matches("show deck \\w+"))
            CollectionMenu.showDeckByName(input.replace("show deck", "").trim());


    }

    public static void handleCommandsInMainMenu(){
        String input = scanner.nextLine();
        checkGenerals(input);
    }

    public static void handleCommandsInShop() {
        String input = scanner.nextLine();
        checkGenerals(input);
        if (input.matches("exit"))
            Shop.goBack();
        if (input.matches("show collection"))
            CollectionMenu.showAllMyCards();
        Pattern pattern = Pattern.compile("search ((\\w+\\s*)+)\\s*");
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            String[] cardNames = matcher.group(1).trim().split("\\s");
            Shop.searchCardsByNames(cardNames);
        }
        pattern = Pattern.compile("search collection ((\\w+\\s*)+)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            String[] cardNames = matcher.group(1).trim().split("\\s");
            Shop.searchCardsByNamesInCollection(cardNames);
        }
        pattern = Pattern.compile("buy ((\\w+\\s*)+)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            String[] cardNames = matcher.group(1).trim().split("\\s");
            Shop.buyCardsByName(cardNames);
        }
        pattern = Pattern.compile("sell ((\\w+\\s*)+)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            String[] cardNames = matcher.group(1).trim().split("\\s");
            Shop.sellCardsByName(cardNames);
        }
        if (input.matches("show"))
            Shop.showAllCards();
        if (input.matches("help")) {
//            Shop.help();
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
    }
}