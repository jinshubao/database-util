package com.jean.database.mysql.handler;

public interface ServerTreeItemActionHandler {
    void open();

    void close();

    void create();

    void copy();

    void delete();

    void properties();

    void commandLine();

    void executeSqlFile();

    void dataTransform();

    void refresh();

}
