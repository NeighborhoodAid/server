package de.wirvsvirus.neighborhoodaid.api;

public class ErrorResponse {

    private final int errorCode;
    private final String errorMessage;

    public ErrorResponse(int errorCode, String message) {
        this.errorCode = errorCode;
        this.errorMessage = message;
    }
}
