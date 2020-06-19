package com.jean.database.sql.view;

import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;

public class SQLDDLInfoTab extends Tab {

    private final TextArea textArea;

    public SQLDDLInfoTab(String text) {
        super(text);
        textArea = new TextArea();
        setContent(textArea);
    }

    public void setInfoText(String text) {
        textArea.setText(text);
    }

}
