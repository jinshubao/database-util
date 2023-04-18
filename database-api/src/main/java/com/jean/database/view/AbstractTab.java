package com.jean.database.view;

import com.jean.database.context.ApplicationContext;
import javafx.scene.Node;
import javafx.scene.control.Tab;

public abstract class AbstractTab extends Tab {

    private final ApplicationContext context;

    public AbstractTab(ApplicationContext context, String text) {
        this(context, text, null, null, true);
    }

    public AbstractTab(ApplicationContext context, String text, Node graphic) {
        this(context, text, graphic, null, true);
    }

    public AbstractTab(ApplicationContext context, String text, Node graphic, Node content, boolean isClosable) {
        super(text, content);
        this.setGraphic(graphic);
        this.setClosable(isClosable);
        this.context = context;
    }


    public ApplicationContext getContext() {
        return context;
    }
}
