package com.jean.database.gui.view.handler;

import javafx.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jinshubao
 */
public abstract class AbstractEventHandler<T> implements ICommonActionEventHandler<T>, IRefreshActionEventHandler<T>, IMouseEventHandler<T> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private Node parent;

    protected AbstractEventHandler(Node parent) {
        this.parent = parent;
    }

    protected Node getParent() {
        return this.parent;
    }

    protected <N extends Node> N lookup(String selector) {
        return (N) this.parent.lookup(selector);
    }

    public Logger getLogger() {
        return this.logger;
    }


    @Override
    public void onCreate(T t) {

    }

    @Override
    public void onOpen(T t) {

    }

    @Override
    public void onClose(T t) {

    }


    @Override
    public void onDelete(T t) {

    }

    @Override
    public void onDetails(T t) {

    }

    @Override
    public void onSelected(T t) {

    }

    @Override
    public void onMouseClick(T t) {

    }

    @Override
    public void onMouseDoubleClick(T t) {

    }

    @Override
    public void onCopy(T t) {

    }

    @Override
    public void refresh(T t) {

    }
}
