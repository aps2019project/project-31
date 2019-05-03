package controller;

import model.Account;

public class Menu {
    private int id;
    private String title;
    private transient ParentMenu parent;
    protected Account account;

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

        public static final int BACK = 5;

        public static final int HELP = 6;

        public static final int EXIT = 7;
    }
}
