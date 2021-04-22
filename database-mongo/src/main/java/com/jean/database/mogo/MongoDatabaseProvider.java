package com.jean.database.mogo;

import com.jean.database.api.DefaultDatabaseProvider;
import com.jean.database.api.utils.ImageUtils;
import com.jean.database.mogo.view.MongoServerItem;
import javafx.scene.control.MenuItem;

public class MongoDatabaseProvider extends DefaultDatabaseProvider {

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


    @Override
    public void init() {
        MenuItem menuItem = new MenuItem(getName(), ImageUtils.createImageView("/mongo/mongodb.png"));
        menuItem.setOnAction(event -> {
            getViewContext().addDatabaseItem(new MongoServerItem(getViewContext(), "mongo[127.0.0.1:21017]"));
        });
        getViewContext().addConnectionMenus(menuItem);
    }


    @Override
    public int getOrder() {
        return 40000;
    }
}
