package com.jean.database.api;

import com.jean.database.api.view.ViewContext;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;

public interface IDatabaseProvider {

    void init(ViewContext viewContext);

    String getIdentifier();

    String getName();

    String getIcon();

    void close();

}
