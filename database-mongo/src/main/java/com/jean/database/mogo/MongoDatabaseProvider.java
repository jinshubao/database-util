package com.jean.database.mogo;

import com.jean.database.context.ApplicationContext;
import com.jean.database.provider.DefaultDatabaseProvider;
import com.jean.database.utils.ImageUtils;
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
    public void init(ApplicationContext context) {
        super.init(context);
        MenuItem menuItem = new MenuItem(getName(), ImageUtils.createImageView("/mongo/mongodb.png"));
        menuItem.setOnAction(event -> {
//            context.addDatabaseItem(new MongoServerItem(context, "mongo[127.0.0.1:21017]"));
        });
        context.addConnectionMenus(menuItem);
    }


    @Override
    public int getOrder() {
        return 40000;
    }
}
