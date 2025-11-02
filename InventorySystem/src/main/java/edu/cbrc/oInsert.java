package org.example;

public class oInsert implements Operation {

    private String operationPermission;
    private String operationName;

    public oInsert(String operationPermission, String operationName) {
        this.operationPermission = operationPermission;
        this.operationName = operationName;
    }
    @Override
    public String getPermission() {
        return operationPermission;
    }

    @Override
    public String getName() {
        return operationName;
    }
}
