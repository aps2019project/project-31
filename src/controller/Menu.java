package controller;

import model.Account;

public abstract class Menu {
    protected static Account account;


    public abstract void run();

    public abstract void show();

    public abstract void help();

}
