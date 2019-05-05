package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParentMenu extends Menu {
    private List<Menu> subMenus = new ArrayList<>();

    public ParentMenu(){
        super();
    }
    public ParentMenu(int id, String title)
    {
        super(id, title);
    }

    public void addSubMenu(Menu menu)
    {
        subMenus.add(menu);
        menu.setParent(this);
    }

    public void addSubMenu(int id, String title)
    {
        addSubMenu(new Menu(id, title));
    }

    public List<Menu> getSubMenus()
    {
        return Collections.unmodifiableList(subMenus);
    }

}