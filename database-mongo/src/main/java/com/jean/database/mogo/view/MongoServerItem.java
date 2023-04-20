package com.jean.database.mogo.view;

import com.jean.database.utils.ImageUtils;
import com.jean.database.view.AbstractTreeItem;

public class MongoServerItem extends AbstractTreeItem<String> {

    public MongoServerItem(String value) {
        super(value, ImageUtils.createImageView("/mongo/mongodb.png"));
    }


}
