package com.jean.database.sql.view;

import com.jean.database.api.view.ViewContext;

public class TreeItemViewContext extends ViewContext {

    private final SQLObjectTab objectTab;
    private final SQLGeneralInfoTab generalInfoTab;
    private final SQLDDLInfoTab ddlInfoTab;

    public TreeItemViewContext(ViewContext viewContext, SQLObjectTab objectTab, SQLGeneralInfoTab generalInfoTab, SQLDDLInfoTab ddlInfoTab) {
        super(viewContext.getMenuBar(), viewContext.getDatabaseTreeView(), viewContext.getObjectTabPan(), viewContext.getInfoPan());
        this.objectTab = objectTab;
        this.generalInfoTab = generalInfoTab;
        this.ddlInfoTab = ddlInfoTab;
        viewContext.getObjectTabPan().getTabs().add(objectTab);
        viewContext.getInfoPan().getTabs().add(generalInfoTab);
        viewContext.getInfoPan().getTabs().add(ddlInfoTab);
    }

    public SQLObjectTab getObjectTab() {
        return objectTab;
    }

    public SQLGeneralInfoTab getGeneralInfoTab() {
        return generalInfoTab;
    }

    public SQLDDLInfoTab getDdlInfoTab() {
        return ddlInfoTab;
    }
}
