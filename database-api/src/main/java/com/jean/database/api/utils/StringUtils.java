package com.jean.database.api.utils;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class StringUtils {

    public static boolean isNotBlank(String text) {
        return !isBlank(text);
    }

    public static boolean isBlank(String text) {
        return text == null || text.length() == 0;
    }

    public static String join(List<String> collection, String sp) {
        if (collection != null) {
            StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < collection.size(); i++) {
                buffer.append(collection.get(i));
                if (i < collection.size() - 1) {
                    buffer.append(sp);
                }
            }
            return buffer.toString();
        }
        return null;
    }

    public static String toPath(String... dirs) {
        List<String> list = new ArrayList<>();
        for (String dir : dirs) {
            if (isNotBlank(dir)) {
                if (dir.startsWith(File.separator)) {
                    dir = dir.substring(1);
                }
                if (dir.endsWith(File.separator)) {
                    dir = dir.substring(0, dir.length() - 2);
                }
                list.add(dir);
            }
        }
        return join(list, File.separator);
    }

    public static String byteArrayToString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static String byteArrayToString(byte[] bytes, Charset charset) {
        return new String(bytes, charset);
    }
}
