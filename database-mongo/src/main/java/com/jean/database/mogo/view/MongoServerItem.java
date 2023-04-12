package com.jean.database.mogo.view;

import com.jean.database.context.ApplicationContext;
import com.jean.database.item.BaseTreeItem;
import com.jean.database.context.ViewContext;
import com.jean.database.utils.ImageUtils;

public class MongoServerItem extends BaseTreeItem<String> {

    public MongoServerItem(ApplicationContext context, String value) {
        super(context, value, ImageUtils.createImageView("/mongo/mongodb.png"));
    }


}
