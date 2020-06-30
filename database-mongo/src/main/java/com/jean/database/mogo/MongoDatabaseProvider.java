package com.jean.database.mogo;

import com.jean.database.api.IDatabaseProvider;
import com.jean.database.api.ViewManger;
import com.jean.database.api.utils.ImageUtils;
import javafx.scene.control.MenuItem;

import java.awt.*;

public class MongoDatabaseProvider implements IDatabaseProvider {

    public static final String NAME = "MongoDB";

    private final String identifier;
    private final String name;

    public MongoDatabaseProvider() {
        this.identifier = NAME;
        this.name = NAME;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getName() {
        return name;
    }

    public void close() {

    }

    @Override
    public void init() {
        MenuItem menuItem = new MenuItem(getName(), ImageUtils.createImageView("/mongo/mongodb.png"));
        menuItem.setOnAction(event -> {

        });
        ViewManger.getViewContext().addConnectionMenus(menuItem);
    }


    @Override
    public int getOrder() {
        return 40000;
    }
}
