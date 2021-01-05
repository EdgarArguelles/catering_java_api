package com.catering.exceptions;

import com.catering.pojos.responses.error.nesteds.NestedError;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class CateringValidationExceptionTest {

    /**
     * Should create basic constructor
     */
    @Test
    public void constructorBasic() {
        final String MESSAGE = "test";
        final CateringException exception = new CateringValidationException(MESSAGE);

        assertSame(MESSAGE, exception.getMessage());
        assertNull(exception.getNestedErrors());
    }

    /**
     * Should create complete constructor
     */
    @Test
    public void constructorComplete() {
        final String MESSAGE = "test";
        final List<NestedError> NESTED_ERRORS = Collections.emptyList();
        final CateringException exception = new CateringValidationException(MESSAGE, NESTED_ERRORS);

        assertSame(MESSAGE, exception.getMessage());
        assertSame(NESTED_ERRORS, exception.getNestedErrors());
    }
}