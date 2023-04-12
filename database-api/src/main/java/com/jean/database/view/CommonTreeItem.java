package com.jean.database.view;

import com.jean.database.handler.ActionHandler;
import javafx.scene.control.TreeItem;

public interface CommonTreeItem<T> {

    TreeItem<T> getItem();

    ActionHandler getHandler();

}
