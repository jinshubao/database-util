package com.jean.database.mysql;

public class ExecuteStatus {
    private String variableName;
    private String value;
    private String description;

    public ExecuteStatus() {
    }

    public ExecuteStatus(String variableName, String value, String description) {
        this.variableName = variableName;
        this.value = value;
        this.description = description;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
