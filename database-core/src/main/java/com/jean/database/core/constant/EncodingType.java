package com.jean.database.core.constant;

import java.nio.charset.Charset;

/**
 *
 * @author jinshubao
 * @date 2017/4/9
 */
public enum EncodingType {

    UTF8("UTF-8", Charset.forName("UTF-8")),
    UTF16("UTF-16", Charset.forName("UTF-16")),
    GBK("GBK", Charset.forName("GBK"));

    public String name;
    public Charset value;

    EncodingType(String name, Charset value) {
        this.value = value;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
