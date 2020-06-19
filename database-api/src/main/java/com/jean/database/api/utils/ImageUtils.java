package com.jean.database.api.utils;

import javafx.scene.image.Image;

/**
 * @author jinshubao
 * @date 2017/4/8
 */
public class ImageUtils {

    public static final String LOGO_IMAGE_PATH = "/logo/logo.png";

    public static final Image LOGO_IMAGE = new Image(ImageUtils.class.getResourceAsStream(LOGO_IMAGE_PATH));

}
