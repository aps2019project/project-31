package controller;

import org.graalvm.compiler.replacements.Log;

import java.util.ArrayList;
import java.util.List;

public class MenuManager {
    private ParentMenu currentMenu;
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

    public void performClickOnMenu(int index) {
        if (index >= currentMenu.getSubMenus().size() || index < 0) {
            Log.println("user clicked out of range");
            return;
        }
        if(currentMenu.getSubMenus().get(index).getId() == Menu.Id.BACK){
            back();
            return;
        }
        if(currentMenu.getSubMenus().get(index).getId() == Menu.Id.EXIT){
            exitGame();
            return;
        }
        Menu clickedMenu = currentMenu.getSubMenus().get(index);
        if (clickedMenu instanceof ParentMenu)
            setCurrentMenu((ParentMenu) clickedMenu);
        else
            callOnClickListeners(clickedMenu.getId());
    }

    private void exitGame() {
        //do before closing stuff
        Log.println("closing the program... bye bye");
        System.exit(0);
    }

    public void back() {
        setCurrentMenu(this.currentMenu.getParent());
    }
}
