package com.jean.database.context;

import com.jean.database.task.BackgroundTask;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;

public interface ApplicationContext {

    /**
     * 添加一个tab
     * @param tab 新的tab
     */
    void addObjectTab(Tab tab);

    /**
     * 删除tab
     * @param tab 被删除的tab
     */
    void removeObjectTab(Tab tab);

    /**
     * 添加一个根节点
     * @param treeItem 节点
     */
    void addDatabaseItem(TreeItem treeItem);
    void setSelected(TreeItem treeItem);

    /**
     * 删除一个根节点
     * @param treeItem 节点
     */
    void removeDatabaseItem(TreeItem treeItem);

    void addFileMenus(MenuItem... menu);

    void addConnectionMenus(MenuItem... menu);

    void addCollectionMenus(MenuItem... menu);

    void addViewMenus(MenuItem... menu);

    void addToolsMenus(MenuItem... menu);

    void addWindowMenus(MenuItem... menu);

    void addHelpMenus(MenuItem... menu);

    void updateProgress(double progress);
    void updateMessage(String message);

    <V> void execute(BackgroundTask<V> task);

    void close();

}
