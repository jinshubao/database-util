package com.jean.database.mogo.view;

import com.jean.database.api.BaseTreeItem;
import com.jean.database.api.utils.ImageUtils;

public class MongoServerItem extends BaseTreeItem<String> {

    public MongoServerItem(String value) {
        super(value, ImageUtils.createImageView("/mongo/mongodb.png"));
    }


}
