package edu.cbrc;

public class oRemove implements Operation {

    private final double operationPermission = 7;
    private final String operationName = "REMOVE";
    private final String operationDescription = "Remove pre-existing item from inventory";

    @Override
    public double getPermission() {
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

    @Override
    public int run() {
        return 0;
    }
}