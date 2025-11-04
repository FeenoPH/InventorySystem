package edu.cbrc;

public class oExit implements Operation {

    private final double operationPermission = 1;
    private final String operationName = "EXIT";
    private final String operationDescription = "Exit program";

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
        return 1;
    }
}
