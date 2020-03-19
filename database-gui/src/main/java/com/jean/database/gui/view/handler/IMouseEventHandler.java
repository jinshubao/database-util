package com.jean.database.gui.view.handler;

public interface IMouseEventHandler<T> {

    void onMouseClick(T t);

    void onMouseDoubleClick(T t);

    void onSelected(T t);

    void onCopy(T t);
}
