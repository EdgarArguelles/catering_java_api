package com.catering.exceptions;

import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class CateringDontFoundExceptionTest {

    /**
     * Should create basic constructor
     */
    @Test
    public void constructorBasic() {
        final String MESSAGE = "test";
        final CateringException exception = new CateringDontFoundException(MESSAGE);

        assertSame(MESSAGE, exception.getMessage());
        assertNull(exception.getNestedErrors());
    }
}