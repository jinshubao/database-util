package com.jean.database.redis.model;

import javafx.beans.property.*;

import java.util.List;

public class RedisValueWrapper {

    private StringProperty uuid = new SimpleStringProperty(this, "uuid");
    private ObjectProperty<byte[]> key = new SimpleObjectProperty<>(this, "key");
    private StringProperty type = new SimpleStringProperty(this, "type");
    private LongProperty ttl = new SimpleLongProperty(this, "ttl");
    private LongProperty size = new SimpleLongProperty(this, "size");
    private List<RedisValue> values;

    public RedisValueWrapper(String uuid, byte[] key, String type, Long ttl, Long size, List<RedisValue> values) {
        this.uuid.set(uuid);
        this.key.set(key);
        this.type.set(type);
        this.ttl.set(ttl);
        this.size.set(size);
        this.values = values;
    }

    public String getUuid() {
        return uuid.get();
    }


    public void setUuid(String uuid) {
        this.uuid.set(uuid);
    }

    public byte[] getKey() {
        return key.get();
    }

    public ObjectProperty<byte[]> keyProperty() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key.set(key);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public long getTtl() {
        return ttl.get();
    }

    public LongProperty ttlProperty() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl.set(ttl);
    }

    public long getSize() {
        return size.get();
    }

    public LongProperty sizeProperty() {
        return size;
    }

    public void setSize(long size) {
        this.size.set(size);
    }

    public List<RedisValue> getValues() {
        return values;
    }

    public void setValues(List<RedisValue> values) {
        this.values = values;
    }
}
