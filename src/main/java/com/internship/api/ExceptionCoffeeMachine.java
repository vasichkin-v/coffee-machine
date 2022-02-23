package com.internship.api;

public class ExceptionCoffeeMachine extends Exception {
    private final int code;
    private final String message;

    public ExceptionCoffeeMachine(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public final static ExceptionCoffeeMachine VOLUME_IS_INCORRECT = new ExceptionCoffeeMachine(100, "Possibly wrong volume.");
    public final static ExceptionCoffeeMachine NAME_IS_INCORRECT = new ExceptionCoffeeMachine(101, "Mistake! Possibly incorrect name.");
    public final static ExceptionCoffeeMachine CMD_IS_INCORRECT = new ExceptionCoffeeMachine(102, "Mistake! Cmd is empty.");
}
