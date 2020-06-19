package com.jean.database.redis.view;

import com.jean.database.api.view.ViewContext;

public class RedisViewContext extends ViewContext {

    private final RedisKeyTableTab keyTableTab;

    public RedisViewContext(ViewContext viewContext,RedisKeyTableTab keyTableTab) {
        super(viewContext.getMenuBar(), viewContext.getDatabaseTreeView(), viewContext.getObjectTabPan(), viewContext.getInfoPan());
        this.keyTableTab = keyTableTab;
    }
}
