package br.com.raroacademy.demo.exception;

public class UsedEmailException extends RuntimeException {
    public UsedEmailException(String message) {
        super(message);
    }
}
