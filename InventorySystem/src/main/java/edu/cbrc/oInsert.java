package edu.cbrc;

public class oInsert implements Operation {

    private final String operationPermission = "reduced";
    private final String operationName = "INSERT";
    private final String operationDescription = "Insert new item into inventory";

    @Override
    public String getPermission() {
        return operationPermission;
    }

    @Override
    public String getName() {
        return operationName;
    }

    @Override
    public String getDescription() {
        return operationDescription;
    }
}
