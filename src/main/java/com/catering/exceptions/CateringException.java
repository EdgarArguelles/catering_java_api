package com.catering.exceptions;

import com.catering.pojos.responses.error.ErrorResponse;
import com.catering.pojos.responses.error.nesteds.NestedError;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

/**
 * Custom exception with user readable message
 */
@EqualsAndHashCode(callSuper = false, of = { "nestedErrors", "devMessage", "path" })
public abstract class CateringException extends RuntimeException implements GraphQLError {

    @Getter
    private List<NestedError> nestedErrors;

    @Setter
    private String devMessage;

    @Setter
    private List<Object> path;

    /**
     * Constructs a new exception with the specified user readable message. The
     * cause is not initialized, and may subsequently be initialized by a call to
     * {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for later
     *                retrieval by the {@link #getMessage()} method.
     */
    public CateringException(String message) {
        this(message, null);
    }

    /**
     * Constructs a new exception with the specified user readable message. The
     * cause is not initialized, and may subsequently be initialized by a call to
     * {@link #initCause}.
     *
     * @param message      the detail message. The detail message is saved for later
     *                     retrieval by the {@link #getMessage()} method.
     * @param nestedErrors nested errors displayed to users
     */
    public CateringException(String message, List<NestedError> nestedErrors) {
        super(message);
        this.nestedErrors = nestedErrors;
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorType getErrorType() {
        return null;
    }

    @Override
    public List<Object> getPath() {
        return path;
    }

    @Override
    public Map<String, Object> getExtensions() {
        ErrorResponse response = new ErrorResponse(getMessage(), devMessage, nestedErrors);
        HttpStatus errorCode = HttpStatus.INTERNAL_SERVER_ERROR;
        if (this instanceof CateringDontFoundException) {
            errorCode = HttpStatus.NOT_FOUND;
        } else if (this instanceof CateringAuthenticationException) {
            errorCode = HttpStatus.FORBIDDEN;
        } else if (this instanceof CateringValidationException) {
            errorCode = HttpStatus.BAD_REQUEST;
        }

        return Map.of("errorType", errorCode, "errorCode", errorCode.value(), "error", response.error());
    }
}