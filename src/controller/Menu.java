package controller;

import model.Account;

public abstract class Menu {
    protected Account account;
    protected Menu lowerMenu;
    protected Menu goingTo;

    public abstract void run();

    public abstract void show();

    public abstract void help();

}
