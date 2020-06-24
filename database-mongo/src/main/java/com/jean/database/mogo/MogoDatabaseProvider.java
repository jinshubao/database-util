package com.jean.database.mogo;

import com.jean.database.api.AbstractDatabaseProvider;
import com.jean.database.api.ViewManger;
import javafx.scene.control.MenuItem;

public class MogoDatabaseProvider extends AbstractDatabaseProvider {

    public static final String name = "MongoDB";

    @Override
    public String getIdentifier() {
        return name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void init() {
        super.init();
        MenuItem menuItem = new MenuItem(getName());
        menuItem.setOnAction(event -> {

        });
        ViewManger.getViewContext().addConnectionMenus(menuItem);
    }


    @Override
    public int getOrder() {
        return 40000;
    }
}
