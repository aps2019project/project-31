package controller;

import model.Account;

public class Menu {
    protected int id;
    protected String title;
    protected transient ParentMenu parent;

    public Menu(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public Menu() {

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ParentMenu getParent() {
        return parent;
    }

    void setParent(ParentMenu parent) {
        this.parent = parent;
    }

    public static class Id {
        public static final int MAIN_MENU = 0;
        public static final int LOGIN_MENU = 1;
        public static final int COLLECTION_MENU = 2;
        public static final int SHOP_MENU = 3;
        public static final int BATTLE_MENU = 4;
        public static final int SINGLE_PLAYER_MENU = 5;
        public static final int MULTI_PLAYER_MENU = 6;
        public static final int SINGLE_PLAYER_CUSTOM_MENU = 7;
        public static final int SINGLE_PLAYER_STORY_MENU = 8;
        public static final int BACK = 9;

        public static final int HELP = 10;

        public static final int EXIT = 11;
    }
}
