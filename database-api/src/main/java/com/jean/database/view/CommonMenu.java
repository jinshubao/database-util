package com.jean.database.view;

import com.jean.database.handler.ActionHandler;
import javafx.scene.control.ContextMenu;

public interface CommonMenu {

    ContextMenu getMenu();

    ActionHandler getHandler();
}
