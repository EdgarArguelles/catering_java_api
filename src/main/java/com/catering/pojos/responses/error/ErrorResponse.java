package com.catering.pojos.responses.error;

import com.catering.pojos.responses.error.nesteds.NestedError;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Error Response pojo
 */
public record ErrorResponse(Error error) {

    /**
     * Create an instance without parameters
     */
    public ErrorResponse() {
        this((Error) null);
    }

    /**
     * Create an instance without specific developer message
     *
     * @param message message displayed to users
     */
    public ErrorResponse(String message) {
        this(message, null);
    }

    /**
     * Create an instance with specific developer message
     *
     * @param message    message displayed to users
     * @param devMessage message displayed to developers
     */
    public ErrorResponse(String message, String devMessage) {
        this(message, devMessage, null);
    }

    /**
     * Create an instance with specific developer message and nested errors
     *
     * @param message      message displayed to users
     * @param devMessage   message displayed to developers
     * @param nestedErrors nested errors displayed to users
     */
    public ErrorResponse(String message, String devMessage, List<NestedError> nestedErrors) {
        this(new Error(message, devMessage, nestedErrors));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record Error(String message, String devMessage, List<NestedError> nestedErrors) {
    }
}