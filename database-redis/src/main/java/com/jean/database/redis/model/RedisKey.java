package com.jean.database.redis.model;

import com.jean.database.redis.RedisConnectionConfiguration;
import javafx.beans.property.*;

public class RedisKey {

    private ObjectProperty<RedisConnectionConfiguration> server = new SimpleObjectProperty<>(this, "server");

    private IntegerProperty database = new SimpleIntegerProperty(this, "database");

    private ObjectProperty<byte[]> key = new SimpleObjectProperty<>(this, "key");

    private StringProperty type = new SimpleStringProperty(this, "type");

    private LongProperty ttl = new SimpleLongProperty(this, "ttl");

    private LongProperty size = new SimpleLongProperty(this, "size");

    public RedisConnectionConfiguration getServer() {
        return server.get();
    }

    public ObjectProperty<RedisConnectionConfiguration> serverProperty() {
        return server;
    }

    public void setServer(RedisConnectionConfiguration server) {
        this.server.set(server);
    }

    public int getDatabase() {
        return database.get();
    }

    public IntegerProperty databaseProperty() {
        return database;
    }

    public void setDatabase(int database) {
        this.database.set(database);
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
}
