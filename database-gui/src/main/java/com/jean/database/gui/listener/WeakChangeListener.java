package com.jean.database.gui.listener;

import javafx.beans.WeakListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.lang.ref.WeakReference;

/**
 * 弱引用监听器，防止内存泄露
 *
 * @param <T> 监听器
 * @author jinshubao
 */
public class WeakChangeListener<T> implements ChangeListener<T>, WeakListener {

    private final WeakReference<ChangeListener<T>> ref;

    public WeakChangeListener(ChangeListener<T> listener) {
        if (listener == null) {
            throw new NullPointerException("监听器不能为空");
        }
        this.ref = new WeakReference<>(listener);
    }

    @Override
    public boolean wasGarbageCollected() {
        //判断监听器是否被回收
        return (ref.get() == null);
    }


    @Override
    public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
        final ChangeListener<T> listener = ref.get();
        if (listener != null) {
            listener.changed(observable, oldValue, newValue);
        } else {
            // 监听器被回收
            observable.removeListener(this);
        }
    }
}
