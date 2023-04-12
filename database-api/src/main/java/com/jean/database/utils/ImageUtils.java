package com.jean.database.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author jinshubao
 * @date 2017/4/8
 */
public final class ImageUtils {

    public static final String LOGO_IMAGE_PATH = "/logo/logo.png";

    public static final Image LOGO_IMAGE = new Image(ImageUtils.class.getResourceAsStream(LOGO_IMAGE_PATH));

    public static ImageView createImageView(String name) {
        return createImageView(name, true);
    }

    public static ImageView createImageView(String name, boolean backgroundLoading) {
        return new ImageView(new Image(name, backgroundLoading));
    }


}
