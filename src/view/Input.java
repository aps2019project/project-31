package view;

import com.sun.tools.javac.Main;
import controller.BattleMenu;
import controller.Menu;
import controller.MenuManager;
import controller.ParentMenu;
import model.Player;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Input {
    static Scanner scanner = new Scanner(System.in);
    private MenuManager menuManager;

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

    public void start() {
        initMenus();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.matches("\\d+")) // && still in menu
            {
                int index = Integer.parseInt(input) - 1;
                menuManager.performClickOnMenu(index);
            } else if (input.equalsIgnoreCase("back"))
                menuManager.back();
        }
    }

    private void initMenus() {
        ParentMenu mainMenu = new ParentMenu(Menu.Id.MAIN_MENU, "Main Menu");

        ParentMenu loginMenu = new ParentMenu(Menu.Id.LOGIN_MENU, "Login Menu");

        ParentMenu collectionMenu = new ParentMenu(Menu.Id.COLLECTION_MENU, "Collection Menu");

        ParentMenu shopMenu = new ParentMenu(Menu.Id.SHOP_MENU, "Shop Menu");

        ParentMenu battleMenu = new ParentMenu(Menu.Id.BATTLE_MENU, "Battle Menu");

        collectionMenu.addSubMenu(shopMenu);
        collectionMenu.addSubMenu(Menu.Id.BACK, "Back");
        collectionMenu.addSubMenu(new Menu(Menu.Id.HELP, "Help"));

        shopMenu.addSubMenu(collectionMenu);
        shopMenu.addSubMenu(Menu.Id.BACK, "Back");
        shopMenu.addSubMenu(new Menu(Menu.Id.HELP, "Help"));

        battleMenu.addSubMenu(Menu.Id.BACK, "Back");
        battleMenu.addSubMenu(new Menu(Menu.Id.HELP, "Help"));

        mainMenu.addSubMenu(loginMenu);
        mainMenu.addSubMenu(battleMenu);
        mainMenu.addSubMenu(collectionMenu);
        mainMenu.addSubMenu(shopMenu);
        mainMenu.addSubMenu(new Menu(Menu.Id.HELP, "Help"));
        mainMenu.addSubMenu(new Menu(Menu.Id.EXIT, "Exit"));

        menuManager = new MenuManager();
        menuManager.addOnMenuChangeListener(this::showMenu);    //Add listeners - (Method reference)
        menuManager.addOnClickListener(this::onItemClicked);
        menuManager.setCurrentMenu(mainMenu);
    }

    private void onItemClicked(int id) {
        //This method can be implemented in the presenter, controller, etc.
        //or you can call appropriate methods on them based on the clicked item
        System.out.printf("Item with id: %d was clicked.%n", id);
    }

    private void showMenu(ParentMenu menu) {
        System.out.println(menu.getTitle());
        System.out.println("--------");
        for (int i = 0; i < menu.getSubMenus().size(); i++) {
            Menu item = menu.getSubMenus().get(i);
            System.out.printf("%d. %s%n", i + 1, item.getTitle());
        }
    }
}