package com.jean.database.redis.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class RedisValue {

    private ObjectProperty<byte[]> key = new SimpleObjectProperty<>(this, "key");
    private ObjectProperty<byte[]> value = new SimpleObjectProperty<>(this, "getValue");
    private DoubleProperty score = new SimpleDoubleProperty(this, "score");

    public RedisValue(byte[] key, byte[] value) {
        this.key.set(key);
        this.value.set(value);
    }

    public RedisValue(byte[] key, byte[] value, double score) {
        this.key.set(key);
        this.value.set(value);
        this.score.set(score);
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

    public byte[] getValue() {
        return value.get();
    }

    public ObjectProperty<byte[]> valueProperty() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value.set(value);
    }

    public double getScore() {
        return score.get();
    }

    public DoubleProperty scoreProperty() {
        return score;
    }

    public void setScore(double score) {
        this.score.set(score);
    }
}
