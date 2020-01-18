package com.jean.database.client.controller;

import com.jean.database.client.utils.DialogUtil;
import javafx.fxml.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;


/**
 * @author jinshubao
 * @date 2017/4/8
 */
abstract class BaseController implements Initializable {

    final Logger logger = LoggerFactory.getLogger(this.getClass());


    void showExceptionDialog(ResourceBundle resources, Throwable e) {
        logger.error(e.getMessage(), e);
        DialogUtil.error(resources.getString("dialog.exception.title"), e);
    }
}
