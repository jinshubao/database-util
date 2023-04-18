package com.jean.database.api;

import com.jean.database.context.ApplicationContext;
import com.jean.database.controller.AbstractController;

public abstract class DefaultController extends AbstractController {


    public DefaultController(ApplicationContext applicationContext) {
        super(applicationContext);
    }
}
