package com.catering.exceptions;

import com.catering.pojos.responses.error.ErrorResponse;
import com.catering.pojos.responses.error.nesteds.NestedError;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CateringExceptionTest {

    /**
     * Should get null Locations
     */
    @Test
    public void getLocations() {
        final String MESSAGE = "test";
        final CateringException exception = new CateringValidationException(MESSAGE);

        assertNull(exception.getLocations());
    }

    /**
     * Should get null ErrorType
     */
    @Test
    public void getErrorType() {
        final String MESSAGE = "test";
        final CateringException exception = new CateringValidationException(MESSAGE);

        assertNull(exception.getErrorType());
    }

    /**
     * Should get Path
     */
    @Test
    public void getPath() {
        final String MESSAGE = "test";
        final List<Object> PATH = List.of("test 1", "test 2");
        final CateringException exception = new CateringValidationException(MESSAGE);
        exception.setPath(PATH);

        assertSame(PATH, exception.getPath());
    }

    /**
     * Should get Extensions with InternalException
     */
    @Test
    public void getInternalExceptionExtensions() {
        final String MESSAGE = "test";
        final String DEV_MESSAGE = "dev test";
        final CateringException exception = new CateringInternalException(MESSAGE);
        exception.setDevMessage(DEV_MESSAGE);
        final ErrorResponse response = new ErrorResponse(MESSAGE, DEV_MESSAGE, null);

        final Map<String, Object> extensionsResult = exception.getExtensions();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, extensionsResult.get("errorType"));
        assertEquals(500, extensionsResult.get("errorCode"));
        assertEquals(response.error(), extensionsResult.get("error"));
    }

    /**
     * Should get Extensions with DontFoundException
     */
    @Test
    public void getDontFoundExceptionExtensions() {
        final String MESSAGE = "test";
        final String DEV_MESSAGE = "dev test";
        final CateringException exception = new CateringDontFoundException(MESSAGE);
        exception.setDevMessage(DEV_MESSAGE);
        final ErrorResponse response = new ErrorResponse(MESSAGE, DEV_MESSAGE, null);

        final Map<String, Object> extensionsResult = exception.getExtensions();

        assertEquals(HttpStatus.NOT_FOUND, extensionsResult.get("errorType"));
        assertEquals(404, extensionsResult.get("errorCode"));
        assertEquals(response.error(), extensionsResult.get("error"));
    }

    /**
     * Should get Extensions with AuthenticationException
     */
    @Test
    public void getAuthenticationExceptionExtensions() {
        final String MESSAGE = "test";
        final String DEV_MESSAGE = "dev test";
        final CateringException exception = new CateringAuthenticationException(MESSAGE);
        exception.setDevMessage(DEV_MESSAGE);
        final ErrorResponse response = new ErrorResponse(MESSAGE, DEV_MESSAGE, null);

        final Map<String, Object> extensionsResult = exception.getExtensions();

        assertEquals(HttpStatus.FORBIDDEN, extensionsResult.get("errorType"));
        assertEquals(403, extensionsResult.get("errorCode"));
        assertEquals(response.error(), extensionsResult.get("error"));
    }

    /**
     * Should get Extensions with ValidationException
     */
    @Test
    public void getValidationExceptionExtensions() {
        final String MESSAGE = "test";
        final String DEV_MESSAGE = "dev test";
        final List<NestedError> NESTED_ERRORS = Collections.emptyList();
        final CateringException exception = new CateringValidationException(MESSAGE, NESTED_ERRORS);
        exception.setDevMessage(DEV_MESSAGE);
        final ErrorResponse response = new ErrorResponse(MESSAGE, DEV_MESSAGE, NESTED_ERRORS);

        final Map<String, Object> extensionsResult = exception.getExtensions();

        assertEquals(HttpStatus.BAD_REQUEST, extensionsResult.get("errorType"));
        assertEquals(400, extensionsResult.get("errorCode"));
        assertEquals(response.error(), extensionsResult.get("error"));
    }
}