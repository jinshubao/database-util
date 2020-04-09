package com.jean.database.core.meta;

/**
 * 键值对信息
 *
 * @author jinshubao
 */
public class KeyValuePair<K, V> {

    private K key;

    private V value;

    public KeyValuePair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

}
