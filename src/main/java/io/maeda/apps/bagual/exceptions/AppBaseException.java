package io.maeda.apps.bagual.exceptions;

public abstract class AppBaseException extends RuntimeException {
    public AppBaseException(String message) {
        super(message);
    }
}
