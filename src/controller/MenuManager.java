package controller;

import view.Input;

import java.util.ArrayList;
import java.util.List;

public class MenuManager {
    private ParentMenu currentMenu;

    private static ParentMenu mainMenu;

    private static ParentMenu loginMenu;
    private static ParentMenu battleMenu;
    private static ParentMenu shopMenu;
    private static ParentMenu collectionMenu;
    private static ParentMenu singlePlayerMenu;
    private static ParentMenu singlePlayerCustomMenu;
    private static ParentMenu singlePlayerStoryMenu;
    private static ParentMenu multiPlayerMenu;
    private static ParentMenu battle;

    public static ParentMenu getMainMenu() {
        return mainMenu;
    }

    private List<OnMenuItemClickListener> clickListeners = new ArrayList<>();
    private List<OnMenuChangeListener> menuChangeListeners = new ArrayList<>();

    public ParentMenu getCurrentMenu() {
        return currentMenu;
    }

    public void setCurrentMenu(ParentMenu currentMenu) {
        this.currentMenu = currentMenu;
        callOnMenuChangeListeners();
    }

    public void addOnClickListener(OnMenuItemClickListener listener) {
        clickListeners.add(listener);
    }

    private void callOnClickListeners(int menuId) {
        clickListeners.forEach(listener -> listener.onMenuItemClicked(menuId));
    }

    public void addOnMenuChangeListener(OnMenuChangeListener listener) {
        menuChangeListeners.add(listener);
    }

    private void callOnMenuChangeListeners() {
        menuChangeListeners.forEach(listener -> listener.onMenuChanged(currentMenu));
    }

    public static void initMenus() {
        mainMenu = new ParentMenu(Menu.Id.MAIN_MENU, "Main Menu");

        loginMenu = new ParentMenu(Menu.Id.LOGIN_MENU, "Login Menu");

        collectionMenu = new ParentMenu(Menu.Id.COLLECTION_MENU, "Collection Menu");

        shopMenu = new ParentMenu(Menu.Id.SHOP_MENU, "Shop Menu");

        battleMenu = new ParentMenu(Menu.Id.BATTLE_MENU, "Battle Menu");
        battle = new ParentMenu(Menu.Id.BACK, "Battle");

        singlePlayerMenu = new ParentMenu(Menu.Id.SINGLE_PLAYER_MENU, "Single Player Menu");
        singlePlayerCustomMenu = new ParentMenu(Menu.Id.SINGLE_PLAYER_CUSTOM_MENU, "Single Player Custom Menu");
        singlePlayerStoryMenu = new ParentMenu(Menu.Id.SINGLE_PLAYER_STORY_MENU, "Single Player Story Menu");

        multiPlayerMenu = new ParentMenu(Menu.Id.MULTI_PLAYER_MENU, "Multi Player Menu");

        battleMenu.addSubMenu(singlePlayerMenu);
        battleMenu.addSubMenu(multiPlayerMenu);

        battleMenu.addSubMenu(battle);
        collectionMenu.addSubMenu(shopMenu);

        shopMenu.addSubMenu(collectionMenu);

        singlePlayerMenu.addSubMenu(singlePlayerCustomMenu);
        singlePlayerMenu.addSubMenu(singlePlayerStoryMenu);

        mainMenu.addSubMenu(loginMenu);
        mainMenu.addSubMenu(battleMenu);
        mainMenu.addSubMenu(collectionMenu);
        mainMenu.addSubMenu(shopMenu);

        Input.setMenuManager(new MenuManager());
        Input.getMenuManager().addOnMenuChangeListener(Input.getInstance()::showMenu);    //Add listeners - (Method reference)
        Input.getMenuManager().addOnClickListener(Input.getInstance()::onItemClicked);
        Input.getMenuManager().setCurrentMenu(mainMenu);
    }

    public void performClickOnMenu(int index) {
        if (index >= currentMenu.getSubMenus().size() || index < 0) {
            System.err.println("user clicked out of range");
            return;
        }
        if (currentMenu.getSubMenus().get(index).getId() == Menu.Id.EXIT) {
            exitGame();
            return;
        }
        Menu clickedMenu = currentMenu.getSubMenus().get(index);
        if (clickedMenu instanceof ParentMenu)
            setCurrentMenu((ParentMenu) clickedMenu);
        else
            callOnClickListeners(clickedMenu.getId());
    }

    public static void exitGame() {
        //do before closing stuff
        System.err.println("closing the program... bye bye (*^ - ^*)");
        System.exit(0);
    }

    public void back() {
        System.err.println("getting back...");
        setCurrentMenu(this.currentMenu.getParent());
    }
}
