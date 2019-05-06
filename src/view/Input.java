package view;

import constants.BattleManagerMode;
import constants.GameMode;
import controller.*;
import model.*;

import java.security.PublicKey;
import java.util.Collections;
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

    public static void setCurrentMenu(ParentMenu currentMenu) {
        menuManager.setCurrentMenu(currentMenu);
    }

    public static String giveMeInput() {
        return scanner.nextLine();
    }

    public static void handleSelectComboCards(String input) {

        Pattern pattern = Pattern.compile("attack combo (\\d+)\\s+((\\d\\s*)+)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            int opponentCardId = Integer.parseInt(matcher.group(1));
            String[] strNumbers = matcher.group(2).split(",");
            BattleMenu.prepareComboAttack(strNumbers, opponentCardId);
        }

    }

    public static void moveAttackPlayCard(String input) {
        if (input.matches("\\s*end turn\\s*"))
            BattleMenu.setAreWeInMiddleOfTurn(false);
        if (input.matches("attack\\s+\\d+")) {
            BattleMenu.attack(Integer.parseInt(input.replace("attack ", "").trim()));
        }
        Pattern pattern = Pattern.compile("move to \\((\\d),(\\d)\\)\\s*");
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            BattleMenu.move(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
        }
        pattern = Pattern.compile("insert in \\((\\d),(\\d)\\)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            BattleMenu.insert(BattleMenu.getBattleManager().getCurrentPlayer().getSelectedCard(),
                    Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
        }
        BattleMenu.getBattleManager().checkTheEndSituation();
    }

    public static void start() {
        MenuManager.initMenus();
        System.err.println("MenuManager initialized");
        Input.getInstance().showMenu(menuManager.getCurrentMenu());
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
                case Menu.Id.SINGLE_PLAYER_MENU:
                    handleCommandsInSinglePlayerMenu();
                    break;
                case Menu.Id.MULTI_PLAYER_MENU:
                    handleCommandsInMultiPlayerMenu();
                    break;
                case Menu.Id.SINGLE_PLAYER_CUSTOM_MENU:
                    handleCommandsInSinglePlayerCustomMenu();
                    break;
                case Menu.Id.SINGLE_PLAYER_STORY_MENU:
                    handleCommandsInSinglePlayerStoryMenu();
                    break;
            }
            Input.getInstance().showMenu(menuManager.getCurrentMenu());
        }
    }

    private static boolean checkGenerals(String input) {
        if (input.matches("\\s*\\d+\\s*")) {
            System.err.println("user entered a number");
            int index = Integer.parseInt(input) - 1;
            menuManager.performClickOnMenu(index);
        } else if (input.equalsIgnoreCase("back")) {
            if (menuManager.getCurrentMenu().getId() == Menu.Id.MAIN_MENU) {
                MenuManager.exitGame();
            }
            menuManager.back();
            return false;
        }
        return true;
    }

    public static void handleCommandsInBattle(Player player, boolean isThereSelectedCard) {
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("help")){
            System.err.println("showing user it's options");
            System.out.println("commands you can enter :\n" +
                    "replace card [card id]\n" +
                    "end turn\n" +
                    "select [card id]\n" +
                    "show mana\n" +
                    "game info\n" +
                    "show my minions\n" +
                    "show opponent minions\n" +
                    "show card info [card id]\n" +
                    "use special power (x,y)\n" +
                    "show hand\n" +
                    "show collectibles\n" +
                    "show info\n" +
                    "show next card\n" +
                    "enter graveyard"
            );
        }
        if(input.equalsIgnoreCase("glimpse of map")){
            BattleMenu.showGlimpseOfMap(BattleMenu.getBattleManager());
        }
        if (input.matches("replace card (\\d+)\\s*")) {
            int cardId = Integer.parseInt(input.replace("replace card ", "").trim());
            BattleMenu.replaceCardInHand(cardId);
        }

        if (input.matches("\\s*end turn\\s*"))
            BattleMenu.setAreWeInMiddleOfTurn(false);
        Pattern pattern = Pattern.compile("select (\\d+)\\s*");
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()){
            if (input.matches("select \\d+")) {
                player.selectACard(Integer.parseInt(input.replace("select ", "").trim()));
                BattleMenu.selectCollectibleItem(Integer.parseInt(matcher.group(1)));
            }
        }
        if (isThereSelectedCard)
            moveAttackPlayCard(input);
        else
            handleSelectComboCards(input);
        if (input.equalsIgnoreCase("show mana"))
            System.out.println("your mana is:  " + BattleMenu.getBattleManager().getCurrentPlayer().getMana());
        if (input.equalsIgnoreCase("game info"))
            BattleMenu.showGameInfo();
        else if (input.trim().equalsIgnoreCase("show my minions")) {
            BattleMenu.showPlayerMinions(player);
        } else if (input.trim().equalsIgnoreCase("show opponent minions"))
            BattleMenu.showPlayerMinions(BattleMenu.getBattleManager().getOtherPlayer());
        else if (input.matches("show card info \\d+")) {
            String cardUniqueId = input.replace("show card info", "").trim();
            System.out.println(BattleManager.findCardByUniqueid(Integer.parseInt(cardUniqueId)).infoToString());
        }
        pattern = Pattern.compile("use special power \\((\\d+),(\\d+)\\)");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            int x1 = Integer.parseInt(matcher.group(1));
            int x2 = Integer.parseInt(matcher.group(2));
            BattleMenu.getBattleManager().playSpell(player.getHero().getHeroSpell(), x1, x2);
        }
        if (input.equalsIgnoreCase("show hand"))
            player.showHand();
        else if (input.equalsIgnoreCase("show collectibles")) {
            BattleMenu.getBattleManager().getCurrentPlayer().showCollectibleItems();
        }

        if (input.equalsIgnoreCase("show info"))
            BattleMenu.showSelectedCardInfo();
        else if (input.equalsIgnoreCase("show next card")) {
            BattleMenu.getBattleManager().getCurrentPlayer().showNextCard();
        } else if (input.equalsIgnoreCase("enter graveyard"))
            enterGraveYard();
        BattleMenu.getBattleManager().checkTheEndSituation();
    }

    private static void enterGraveYard() {
        while (true) {
            String input = scanner.nextLine();
            if(input.equalsIgnoreCase("help")){
                System.err.println("showing user it's options");
                System.out.println("commands you can enter :\n" +
                        "-show cards\n" +
                        "-show info [card id]\n" +
                        "-exit"
                );
            }
            if (input.equalsIgnoreCase("show cards"))
                showAllGraveyardCards();
            Pattern pattern = Pattern.compile("show info (\\d+)");
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                System.out.println(BattleMenu.findDeadMinion(Integer.parseInt(matcher.group(1))).infoToString());
            }
            if (input.equalsIgnoreCase("exit"))
                return;
        }
    }

    public static void showAllGraveyardCards() {
        System.out.println("Player 1 dead minions:\n");
        for (Deployable card : BattleMenu.getBattleManager().getPlayer1().getGraveYard()) {
            System.out.println(card.infoToString());
        }
    }

    public static void handleCommandsInCollectionMenu() {
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("enter shop menu")) {
            setCurrentMenu(MenuManager.getShopMenu());
            return;
        }
        if (input.equalsIgnoreCase("back")) {
            menuManager.back();
            return;
        }
        if (input.equalsIgnoreCase("help")) {
            System.err.println("showing user it's options");
            System.out.println("commands you can enter :\n" +
                    "-show\n" +
                    "-save\n" +
                    "-search [card1 name],[card2 name],[...\n" +
                    "-create deck [deck name]\n" +
                    "-delete deck [deck name]\n" +
                    "-add [card1 id],[card2 id],[...] to deck [deck name]\n" +
                    "-remove [card1 id],[card2 id],[...] from deck [deck name]\n" +
                    "-validate deck [deck name]\n" +
                    "-select deck [deck name]\n" +
                    "-show deck [deck name]\n" +
                    "-show all decks\n"
            );
            return;
        }
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
        Pattern pattern = Pattern.compile("search (.+,*+)\\s*");
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            System.err.println("searching in collection...");
            String[] names = matcher.group(1).trim().split(",");
            System.err.println("founded cards :");
            CollectionMenu.showCardsByNames(names);
            return;
        }
        pattern = Pattern.compile("create deck (\\w+)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            System.err.println("creating deck");
            Account.getMainAccount().createDeck(matcher.group(1));
            System.err.println("deck '" + matcher.group(1) + "' created");
            return;
        }
        pattern = Pattern.compile("delete deck (\\w+)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            System.err.println("deleting deck");
            Account.getMainAccount().deleteDeck(matcher.group(1));
            System.err.println("deck '" + matcher.group(1) + "' deleted");
            return;
        }
        pattern = Pattern.compile("add ((\\d+,*)+) to deck (\\w+)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            System.err.println("adding cards to deck");
            String[] numbers = matcher.group(1).trim().split(",");
            Account.getMainAccount().addCardsToDeck(numbers, matcher.group(3).trim());
            System.err.println("adding cards finished");
            return;
        }
        pattern = Pattern.compile("remove ((\\d+,*)+) from deck (\\w+)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            System.err.println("removing card from deck");
            String[] numbers = matcher.group(1).trim().split(",");
            Account.getMainAccount().removeCardsFromDeck(numbers, matcher.group(3).trim());
            System.err.println("removed");
            return;
        }
        if (input.matches("validate deck \\w+")) {
            System.err.println("validating deck");
            Account.getMainAccount().checkValidationOfDeck(input.replace("validate deck", "").trim());
            return;
        }
        if (input.matches("select deck \\w+")) {
            System.err.println("selecting deck");
            Account.getMainAccount().selectAsMainDeck(input.replace("select deck", "").trim());
            return;
        }
        if (input.matches("show deck \\w+")) {
            System.err.println("showing deck");
            Account.getMainAccount().showDeckByName(input.replace("show deck", "").trim());
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
        if (input.equalsIgnoreCase("enter login menu")) {
            setCurrentMenu(MenuManager.getLoginMenu());
            return;
        }
        if (input.equalsIgnoreCase("enter battle menu")) {
            if (Account.getMainAccount().getTheMainDeck() != null) {
                if (Account.getMainAccount().getTheMainDeck().checkIfValid()) {
                    setCurrentMenu(MenuManager.getBattleMenu());
                } else {
                    System.out.println("selected deck is invalid");
                }
            } else {
                System.err.println("no deck!");
                System.out.println("no deck!");
            }
            return;
        }
        if (input.equalsIgnoreCase("enter collection menu")) {
            setCurrentMenu(MenuManager.getCollectionMenu());
            return;
        }
        if (input.equalsIgnoreCase("enter shop menu")) {
            setCurrentMenu(MenuManager.getShopMenu());
            return;
        }
        if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("back")) {
            MenuManager.exitGame();
            return;
        }
    }

    public static void handleCommandsInShop() {
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("enter collection menu")) {
            setCurrentMenu(MenuManager.getCollectionMenu());
            return;
        }
        if (input.equalsIgnoreCase("back")) {
            menuManager.back();
            return;
        }
        if (input.equalsIgnoreCase("help")) {
            System.err.println("showing user it's options");
            System.out.println("commands you can enter :\n" +
                    "-show\n" +
                    "-show collection\n" +
                    "-search cards [card1 name],[card2 name],[...\n" +
                    "-search collection [card1 name],[card2 name],[...\n" +
                    "-buy [card1 name],[card2 name],[...\n" +
                    "-sell [card1 name],[card2 name],[..."
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
        Pattern pattern = Pattern.compile("search (.+,*)+\\s*");
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            System.err.println("searching cards:");
            String[] cardNames = matcher.group(1).trim().split(",");
            Shop.searchCardsByNames(cardNames);
        }
        pattern = Pattern.compile("search collection (.+,*)+\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            System.err.println("searching cards in collection");
            String[] cardNames = matcher.group(1).trim().split(",");
            Shop.searchCardsByNamesInCollection(cardNames);
        }
        pattern = Pattern.compile("buy (.+,*)+\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            System.err.println("buying cards");
            String[] cardNames = matcher.group(1).trim().split(",");
            Shop.buyCardsByName(cardNames);
        }
        pattern = Pattern.compile("sell (.+,*)+\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            System.err.println("selling cards");
            String[] cardNames = matcher.group(1).trim().split(",");
            Shop.sellCardsByName(cardNames);
        }
    }

    private static void handleCommandsInMultiPlayerMenu() {
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("back")) {
            menuManager.back();
            return;
        }
        if (input.equalsIgnoreCase("help")) {
            System.err.println("showing user it's options");
            System.out.println("commands you can enter :\n" +
                    "select user [username] with deck [deck name] and game mode [game mode] [number of flags/max number" +
                    "x of turn having flag (optional)]\n"
            );
        }
        Pattern pattern = Pattern.compile("select user (\\w+) with deck (\\w+) and game mode (\\w+)\\s*(\\d+)*");
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            Account theAccount = Account.findAccount(matcher.group(1));
            if (theAccount == null) {
                System.err.println("the user does not exist!");
                return;
            }
            Deck thePlayer2Deck = theAccount.findDeckByName(matcher.group(2));
            if (thePlayer2Deck == null) {
                System.err.println("the deck" +
                        matcher.group(2) +
                        "wasn't found");
                return;
            }
            Account.getMainAccount().checkValidationOfDeck(thePlayer2Deck.getDeckName());
            if (thePlayer2Deck.checkIfValid())
                theAccount.setTheMainDeck(thePlayer2Deck);
            if (matcher.group(3).trim().equalsIgnoreCase("domination")) {
                BattleMenu.setBattleManagerForMultiPlayer(Account.getMainAccount(), theAccount,
                        Integer.parseInt(matcher.group(4)), 100, GameMode.Domination);
                BattleMenu.runTheGame();
            }
            if (matcher.group(3).trim().equalsIgnoreCase("flag")) {
                BattleMenu.setBattleManagerForMultiPlayer(Account.getMainAccount(), theAccount,
                        100, Integer.parseInt(matcher.group(4)), GameMode.Flag);
                BattleMenu.runTheGame();
            }
            if (matcher.group(3).trim().equalsIgnoreCase("deathMatch")) {
                BattleMenu.setBattleManagerForMultiPlayer(Account.getMainAccount(), theAccount,
                        100, 100, GameMode.DeathMatch);
                BattleMenu.runTheGame();
            }
        }
    }

    private static void handleCommandsInSinglePlayerMenu() {
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("enter single player story menu") || input.equalsIgnoreCase("enter story menu")) {
            System.err.println("showing user it's options");
            System.out.println("commands you can enter :\n" +
                    "-enter 'story 1' :: Hero : White Div mode: DeathMatch\n" +
                    "-enter 'story 2' :: Hero : Zah'aak   mode: Flag\n" +
                    "-enter 'story 3' :: Hero : Arash     mode: Domination"
            );
            setCurrentMenu(MenuManager.getSinglePlayerStoryMenu());
            return;
        }
        if (input.equalsIgnoreCase("enter custom menu") || input.equalsIgnoreCase("enter single player custom menu")) {
            setCurrentMenu(MenuManager.getSinglePlayerCustomMenu());
            return;
        }
        if (input.equalsIgnoreCase("back")) {
            menuManager.back();
            return;
        }
        if (input.equalsIgnoreCase("help")) {
            System.err.println("showing user it's options");
            System.out.println("commands you can enter :\n" +
                    ""
            );
        }
    }

    public static void handleCommandsInSinglePlayerStoryMenu() {
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("back")) {
            menuManager.back();
            return;
        }
        if (input.equalsIgnoreCase("help")) {
            System.err.println("showing user it's options");
            System.out.println("commands you can enter :\n" +
                    "-enter 'story 1' :: Hero : White Div mode: DeathMatch\n" +
                    "-enter 'story 2' :: Hero : Zah'aak   mode: Flag\n" +
                    "-enter 'story 3' :: Hero : Arash     mode: Domination"
            );
        }

        if (input.equalsIgnoreCase("story 1")) {
            BattleMenu.setBattleManagerForSinglePLayer(BattleManagerMode.Story, Account.getMainAccount(), 100,
                    100, GameMode.DeathMatch, 1);
            BattleMenu.runTheGame();
        }
        if (input.equalsIgnoreCase("story 2")) {
            BattleMenu.setBattleManagerForSinglePLayer(BattleManagerMode.Story, Account.getMainAccount(), 100,
                    100, GameMode.DeathMatch, 2);
            BattleMenu.runTheGame();
        }
        if (input.equalsIgnoreCase("story 3")) {
            BattleMenu.setBattleManagerForSinglePLayer(BattleManagerMode.Story, Account.getMainAccount(), 100,
                    100, GameMode.DeathMatch, 3);
            BattleMenu.runTheGame();
        }
    }

    public static void handleCommandsInSinglePlayerCustomMenu() {
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("back")) {
            menuManager.back();
            return;
        }
        if (input.equalsIgnoreCase("help")) {
            System.err.println("showing user it's options");
            System.out.println("commands you can enter :\n" +
                    "Start game [mode] [number of flags]"
            );
        }
        Pattern pattern = Pattern.compile("start game (\\w+)\\s*(\\d+)*");
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            try {
                if (matcher.group(3).trim().equalsIgnoreCase("domination")) {
                    BattleMenu.setBattleManagerForSinglePLayer(BattleManagerMode.CustomGame, Account.getMainAccount(),
                            Integer.parseInt(matcher.group(4)), 100, GameMode.Domination, 1);
                    BattleMenu.runTheGame();
                }
                if (matcher.group(3).trim().equalsIgnoreCase("flag")) {
                    BattleMenu.setBattleManagerForSinglePLayer(BattleManagerMode.CustomGame, Account.getMainAccount(),
                            100, Integer.parseInt(matcher.group(4)), GameMode.Flag, 1);
                    BattleMenu.runTheGame();
                }
                if (matcher.group(3).trim().equalsIgnoreCase("deathMatch")) {
                    BattleMenu.setBattleManagerForSinglePLayer(BattleManagerMode.CustomGame, Account.getMainAccount(),
                            100, 100, GameMode.DeathMatch, 1);
                    BattleMenu.runTheGame();
                }
            } catch (Exception e) {
                System.err.println("custom game error probably for null pointer exception");
            }

        }
    }

    public static void handleCommandsInBattleMenu() {
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("enter single player menu")) {
            setCurrentMenu(MenuManager.getSinglePlayerMenu());
            return;
        }
        if (input.equalsIgnoreCase("enter multi player menu")) {
            setCurrentMenu(MenuManager.getMultiPlayerMenu());
            return;
        }
        if (input.equalsIgnoreCase("back")) {
            menuManager.back();
            return;
        }
    }

    public static void handleCommandsInLoginMenu() {
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("enter main menu")) {
            if (Account.getMainAccount() == null) {
                System.err.println("user have not logged in yet");
                return;
            }
            setCurrentMenu(MenuManager.getMainMenu());
            return;
        }
        if (input.equalsIgnoreCase("help")) {
            System.err.println("showing user it's options");
            System.out.println("commands you can enter :\n" +
                    "-create account [username]\n" +
                    "-login [username]\n" +
                    "-logout\n" +
                    "-save\n" +
                    "-show leaderBoard\n" +
                    "-exit"
            );
        }
        if (input.equalsIgnoreCase("back")) {
            return;
        }
        if (input.equalsIgnoreCase("exit")) {
            MenuManager.exitGame();
            return;
        }
        Pattern pattern = Pattern.compile("create account (\\w+)\\s*");
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            if(Account.getMainAccount() != null){
                System.out.println("you are already logged in,log out to create account");
                return;
            }
            Output.print("enter your password:");
            String username = matcher.group(1).trim();
            String password = scanner.nextLine();
            System.err.println("checking..");
            if (Account.findAccount(username) != null) {
                System.out.println("this username is already taken");
                return;
            }
            Account account = Account.createAccount(username, password.trim());
            Account.setMainAccount(account);
            System.err.println("account created");
            menuManager.back();
            return;
        }
        pattern = Pattern.compile("login (\\w+)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            System.out.println("enter password");
            String password = scanner.nextLine();
            String username = matcher.group(1).trim();
            Account account = Account.findAccount(username);
            if (account == null) {
                System.out.println("account does not exist!");
                return;
            }
            if (account.getPassword().equals(password)) {
                System.out.println("login successful");
                Account.setMainAccount(account);
                return;
            } else {
                System.out.println("incorrect password");
                return;
            }
        }
        if (input.equalsIgnoreCase("logout")) {
            Account.setMainAccount(null);
            return;
        }
        if (input.equalsIgnoreCase("save")) {
            //masih!?
            return;
        }
        if (input.equalsIgnoreCase("show leaderBoard")) {
            Output.showLeaderBoard();
            return;
        }
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