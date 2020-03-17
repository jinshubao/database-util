package com.jean.database.core.meta;

/**
 * 键值对信息
 *
 * @author jinshubao
 */
public class KeyValuePairData {

    private Object name;

    private Object value;

    public KeyValuePairData(Object name, Object value) {
        this.name = name;
        this.value = value;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
