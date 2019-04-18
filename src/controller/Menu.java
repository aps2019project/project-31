package controller;

import model.Account;

public abstract class Menu {
    private Account account;
    private Menu lowerMenu;
    private Menu goingTo;

    public abstract void run();

    public abstract void show();

    public abstract void help();

}
