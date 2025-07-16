package br.com.raroacademy.demo.exception;

public class ExpectedHiringAlreadyExistsException extends RuntimeException {
    private final String messageKey;
    public ExpectedHiringAlreadyExistsException(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
