package com.jean.database.gui.view.handler;

public interface IMouseEventHandler<T> {

    default void onClick(T t) {
    }

    default void onDoubleClick(T t) {
    }

    default void onSelected(T t) {
    }

    default void onCopy(T t) {
    }
}
