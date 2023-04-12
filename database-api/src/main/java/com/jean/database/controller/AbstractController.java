package com.jean.database.controller;

import com.jean.database.context.ApplicationContext;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class AbstractController implements IController, Initializable {


    private final ApplicationContext applicationContext;

    public AbstractController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public ApplicationContext getContext() {
        return applicationContext;
    }
}
